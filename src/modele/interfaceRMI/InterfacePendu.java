package modele.interfaceRMI;

import java.rmi.*;

public interface InterfacePendu extends Remote {

    /**
     * Fonction générant un mot aléatoire parmis ceux disponibles dans le dictionnaire.
     * @return String : Chaine de caractère aléatoire du dictionnaire.
     * @throws RemoteException
     */
    public String motAleatoire() throws RemoteException;

    /**
     * Génère un nombre aléatoire entre 0 et max.
     * @param max est la borne supérieur positive du nombre aléatoire généré.
     * @return Int : Nombre aléatoire.
     * @throws RemoteException
     */
    public int randInt(int max) throws RemoteException;

    /**
     * Fonction pour le jeu du Pendu. Elle va permettre de voir quelles sont les lettres qui ont été trouvées ou non à partir
     * d'une table de caractère. Pour toutes les lettres qui n'ont pas encore été trouvées, le mot contiendra
     * un '_' à la place de la lettre jusqu'à ce que celle-ci soit trouvée.
     * @param mot est la chaine de caractère qui va être changée en une chaine adaptée au jeu du pendu.
     * @param lettres est la table de caractères que le joueur a sélectionné.
     * @return String : La chaine de caractère qui va être affichée à l'écran du joueur.
     * @throws RemoteException
     */
    public String changeMot(String mot, char[] lettres) throws RemoteException;

    /**
     * Fonction qui ajoute un un caractère à la fin d'une table de caractère.
     * @param tab est la table de caractère à laquelle on va ajouter le caractère.
     * @param c est le caractère à ajouter à la table de caractère.
     * @return Char[] : La table de caractère avec le nouveau caractère.
     * @throws RemoteException
     */
    public char[] ajouterChar(char[] tab, char c) throws RemoteException;

    /**
     * Fonction permettant de savoir si un caractère est dans une table de caractère ou non.
     * @param tab est la table de caractère.
     * @param c est la caractère.
     * @return Boolean : True si la table contient le caractère en paramètre. False sinon.
     * @throws RemoteException
     */
    public boolean contientChar(char[] tab, char c) throws RemoteException;
}
