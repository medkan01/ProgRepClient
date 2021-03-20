package controleur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfaceAllumettes;

public class AllumettesChoixModeControleur implements Initializable {
	
	@FXML private Label lbl;
	@FXML private TextField tf_nomJoueur;
	@FXML private Button solo;
	@FXML private Button duo;
	
	@FXML
	public void verifNomJoueur() {
		if (tf_nomJoueur.getText().trim().length() < 3) {
			solo.setDisable(true);
			duo.setDisable(true);
		}
		else {
			solo.setDisable(false);
			duo.setDisable(false);
		}
	}
	
	@FXML
	private void click(ActionEvent event) {
		int joueur = 0;
		
		//On recupere la Node sur laquelle on a clique
		final Node source = (Node) event.getSource();
		
		int port = 3000;
		InterfaceAllumettes obj;
		try {
			obj = (InterfaceAllumettes) Naming.lookup ("rmi://localhost:"+port+"/Allumettes");
			
			UUID uuid = obj.creerPartie(source.getId());
			
			//Si on lance une partie solo ou qu'une partie duo est créée on initialise 
			if ( source.getId().equals("solo") || (source.getId().equals("duo") && obj.getNbJoueurs(uuid) == 0) ) {
				obj.initialise(uuid, source.getId(), tf_nomJoueur.getText().trim());
				joueur = 0;
			}
			else {
				obj.rejoindrePartie(uuid, tf_nomJoueur.getText().trim());
				joueur = 1;
			}
				
			lbl.setVisible(true);
			
			Stage nStage = new Stage();
			
			URL fxmlURL=getClass().getResource("../vue/AllumettesVue.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			Node root = null;
			try {
				root = fxmlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//On affiche la fenetre du jeu des allumettes
			Scene scene = new Scene((VBox) root, 600, 400);
			nStage.setScene(scene);
			nStage.setResizable(false);
			nStage.setTitle("Jeu des allumettes");
			nStage.initModality(Modality.APPLICATION_MODAL);
			
			AllumetteControleur allumetteControleur = fxmlLoader.getController();
			
			allumetteControleur.setInterfaceAllumettes(obj);
			allumetteControleur.initialisation(uuid, joueur);
			
			nStage.setOnCloseRequest(e -> {
				allumetteControleur.retour();
			});
			
			nStage.show();
			
			//On ferme la fenêtre actuelle
		    Stage stage = (Stage) source.getScene().getWindow();
		    stage.close();
		
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lbl.setVisible(false);
		
		solo.setDisable(true);
		duo.setDisable(true);
	}
}
