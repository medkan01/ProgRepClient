package controleur;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.UUID;
import modele.interfaceRMI.InterfaceAllumettes;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AllumetteControleur implements Initializable {
	@FXML private Button btn_valider;
	@FXML private HBox pane;
	@FXML private Label lbl_tour;
	@FXML private Label lbl_scoreJ1;
	@FXML private Label lbl_scoreJ2;
	@FXML private Button btn_retour;
	
	private int nbAllChoisies = 0;
	private Node tabAllumetteRetirer[] = new Node [2];
	private InterfaceAllumettes interfaceAllumettes;
	private UUID idPartie;
	
	/**
	 * Affiche les allumettes,
	 * le premier joueur a devoir jouer,
	 * lance le premier tour de jeu
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			this.interfaceAllumettes = (InterfaceAllumettes) Naming.lookup ("rmi://localhost:3000/allumettes");
			
			this.idPartie = this.interfaceAllumettes.creerPartie();
			
			this.interfaceAllumettes.initialise(this.idPartie);
			
			afficheAllumettes( interfaceAllumettes.getNbAllumettes(this.idPartie) );
			affPremierJoueur(interfaceAllumettes.nomJoueurTour(this.idPartie));
			tour();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Execute un tour de jeu
	 */
	private void tour() {
		try {
			affTourJoueur(interfaceAllumettes.nomJoueurTour(this.idPartie));
			
			if ( interfaceAllumettes.getTour(idPartie) %2 == 0) {
				new Thread(() -> {
					try {
						for (Node node : pane.getChildren().filtered(t->t.isVisible())) {
							node.setDisable(true);
						}
						
						Thread.sleep(800);
						//Le serveur choisit un nombre d'allumettes
						try {
							this.nbAllChoisies = interfaceAllumettes.coupIA(this.idPartie);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						
						//Choix des allumettes sur l'interface graphique
						tourIA(this.nbAllChoisies);
						
						//actualisation de l'affichage
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								valider();
							}
						});
						
						for (Node node : pane.getChildren().filtered(t->t.isVisible())) {
							node.setDisable(false);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creer et affiche le nombre d'allumettes specifie
	 * @param nbAllumette Le nombre d'allumettes de la partie
	 */
	private void afficheAllumettes(int nbAllumette) {
		
		for (int i=1; i<=nbAllumette; i++) {
			String id = "allumette".concat(String.valueOf(i));
			
			Button button = new Button();
			button.setId(id);
			button.setOnAction(a -> {
				choixAllumette(a);
			});
			button.setMinWidth(5);
			button.setMaxWidth(5);
			button.setPrefWidth(5);

			button.setMinHeight(150);
			button.setMaxHeight(150);
			button.setPrefHeight(150);
			
			pane.getChildren().add(button);
		}
		
		btn_valider.setDisable(true);
	}
	
	
	/**
	 * Affiche une alerte du premier jouer e devoir jouer
	 * @param nomPJoueur Le nom du premier joueur
	 */
	private void affPremierJoueur(String nomPJoueur) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Premier joueur");
		alert.setContentText("Le premier joueur a jouer est : " + nomPJoueur);
		alert.showAndWait();
	}
	
	
	/**
	 * Selectionne les nbAlluChoisies premieres allumettes disponibles
	 * @param nbAlluChoisies Le nombre d'allumettes choisies
	 */
	private void tourIA(int nbAlluChoisies) {
		//Liste de toutes les allumettes pas encore selectionnees
		ObservableList<Node> alluVisibles = pane.getChildren().filtered(t->t.isVisible());
		
		//on selectionne les "nbAllChoisies" premieres allumettes
		for (int i=0; i<nbAlluChoisies; i++) {
			tabAllumetteRetirer[i] = alluVisibles.get(i);
		}
	}
	
	
	/**
	 * Affiche le tableau des scores
	 * @param tabScore Le tableau des scores
	 */
	private void affTabScore(int[] tabScore) {
		lbl_scoreJ1.setText(String.valueOf(tabScore[0]));
		lbl_scoreJ2.setText(String.valueOf(tabScore[1]));
	}

	
	/**
	 * Affiche le nom du joueur qui doit jouer
	 * @param nomJoueur Le nom du joueur
	 */
	private void affTourJoueur(String nomJoueur) {
		this.lbl_tour.setText(nomJoueur);
	}
	
	/**
	 * Selectionne ou deselectionne une allumette lorsque l'on clique dessus
	 * @param event
	 */
	@FXML
	private void choixAllumette(ActionEvent event) {
		//On recupere la Node sur laquelle on a clique
		final Node source = (Node) event.getSource();
		
		//Si on a deje modifie son style, donc != "", alors ea veut dire qu'on a deje clique dessus
		if (source.getStyle() == "") {
			//Comme on ne peut choisir que 2 allumettes max, on ne selectionne cette allumette que si nbAllChoisies != 2
			//Si on la choisie, on la rajoute dans un tableau, on modifie son style et on incremente nbAllChoisies
			if (nbAllChoisies != 2) {
				nbAllChoisies += 1;
				if (tabAllumetteRetirer[0] == null)
					tabAllumetteRetirer[0] = source;
				else 
					tabAllumetteRetirer[1] = source;
				
				source.setStyle("-fx-background-color: red;");
			}
		}
		//Si on a deje clique dessus, on la retire du tableau, on remet le style de base et on decremente nbALLChoisies
		else {
			nbAllChoisies -= 1;
			if (tabAllumetteRetirer[0] != null) {
				if (tabAllumetteRetirer[0].equals(source))
					tabAllumetteRetirer[0] = null;
				else 
					tabAllumetteRetirer[1] = null;
			}
			else 
				tabAllumetteRetirer[1] = null;
			
			source.setStyle("");
		}
		
		//Si aucune allumette n'est selectionnee, alors le bouton Valider n'est pas actif
		if ( (tabAllumetteRetirer[0] != null) || (tabAllumetteRetirer[1] != null) )
			btn_valider.setDisable(false);
		else
			btn_valider.setDisable(true);
	}
	
	/**
	 * affiche une alerte d'aide
	 */
	@FXML
	private void aide() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Aide");
		alert.setContentText("Le jeu des allumettes ! \n    Un tas d'allumettes est dispose sur la table, il y en a un nombre impair."
				+ "\n    Chaque joueur prend, tour e tour, 1 ou 2 allumettes dans le tas (vous pouvez essayez de tricher ea ne fonctionnera pas !"
				+ " La partie se termine quand il n'y a plus d'allumettes sur la table. Le gagant est celui qui aura un nombre impair d'allumettes e la fin !"
				+ "\n    Le nombre d'allumettes de depart est aleatoire, tout comme le joueur qui commence. Que le meilleur gagne !"
				+ "\n\nComment jouer ? \n    Mais c'est tres simple jammy ! Il suffit de cliquer sur une allumette presente sur la table pour la selectionner !"
				+ "\n    Pour valider votre selection il suffit de cliquer sur le bouton \"Valider\". ");
		
		alert.showAndWait();
	}
	
	/**
	 * Appelee lors du clic sur le bouton "btn_valider",
	 * actualise les donnees serveur en fonction du nombre d'allumettes choisies,
	 * actualise le tableau des scores,
	 * retire les allumettes selectionnees de l'affichage,
	 * relance un tour de jeu si la partie n'est pas finie
	 */
	@FXML
	private void valider() {
		
		try {
			//actualisation des donnees sur le serveur
			interfaceAllumettes.action(this.idPartie, this.nbAllChoisies);
			
			//Actualisation des scores
			affTabScore( interfaceAllumettes.getTabScore(idPartie));
			
			//On retire les allumettes selectionnees de l'affichage
			for (int i=0; i<tabAllumetteRetirer.length; i++) {
				if (tabAllumetteRetirer[i] != null) {
					tabAllumetteRetirer[i].setVisible(false);
					tabAllumetteRetirer[i] = null;
				}
			}
			
			if ( interfaceAllumettes.getNbAllumettes(idPartie) == 0) {
				finPartie(interfaceAllumettes.nomGagnant(this.idPartie), interfaceAllumettes.scoreGagnant(this.idPartie));
			}
			else {
				//On reinitialise les variables necessaires e un tour de jeu
				nbAllChoisies = 0;
				
				tour();
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ferme la fenetre de jeu
	 */
	@FXML
	public void retour() {
	    Stage stage = (Stage) btn_retour.getScene().getWindow();
	    stage.close();
	}
	
	/**
	 * Affiche une alerte de fin de partie avec le gagnant et son score. Ferme ensuite la fenetre
	 * @param nomGagnant Le nom du joueur gagnant
	 * @param scoreGagnant Le score du joueur gagnant
	 */
	private void finPartie(String nomGagnant, int scoreGagnant) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Fin de partie");
		alert.setContentText("Fin de la partie ! Le gagnant est " + nomGagnant + " avec un score de " + scoreGagnant );
		
		alert.showAndWait();
		retour();
	}
}
