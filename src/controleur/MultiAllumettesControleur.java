package controleur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Pair;
import modele.interfaceRMI.InterfaceAllumettes;

public class MultiAllumettesControleur implements Initializable {
	
	@FXML private TableView<Pair> tabParties;
	@FXML private TableColumn<Pair, String> idCol;
	@FXML private TableColumn<Pair, String> joueurCol;
	
	@FXML private Button btn_rejoindre;
	@FXML private Button btn_retour;
	@FXML private Button btn_creer;
	@FXML private Button btn_actualiser;
	
	@FXML private TextField tf_idPartie;
	@FXML private TextField tf_joueur;
	
	private String nomJoueur;
	private InterfaceAllumettes allumettes;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.idCol.setCellValueFactory(new PropertyValueFactory<Pair, String>("uuid"));
		this.joueurCol.setCellValueFactory(new PropertyValueFactory<Pair, String>("joueur"));
		this.btn_rejoindre.setDisable(true);
		
		try {
			this.allumettes = (InterfaceAllumettes) Naming.lookup("rmi://localhost:3000/Allumettes");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		//Listener qui definit si les boutons sont desactives ou non
		this.tabParties.getSelectionModel().selectedItemProperty().addListener(
				(observale, odlValue, newValue) -> {
					this.btn_rejoindre.setDisable(newValue == null);
				});
		
		//evenement qui permet d'enlever la selection actuelle dans la tableView si on clique sur une ligne vide 
		this.tabParties.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
		    Node source = evt.getPickResult().getIntersectedNode();
		   
		    // move up through the node hierarchy until a TableRow or scene root is found 
		    while (source != null && !(source instanceof TableRow)) {
		        source = source.getParent();
		    }

		    // clear selection on click anywhere but on a filled row
		    if (source == null || (source instanceof TableRow && ((TableRow<?>) source).isEmpty())) {
		    	tabParties.getSelectionModel().clearSelection();
		    }
		});
		
		remplirTable(null);
	}
	
	private void remplirTable(UUID key) {
		try {
			ObservableList<Pair> data = FXCollections.observableArrayList();

			allumettes.getJoueursParties().forEach((k,v) -> {
				if (k != key)
					data.add(new Pair(k.toString(), v));
			});
			
			tabParties.setItems(data);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void setNomJoueur(String nom) {
		this.nomJoueur = nom;
	}
	
	@FXML
	private void rejoindre() {
		UUID uuid = UUID.fromString(tabParties.getSelectionModel().getSelectedItem().getUuid());
		
		try {
			if (allumettes.getNbJoueurs(uuid) != 2) {
				allumettes.rejoindrePartie(uuid, nomJoueur);
				
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
				
				allumetteControleur.setInterfaceAllumettes(allumettes);
				allumetteControleur.initialisation(uuid, 1);
				
				nStage.setOnCloseRequest(e -> {
					allumetteControleur.retour();
				});
				
				nStage.showAndWait();
				
				allumettes.finPartie(uuid);
			}
			else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Partie indisponible");
				alert.setContentText("Il me semble que la partie est actuellement indisponible, surement quelqu'un qui a rejoint avant vous ...");
				alert.showAndWait();
			}
				
			remplirTable(uuid);
		
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NullPointerException e1) {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Partie indisponible");
			alert.setContentText("Il me semble que la partie est actuellement indisponible, surement quelqu'un qui a rejoint avant vous ...");
			alert.showAndWait();
			
			remplirTable(uuid);
		}
	}
	
	@FXML
	private void creer() {
		try {
			UUID uuid = allumettes.creerPartie(nomJoueur);
			allumettes.initialise(uuid, "duo", this.nomJoueur);
			
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
			
			allumetteControleur.setInterfaceAllumettes(allumettes);
			allumetteControleur.initialisation(uuid, 0);
			nStage.getIcons().add(new Image("/vue/icones/allumetteIco.png"));
			
			nStage.setOnCloseRequest(e -> {
				allumetteControleur.retour();
			});
			
			nStage.showAndWait();
			
			allumettes.finPartie(uuid);
		
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
	
	
	@FXML private void actualiser() {
		remplirTable(null);
		filtrerIdPartie();
		filtrerJoueur();
	}
	
	@FXML
	private void retour() {
		Stage stage = (Stage) btn_retour.getScene().getWindow();
	    stage.close();
	}
	
	@FXML
	private void filtrerIdPartie() {
		ObservableList<Pair> listeParties = FXCollections.observableArrayList();
		String id = this.tf_idPartie.getText().trim();
		
		try {
			allumettes.getJoueursParties().forEach((k,v) -> {
				String key = k.toString();
				for (int i=0; i<key.length(); i++) {
					for (int j=i; j<key.length(); j++) {
						if (key.subSequence(i, j).equals(id) && !listeParties.contains(new Pair(key, v))) {
							listeParties.add(new Pair(k.toString(), v));
						}
					}
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		tabParties.setItems(listeParties);
	}
	
	@FXML
	private void filtrerJoueur() {
		ObservableList<Pair> listeParties = FXCollections.observableArrayList();
		String nomJoueur = this.tf_joueur.getText().trim();
		
		try {
			allumettes.getJoueursParties().forEach((k,v) -> {
				for (int i=0; i<v.length(); i++) {
					for (int j=i; j<v.length(); j++) {
						Pair aAjouter = new Pair(k.toString(), v);
						
						if (v.subSequence(i, j).equals(nomJoueur) && !listeParties.contains(aAjouter))
							listeParties.add(aAjouter);
					}
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		tabParties.getItems().clear();
		tabParties.setItems(listeParties);
	}
}
