package modele.interfaceRMI;

import java.rmi.*;

public interface InterfaceTicTacToe extends Remote{
    
    /**
     * Initialise la grille de coordonnées d'une partie.
     * @param size - Définit la taille de la grille.
     * @return String[][] de la grille de coordonnées.
     * @throws Exception Contraintes pour l'argument size.
     */
    public String[][] initialiserCoord(int size) throws Exception;

    /**
     * Permet de remplir une case pour à des coordonnées et pour un joueur donné.
     * @param x - Coordonnée x.
     * @param y - Coordonnée y.
     * @param joueur - Determine le symbole à insérer dans la case.
     * @param coord - Grille de jeu.
     * @return String[][] de la grille mise à jour
     * @throws Exception Contraintes des arguments.
     */
    public String[][] remplirCase(int x, int y, int joueur, String[][] coord) throws Exception;

    public boolean verifierLignes(int x, String[][] coord, String symbole);

    public boolean verifierCase(int x, int y, String[][] coord);

    public boolean verifierColones(int y, String[][] coord, String symbole);

    public boolean verifierDiagonale1(String[][] coord, String symbole);

    public boolean verifierDiagonale2(String[][] coord, String symbole);

    public boolean verifierDiagonales(String[][] coord, String symbole);

    public boolean verifierTour(int x, int y, String[][] coord, String symbole);

    public boolean grilleRemplie(String[][] coord);
}
