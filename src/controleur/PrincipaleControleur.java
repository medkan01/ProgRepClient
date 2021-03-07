package controleur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.UUID;


// UI client
public class PrincipaleControleur {
	
	@FXML private Button btn_allumette;
	@FXML private Button btn_pendu;
	
	@FXML
	public void pendu() {
		Stage nStage = new Stage();
		
		URL fxmlURL=getClass().getResource("/client/vue/PenduView.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
		Node root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//On affiche la fenetre du jeu des allumettes
		Scene scene = new Scene((AnchorPane) root, 600, 400);
		nStage.setScene(scene);
		nStage.setResizable(false);
		nStage.setTitle("Jeu des allumettes");
		nStage.initModality(Modality.APPLICATION_MODAL);
		
		nStage.show();
	}
	
	
	@FXML
	public void allumette() {
		try {
			int port = 3000;
			InterfaceAllumettes obj;
				obj = (InterfaceAllumettes) Naming.lookup ("rmi://localhost:"+port+"/allumettes");
			
			UUID uuid = obj.creerPartie();
			obj.initialise(uuid);
			
			System.out.println("UUID partie = " + uuid);
			
			Stage nStage = new Stage();
			
			URL fxmlURL=getClass().getResource("/client/vue/allumettes.fxml");
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
