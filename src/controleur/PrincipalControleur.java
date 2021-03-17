package controleur;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


// UI client
public class PrincipalControleur {

	@FXML private Button btn_allumette;
	@FXML private Button btn_pendu;
	@FXML private Button btn_ticTacToe;
	
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
		
		nStage.show();
	}
	
	
	@FXML
	public void allumette() {
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
		
		nStage.setOnCloseRequest(event -> {
			try {
				allumetteControleur.retour();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		nStage.show();
	}

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
		
		nStage.show();
	}
}
