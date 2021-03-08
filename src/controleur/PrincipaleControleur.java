package controleur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfaceAllumettes;
import modele.interfaceRMI.InterfacePendu;


// UI client
public class PrincipaleControleur {
	
	@FXML private Button btn_allumette;
	@FXML private Button btn_pendu;
	
	@FXML
	public void pendu() {
		System.out.println(System.getProperty("args"));

		String hote = "localhost";
		int port = 3000;
			try {
				InterfacePendu obj = (InterfacePendu) Naming.lookup("rmi://" + hote + ":" + port + "/Pendu");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				e1.printStackTrace();
			}

		// try {
		// 	String hote = args[0];
		// 	int port = Integer.parseInt(args[1]);
		// 	InterfacePendu obj = (InterfacePendu) Naming.lookup("rmi://" + hote + ":" + port + "/Pendu");
		// } catch (Exception e) {
		// 	System.out.println("Une erreur est survenue:\n" + e);
		// }

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
		Scene scene = new Scene((Parent) root, 600, 400);
		nStage.setScene(scene);
		nStage.setResizable(false);
		nStage.setTitle("Jeu des allumettes");
		nStage.initModality(Modality.APPLICATION_MODAL);
		
		nStage.show();
	}
	
	
	@FXML
	public void allumette() {
		try {
			int port = 3001;
			InterfaceAllumettes obj;
				obj = (InterfaceAllumettes) Naming.lookup ("rmi://localhost:"+port+"/Allumettes");
			
			UUID uuid = obj.creerPartie();
			obj.initialise(uuid);
			
			System.out.println("UUID partie = " + uuid);
			
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
			allumetteControleur.initialisation(uuid);
			
			nStage.setOnCloseRequest(event -> {
				allumetteControleur.retour();
			});
			
			nStage.show();
			
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}
	}
}
