package modele.interfaceRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

public interface InterfaceAllumettes extends Remote {
	
	
	/**
	 * Creer une nouvelle partie si c'est en mode solo. Sinon il cherche si une partie duo manque un joueur. Si aucune partie duo n'existe ou que toutes les parties duo possèdent dejà 2 joueurs, creer une nouvelle partie duo.
	 * @param mode Le mode de jeu de la partie (solo / duo)
	 * @return L'id de la partie creee / selectionnee
	 * @throws RemoteException
	 */
	public UUID creerPartie(String mode) throws RemoteException;
	
	
	/**
	 * Initialise les donnees de jeu necessaires pour debuter une partie
	 * @param uuid L'id de la partie ciblee
	 * @param mode Le mode de jeu de la partie
	 * @param nomJoueur Le nom du joueur affiche dans la partie
	 * @throws RemoteException
	 */
	public void initialise(UUID uuid, String mode, String nomJoueur) throws RemoteException;
	
	
	/**
	 * Actualise les donnees de jeu de la partie ciblee en fonction du joueur qui joue et du nombre d'allumettes selectionnees
	 * @param uuid L'id de la partie ciblee
	 * @param nbAllChoisies Le nombre d'allumettes que le joueur a decide de retirer
	 * @throws RemoteException
	 */
	public void action(UUID uuid, int nbAllChoisies) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nombre maximum d'allumettes selectionnables
	 * @throws RemoteException
	 */
	public int maxAllumettes(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nom du joueur gagnant
	 * @throws RemoteException
	 */
	public String nomGagnant(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le score du joueur gagnant
	 * @throws RemoteException
	 */
	public int scoreGagnant(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nom du joueur devant jouer pour ce tour
	 * @throws RemoteException
	 */
	public String nomJoueurTour(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le tableau des scores de la partie
	 * @throws RemoteException
	 */
	public int[] getTabScore(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nombre d'allumettes encore en jeu
	 * @throws RemoteException
	 */
	public int getNbAllumettes(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le numero du tour actuel de la partie
	 * @throws RemoteException
	 */
	public int getTour(UUID uuid) throws RemoteException;
	
	
	/**
	 * Ajoute un joueur dans la partie ciblee avec son nom
	 * @param uuid L'id de la partie ciblee
	 * @param nomJoueur Le nom du joueur affiche dans la partie
	 * @throws RemoteException
	 */
	public void rejoindrePartie(UUID uuid, String nomJoueur) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nombre de joueurs dans la partie
	 * @throws RemoteException
	 */
	public int getNbJoueurs(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le mode de la partie (solo / duo)
	 * @throws RemoteException
	 */
	public String getMode(UUID uuid) throws RemoteException;
	
	
	/**
	 * Retire la partie ciblee de la liste des parties s'il n'y a plus de joueurs dans la partie
	 * @param uuid L'id de la partie ciblee
	 * @throws RemoteException
	 */
	public void finPartie(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nom du joueur 1
	 * @throws RemoteException
	 */
	public String getNJ1(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nom du joueur 2
	 * @throws RemoteException
	 */
	public String getNJ2(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Une liste des id des allumettes retirees lors du tour
	 * @throws RemoteException
	 */
	public ArrayList<String> getAllRetiree(UUID uuid) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @param tab Une liste des id des allumettes retirees lors du tour
	 * @throws RemoteException
	 */
	public void setAllRetiree(UUID uuid, ArrayList<String> tab) throws RemoteException;
	
	
	/**
	 * @param uuid L'id de la partie ciblee
	 * @return Le nombre d'allumettes selectionnees par le serveur
	 * @throws RemoteException
	 */
	public int coupIA(UUID uuid) throws RemoteException;
	
	/**
	 * @return Une hashtable<IdPartie, nomDuJoueur>, liste des parties qui n'ont qu'un joueur
	 * @throws RemoteException
	 */
	public Hashtable<UUID, String> getJoueursParties() throws RemoteException;
}
