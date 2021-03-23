package controleur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfaceAllumettes;


// UI client
public class PrincipalControleur implements Initializable {
	
	@FXML private VBox box_info_allumettes;
	@FXML private VBox box_info_pendu;
	@FXML private VBox box_info_tic_tac_toe;
	
	@FXML private TextField tf_nomJoueur;
	
	@FXML private Button btn_info_allumette;
	@FXML private Button btn_jouer_allumettes_solo;
	@FXML private Button btn_jouer_allumettes_multi;
	
	@FXML private Button btn_info_pendu;
	@FXML private Button btn_jouer_pendu;
	
	@FXML private Button btn_info_ticTacToe;
	@FXML private Button btn_jouer_ticTacToe;
	
	
	/**
	 * Lance une partie de pendu
	 */
	@FXML
	public void pendu() {
		Stage nStage = new Stage();
		
		URL fxmlURL=getClass().getResource("../vue/PenduVue.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
		Node root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//On affiche la fenetre du jeu des allumettes
		Scene scene = new Scene((AnchorPane) root, 720, 480);
		nStage.setScene(scene);
		nStage.setResizable(false);
		nStage.setTitle("Jeu du pendu");
		nStage.initModality(Modality.APPLICATION_MODAL);
		nStage.getIcons().add(new Image("/vue/icones/penduIco.png"));
		
		nStage.show();
	}
	
	
	/**
	 * Affiche les informations du jeu des allumettes
	 */
	@FXML
	public void info_allumettes() {
		box_info_allumettes.setMinWidth(300);
		box_info_allumettes.setMaxWidth(350);
		box_info_allumettes.setVisible(true);
		
		box_info_pendu.setMinWidth(0);
		box_info_pendu.setMaxWidth(0);
		box_info_pendu.setVisible(false);
		
		box_info_tic_tac_toe.setMinWidth(0);
		box_info_tic_tac_toe.setMaxWidth(0);
		box_info_tic_tac_toe.setVisible(false);
		
		btn_jouer_allumettes_solo.setDisable(true);
		btn_jouer_allumettes_multi.setDisable(true);
	}
	
	
	/**
	 * Affiche les informations du jeu du pendu
	 */
	@FXML
	public void info_pendu() {
		box_info_pendu.setMinWidth(300);
		box_info_pendu.setMaxWidth(350);
		box_info_pendu.setVisible(true);
		
		box_info_allumettes.setMinWidth(0);
		box_info_allumettes.setMaxWidth(0);
		box_info_allumettes.setVisible(false);
		
		box_info_tic_tac_toe.setMinWidth(0);
		box_info_tic_tac_toe.setMaxWidth(0);
		box_info_tic_tac_toe.setVisible(false);
	}
	
	
	/**
	 * Affiche les informations du tic tac toe
	 */
	@FXML
	public void info_ticTacToe() {
		box_info_tic_tac_toe.setMinWidth(300);
		box_info_tic_tac_toe.setMaxWidth(350);
		box_info_tic_tac_toe.setVisible(true);
		
		box_info_allumettes.setMinWidth(0);
		box_info_allumettes.setMaxWidth(0);
		box_info_allumettes.setVisible(false);
		
		box_info_pendu.setMinWidth(0);
		box_info_pendu.setMaxWidth(0);
		box_info_pendu.setVisible(false);
	}
	
	
	/**
	 * Verifie que le nom du joueur pour les allumettes fait au moins 3 caracteres
	 */
	@FXML
	public void verifNomJoueur() {
		if (tf_nomJoueur.getText().trim().length() < 3) {
			btn_jouer_allumettes_solo.setDisable(true);
			btn_jouer_allumettes_multi.setDisable(true);
		}
		else {
			btn_jouer_allumettes_solo.setDisable(false);
			btn_jouer_allumettes_multi.setDisable(false);
		}
	}
	
	
	/**
	 * Lance une partie du jeu des allumettes en solo
	 */
	@FXML
	private void jouer_allumettes_solo() {
		int port = 3000;
		InterfaceAllumettes obj;
		try {
			obj = (InterfaceAllumettes) Naming.lookup ("rmi://localhost:" + port + "/Allumettes");
			
			UUID uuid = obj.creerPartie("solo");
			obj.initialise(uuid, "solo", tf_nomJoueur.getText().trim());
			
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
			nStage.getIcons().add(new Image("/vue/icones/allumetteIco.png"));
			
			AllumetteControleur allumetteControleur = fxmlLoader.getController();
			
			allumetteControleur.setInterfaceAllumettes(obj);
			allumetteControleur.initialisation(uuid, 0);
			
			nStage.setOnCloseRequest(e -> {
				allumetteControleur.retour();
			});
			
			nStage.showAndWait();
			
			tf_nomJoueur.clear();
			verifNomJoueur();
			
			obj.finPartie(uuid);
		
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * Lance la fenetre d'affichage des parties d'allumettes disponibles
	 */
	@FXML
	private void jouer_allumettes_multi() {
		Stage nStage = new Stage();
		
		URL fxmlURL = getClass().getResource("../vue/MultiAllumettesVue.fxml");
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
		nStage.setTitle("Liste des parties");
		nStage.initModality(Modality.APPLICATION_MODAL);
		nStage.getIcons().add(new Image("/vue/icones/allumetteIco.png"));
		
		MultiAllumettesControleur multiAllumettesControleur = fxmlLoader.getController();
		multiAllumettesControleur.setNomJoueur(this.tf_nomJoueur.getText().trim());
		
		nStage.showAndWait();
	}

	
	/**
	 * Lance une partie du tic tac toe
	 */
	@FXML
	public void ticTacToe() {
		Stage nStage = new Stage();
		
		URL fxmlURL=getClass().getResource("../vue/TicTacToeVue.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
		Node root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//On affiche la fenetre du jeu des allumettes
		Scene scene = new Scene((AnchorPane) root, 720, 480);
		nStage.setScene(scene);
		nStage.setResizable(false);
		nStage.setTitle("Jeu du Tic-Tac-Toe");
		nStage.initModality(Modality.APPLICATION_MODAL);
		nStage.getIcons().add(new Image("/vue/icones/tictactoeIco.png"));
		
		nStage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.box_info_allumettes.setVisible(false);
		this.box_info_pendu.setVisible(false);
		this.box_info_tic_tac_toe.setVisible(false);
		
	}
}
