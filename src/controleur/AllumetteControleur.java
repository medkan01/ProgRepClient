package controleur;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllumetteControleur {
	
	@FXML private Button btn_valider;
	@FXML private Button btn_retour;
	
	@FXML private VBox parent;
	@FXML private HBox boxAllumettes;
	
	@FXML private Label lbl_scoreJ1;
	@FXML private Label lbl_j1;
	@FXML private Label lbl_scoreJ2;
	@FXML private Label lbl_j2;
	@FXML private Label lbl_tour;
	
	
	private InterfaceAllumettes iAllumettes;
	
	private UUID idPartie;
	
	private int nbAllChoisies = 0;
	private int joueur;
	
	private ArrayList<String> tabAllRetirerStr = new ArrayList<String>();
	private ArrayList<Node> tabAllRetirer = new ArrayList<Node>();
	
	private Thread tVictoire;
	private Thread tAttJoueurs = threadAttenteJoueurs();
	private CountDownLatch readyThreadCounter;
	
	/**
	 * Enregistre l'id de la partie, le numero du joueur associe a la fenetre. Si la partie est mode duo, attend que 2 joueurs soient connectes. Initialise l'affichage et debute un tour de jeu
	 * @param uuid L'id de la partie ciblee
	 * @param joueur Le numero de place que le joueur occupera pour cette partie
	 */
	public void initialisation(UUID uuid, int joueur) {
		try {
			this.idPartie = uuid;
			this.joueur = joueur;
			
			if (iAllumettes.getMode(uuid).equals("duo")) {
				tAttJoueurs.start();
			}
			else {
				lbl_j1.setText(iAllumettes.getNJ1(uuid));
				lbl_j2.setText(iAllumettes.getNJ2(uuid));
				
				afficheAllumettes(iAllumettes.getNbAllumettes(uuid));
				
				affPremierJoueur(iAllumettes.nomJoueurTour(uuid));
				
				tVictoire = threadSoloVictoire();
				tVictoire.start();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return Un thread qui attend que 2 joueurs soient connectes pour lancer la suite de la partie (affichage du nom des joueurs / allumettes, appel de {@link #threadMultiVictoire()} )
	 */
	private Thread threadAttenteJoueurs() {
		return new Thread(() -> {
			try {
				parent.setDisable(true);
				
				while (iAllumettes.getNbJoueurs(idPartie) != 2)
					Thread.sleep(500);
				
				Platform.runLater(() -> {
					try {
						lbl_j1.setText(iAllumettes.getNJ1(idPartie));
						lbl_j2.setText(iAllumettes.getNJ2(idPartie));
						
						afficheAllumettes(iAllumettes.getNbAllumettes(idPartie));
						
						affPremierJoueur(iAllumettes.nomJoueurTour(idPartie));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				});
				
				tVictoire = threadMultiVictoire();
				tVictoire.start();
				
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
	
	
	/**
	 * @return Un thread qui, tant que les conditions de fin de partie ne sont pas verifiees pour le mode <i>Solo</i>, relance un tour de jeu ({@link #tour()})
	 */
	private Thread threadSoloVictoire() {
		return new Thread(() -> {
			try {
				while (iAllumettes.getNbAllumettes(idPartie) != 0) {
					readyThreadCounter = new CountDownLatch(1);
					tour();
					readyThreadCounter.await();
				}
				
				Platform.runLater(() -> {
					try {
						finPartie(iAllumettes.nomGagnant(idPartie), iAllumettes.scoreGagnant(idPartie));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				});
				
			} catch (RemoteException | InterruptedException e) {
				Platform.runLater(() -> {
					try {
						finPartie(iAllumettes.nomGagnant(idPartie), iAllumettes.scoreGagnant(idPartie));
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				});
			}
		});
	}
	
	
	/**
	 * @return Un thread qui, tant que les conditions de fin de partie ne sont pas verifiees pour le mode <i>Duo</i>, relance un tour de jeu ({@link #tour()})
	 */
	private Thread threadMultiVictoire() {
		return new Thread(() -> {
			try {
				while (iAllumettes.getNbJoueurs(idPartie) == 2 && iAllumettes.getNbAllumettes(idPartie) != 0) {
					readyThreadCounter = new CountDownLatch(1);
					tour();
					readyThreadCounter.await();
				}
				
				Platform.runLater(() -> {
					try {
						if (iAllumettes.getNbJoueurs(idPartie) != 2) {
							String nomGagnant= null;
							int i;
							if (joueur == iAllumettes.getTour(idPartie)%2) {
								nomGagnant = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie)) ? lbl_j2.getText().trim() : lbl_j1.getText().trim());
								i = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie))) ? 1 : 0;
							}
							else {
								nomGagnant = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie)) ? lbl_j1.getText().trim() : lbl_j2.getText().trim());
								i = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie))) ? 0 : 1;
							}
							finPartieAbandon(nomGagnant, iAllumettes.getTabScore(idPartie)[i]);
						}
						else
							finPartie(iAllumettes.nomGagnant(idPartie), iAllumettes.scoreGagnant(idPartie));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				});
			} catch (InterruptedException | RemoteException e) {
				Platform.runLater(() -> {
					try {
						if (iAllumettes.getNbJoueurs(idPartie) != 2) {
							String nomGagnant= null;
							int i;
							if (joueur == iAllumettes.getTour(idPartie)%2) {
								nomGagnant = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie)) ? lbl_j2.getText().trim() : lbl_j1.getText().trim());
								i = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie))) ? 1 : 0;
							}
							else {
								nomGagnant = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie)) ? lbl_j1.getText().trim() : lbl_j2.getText().trim());
								i = (lbl_j1.getText().trim().equals(iAllumettes.nomJoueurTour(idPartie))) ? 0 : 1;
							}
							finPartieAbandon(nomGagnant, iAllumettes.getTabScore(idPartie)[i]);
						}
							
						else
							finPartie(iAllumettes.nomGagnant(idPartie), iAllumettes.scoreGagnant(idPartie));
							
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				});
			}
		});
	}
	
	/**
	 * Execute un tour de jeu
	 */
	private void tour() {
		new Thread(() -> {
			try {
				Platform.runLater(() -> {
					try {
						affTourJoueur(iAllumettes.nomJoueurTour(this.idPartie));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				});
				
				//action du serveur
				if ( iAllumettes.getTour(idPartie)%2 == 1 && iAllumettes.getMode(idPartie).equals("solo")) {
					//Action du serveur
					parent.setDisable(true);
				
					Thread.sleep(800);

					this.nbAllChoisies = iAllumettes.coupIA(this.idPartie);
					
					//Selection des allumettes disponibles par le serveur
					tourIA(this.nbAllChoisies);
					
					//Comme nous sommes dans un nouveau thread, il nous faut utiliser runLater pour executer une fonction qui est dans l'autre thread
					Platform.runLater(() -> {
						valider();
					});
					
					parent.setDisable(false);
				}
				else if (iAllumettes.getMode(idPartie).equals("duo")) {
					parent.setDisable(true);
					
					//Attendre que l'adversaire ait joue
					try {
						while (this.joueur != iAllumettes.getTour(idPartie)%2) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
						//On efface les allumettes selectionnees par le joueur precedent
						for (Node node : boxAllumettes.getChildren().filtered(t->t.isVisible())) {
							if (iAllumettes.getAllRetiree(idPartie).contains(node.getId()))
								node.setVisible(false);
						}
						
						//Comme nous sommes dans un nouveau thread, il nous faut utiliser runLater pour executer une fonction qui est dans l'autre thread
						Platform.runLater(() -> {
							//Actualisation du tableau des scores
							try {
								affTabScore(iAllumettes.getTabScore(idPartie));
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						});
						
						parent.setDisable(false);
						
					} catch (NullPointerException npe) {
						Platform.runLater(() -> {
							Stage stage = (Stage) btn_retour.getScene().getWindow();
							stage.close();
							Thread.currentThread().interrupt();
						});
					}
				}
			} catch (RemoteException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public void setInterfaceAllumettes(InterfaceAllumettes interfaceAllumettes) {
		this.iAllumettes = interfaceAllumettes;
	}

	
	/**
	 * Creer nbAllumette et les affiche dans le panel boxAllumettes 
	 * @param nbAllumette Le nombre d'allumettes desirees
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
			
			boxAllumettes.getChildren().add(button);
		}
		
		btn_valider.setDisable(true);
	}
	
	
	/**
	 * Affiche une alerte du premier jouer a devoir jouer
	 * @param nomPJoueur Le nom du joueur
	 */
	private void affPremierJoueur(String nomPJoueur) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Premier joueur");
		alert.setContentText("Le premier joueur e jouer est : " + nomPJoueur);
		alert.showAndWait();
	}
	
	
	/**
	 * Selectionne les <b>nbAlluChoisies</b> premieres allumettes disponibles dans la liste des allumettes visibles
	 * @param nbAlluChoisies Le nombre d'allumettes choisies
	 */
	private void tourIA(int nbAlluChoisies) {
		//Liste de toutes les allumettes visibles
		ObservableList<Node> alluVisibles = boxAllumettes.getChildren().filtered(t->t.isVisible());
		
		for (int i=0; i<nbAlluChoisies; i++)
			tabAllRetirer.add(alluVisibles.get(i));
	}
	
	
	/**
	 * Affiche le tableau des scores dans les labels <b>lbl_scoreJ1</b> et <b>lbl_scoreJ1</b>
	 * @param tabScore Le tableau des scores
	 */
	private void affTabScore(int[] tabScore) {
		lbl_scoreJ1.setText(String.valueOf(tabScore[0]));
		lbl_scoreJ2.setText(String.valueOf(tabScore[1]));
	}

	
	/**
	 * Affiche le nom du joueur qui doit jouer dans le label <b>lbl_tour</b>
	 * @param nomJoueur Le nom du joueur
	 */
	private void affTourJoueur(String nomJoueur) {
		lbl_tour.setText(nomJoueur);
	}
	
	
	/**
	 * Selection + enregistrement ou deselection + annulation de l'enregistrement de l'allumette selectionnee. L'allumette enregistree sera effacee lors de la validation de l'action
	 * @param event ActionEvent event
	 */
	@FXML
	private void choixAllumette(ActionEvent event) {
		//On recupere la Node sur laquelle on a clique
		final Node source = (Node) event.getSource();
	
		if (source.getStyle().equals("") && nbAllChoisies != 2) {
			tabAllRetirer.add(source);
			nbAllChoisies += 1;
			source.setStyle("-fx-background-color: red;");
		}
		else if (!source.getStyle().equals("")) {
			tabAllRetirer.remove(source);
			nbAllChoisies -= 1;
			source.setStyle("");
		}
		
		//Si aucune allumette n'est selectionnee, alors le bouton Valider n'est pas actif
		if ( nbAllChoisies == 0 )
			btn_valider.setDisable(true);
		else
			btn_valider.setDisable(false);
	}
	
	
	/**
	 * Affiche une alerte d'aide
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
	 * Appelee lors du clic sur le bouton <i>btn_valider</i>,
	 * appelle {@link modele.interfaceRMI.InterfaceAllumettes#action(UUID, int)}
	 * actualise le <b>tableau des scores</b>, retire les allumettes selectionnees de l'affichage,
	 * enregistre les allumettes selectionnees sur le serveur pour le prochain joueur
	 */
	@FXML
	private void valider() {
		try {
			//Enregistrement des données de jeu sur le serveur
			iAllumettes.action(this.idPartie, this.nbAllChoisies);
			
			//Actualisation du tableau des scores
			affTabScore(iAllumettes.getTabScore(idPartie));
			
			//On retire les allumettes selectionnees de l'affichage
			for (Node node : tabAllRetirer) {
				tabAllRetirerStr.add(node.getId());
				node.setVisible(false);
			}
			tabAllRetirer.clear();
			
			//Enregistrement des allumettes selectionnees sur le serveur
			iAllumettes.setAllRetiree(idPartie, tabAllRetirerStr);
			
			nbAllChoisies = 0;
			
			readyThreadCounter.countDown();
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * appelle {@link modele.interfaceRMI.InterfaceAllumettes#finPartie(UUID uuid)} puis ferme la fenetre de jeu
	 */
	@FXML
	public void retour() {
		try {
			iAllumettes.finPartie(idPartie);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	    Stage stage = (Stage) btn_retour.getScene().getWindow();
	    stage.close();
	}
	
	
	/**
	 * Affiche une alerte de fin de partie avec le gagnant et son score. Appelle ensuite {@link #retour()} 
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
	
	
	/**
	 * Affiche une alerte de fin de partie suite a l'abandon / disparition de son adversaire. Appelle ensuite {@link #retour()} 
	 * @param nomGagnant Le nom du joueur gagnant
	 * @param scoreGagnant Le score du joueur gagnant
	 */
	private void finPartieAbandon(String nomGagnant, int scoreGagnant) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Fin de partie");
		alert.setContentText("Votre adversaire a ete deconnecte ou a abandonne ! Le gagnant est donc " + nomGagnant + " avec un score de " + scoreGagnant );
		
		alert.showAndWait();
		retour();
	}
}
