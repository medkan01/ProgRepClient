package controleur;

import java.rmi.RemoteException;
import java.util.UUID;
import modele.interfaceRMI.InterfaceAllumettes;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AllumetteControleur {
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
	 * {@code Affiche les allumettes,
	 * le premier joueur � devoir jouer,
	 * lance le premier tour de jeu}
	 */
	public void initialisation(UUID uuid) {
		try {
			this.idPartie = uuid;
			
			afficheAllumettes(interfaceAllumettes.getPartieAllumettes(uuid).getNbAllumettes());
			
			affPremierJoueur(interfaceAllumettes.nomJoueurTour(uuid));
			
			tour();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@code Ex�cute un tour de jeu} 
	 */
	private void tour() {
		try {
			affTourJoueur(interfaceAllumettes.nomJoueurTour(this.idPartie));
			
			if (interfaceAllumettes.getPartieAllumettes(this.idPartie).getTour()%2 == 0) {
				new Thread(() -> {
					try {
						for (Node node : pane.getChildren().filtered(t->t.isVisible())) {
							node.setDisable(true);
						}
						
						Thread.sleep(800);
						//Le serveur choisit un nombre d'allumettes
						try {
							nbAllChoisies = interfaceAllumettes.coupIA(this.idPartie);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						
						//Choix des allumettes sur l'interface graphique
						tourIA(nbAllChoisies);
						
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
	
	public void setInterfaceAllumettes(InterfaceAllumettes interfaceAllumettes) {
		this.interfaceAllumettes = interfaceAllumettes;
	}

	/**
	 * {@code Cr�er et affiche le nombre d'allumettes sp�cifi�}
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
	 * {@code Affiche une alerte du premier jouer � devoir jouer}
	 * @param nomPJoueur Le nom du premier joueur
	 */
	private void affPremierJoueur(String nomPJoueur) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Premier joueur");
		alert.setContentText("Le premier joueur � jouer est : " + nomPJoueur);
		alert.showAndWait();
	}
	
	
	/**
	 * {@code S�lectionne les nbAlluChoisies premi�res allumettes disponibles}
	 * @param nbAlluChoisies Le nombre d'allumettes choisies
	 */
	private void tourIA(int nbAlluChoisies) {
		//Liste de toutes les allumettes pas encore selectionn�es
		ObservableList<Node> alluVisibles = pane.getChildren().filtered(t->t.isVisible());
		
		//on s�lectionne les "nbAllChoisies" premi�res allumettes
		for (int i=0; i<nbAlluChoisies; i++) {
			tabAllumetteRetirer[i] = alluVisibles.get(i);
		}
	}
	
	
	/**
	 * {@code Affiche le tableau des scores}
	 * @param tabScore Le tableau des scores
	 */
	private void affTabScore(int[] tabScore) {
		//On affiche le tableau des scores actualis�
		lbl_scoreJ1.setText(String.valueOf(tabScore[0]));
		lbl_scoreJ2.setText(String.valueOf(tabScore[1]));
	}

	
	/**
	 * {@code Affiche le nom du joueur qui doit jouer}
	 * @param nomJoueur Le nom du joueur
	 */
	private void affTourJoueur(String nomJoueur) {
		this.lbl_tour.setText(nomJoueur);
	}
	
	/**
	 * {@code S�lectionne ou d�s�lectionne une allumette lorsque l'on clique dessus}
	 * @param event
	 */
	@FXML
	private void choixAllumette(ActionEvent event) {
		//On recup�re la Node sur laquelle on a cliqu�
		final Node source = (Node) event.getSource();
		
		//Si on a d�j� modifi� son style, donc != "", alors �a veut dire qu'on a d�j� cliqu� dessus
		if (source.getStyle() == "") {
			//Comme on ne peut choisir que 2 allumettes max, on ne s�lectionne cette allumette que si nbAllChoisies != 2
			//Si on la choisie, on la rajoute dans un tableau, on modifie son style et on incr�mente nbAllChoisies
			if (nbAllChoisies != 2) {
				nbAllChoisies += 1;
				if (tabAllumetteRetirer[0] == null)
					tabAllumetteRetirer[0] = source;
				else 
					tabAllumetteRetirer[1] = source;
				
				source.setStyle("-fx-background-color: red;");
			}
		}
		//Si on a d�j� cliqu� dessus, on la retire du tableau, on remet le style de base et on d�cr�mente nbALLChoisies
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
		
		//Si aucune allumette n'est s�lectionn�e, alors le bouton Valider n'est pas actif
		if ( (tabAllumetteRetirer[0] != null) || (tabAllumetteRetirer[1] != null) )
			btn_valider.setDisable(false);
		else
			btn_valider.setDisable(true);
	}
	
	/**
	 * {@code affiche une alerte d'aide}
	 */
	@FXML
	private void aide() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Aide");
		alert.setContentText("Le jeu des allumettes ! \n    Un tas d'allumettes est dispos� sur la table, il y en a un nombre impair."
				+ "\n    Chaque joueur prend, tour � tour, 1 ou 2 allumettes dans le tas (vous pouvez essayez de tricher �a ne fonctionnera pas !"
				+ " La partie se termine quand il n'y a plus d'allumettes sur la table. Le gagant est celui qui aura un nombre impair d'allumettes � la fin !"
				+ "\n    Le nombre d'allumettes de d�part est al�atoire, tout comme le joueur qui commence. Que le meilleur gagne !"
				+ "\n\nComment jouer ? \n    Mais c'est tr�s simple jammy ! Il suffit de cliquer sur une allumette pr�sente sur la table pour la s�lectionner !"
				+ "\n    Pour valider votre s�lection il suffit de cliquer sur le bouton \"Valider\". ");
		
		alert.showAndWait();
	}
	
	/**
	 * {@code Appel�e lors du clic sur le bouton "btn_valider",
	 * actualise les donn�es serveur en fonction du nombre d'allumettes choisies,
	 * actualise le tableau des scores,
	 * retire les allumettes s�lectionn�es de l'affichage,
	 * relance un tour de jeu si la partie n'est pas finie}
	 */
	@FXML
	private void valider() {
		
		try {
			//actualisation des donn�es sur le serveur
			interfaceAllumettes.action(this.idPartie, nbAllChoisies);
			
			//Actualisation des scores
			affTabScore(interfaceAllumettes.getPartieAllumettes(this.idPartie).getTabScore());
			
			//On retire les allumettes s�lectionn�es de l'affichage
			for (int i=0; i<tabAllumetteRetirer.length; i++) {
				if (tabAllumetteRetirer[i] != null) {
					tabAllumetteRetirer[i].setVisible(false);
					tabAllumetteRetirer[i] = null;
				}
			}
			
			if (interfaceAllumettes.getPartieAllumettes(this.idPartie).getNbAllumettes() == 0) {
				finPartie(interfaceAllumettes.nomGagnant(this.idPartie), interfaceAllumettes.scoreGagnant(this.idPartie));
			}
			else {
				//On r�initialise les variables n�cessaires � un tour de jeu
				nbAllChoisies = 0;
				
				tour();
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@code Ferme la fen�tre de jeu}
	 */
	@FXML
	public void retour() {
		// get a handle to the stage
	    Stage stage = (Stage) btn_retour.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	
	/**
	 * {@code Affiche une alerte de fin de partie avec le gagnant et son score. Ferme ensuite la fen�tre}
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
