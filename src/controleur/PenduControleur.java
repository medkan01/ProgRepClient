package controleur;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfacePendu;

import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PenduControleur implements Initializable {

    @FXML private Label motAleatoire, lblChance;
    @FXML private Button btnQuitter;
    private String mot;
    private char[] lettres = new char[26];
    private int nbChance;
    private String hote = "localhost";
    private int port = 3000;
    private InterfacePendu pendu;

    public void initialize(URL arg0, ResourceBundle arg1) {
        try{
            this.nbChance = 15;
            this.lblChance.setText("Chances restantes : " + nbChance);
            pendu = (InterfacePendu) Naming.lookup("rmi://" + hote + ":" + port + "/Pendu");
            this.mot = pendu.motAleatoire();
            this.motAleatoire.setText(pendu.changeMot(this.mot, this.lettres));
            this.motAleatoire.setTextFill(Color.BLACK);
            System.out.println("Chargement de la page..");
            
        } catch (Exception e) {
            System.out.println(e);
        }
	}

    public void actualiseMot(ActionEvent event) throws RemoteException {
        String source = ((Button) event.getSource()).getId();
        char lettre = '0';
        switch(source) {
            case "btnA":
                lettre = 'a';
                break;
            case "btnB":
                lettre = 'b';
                break;
            case "btnC":
                lettre = 'c';
                break;
            case "btnD":
                lettre = 'd';
                break;
            case "btnE":
                lettre = 'e';
                break;
            case "btnF":
                lettre = 'f';
                break;
            case "btnG":
                lettre = 'g';
                break;
            case "btnH":
                lettre = 'h';
                break;
            case "btnI":
                lettre = 'i';
                break;
            case "btnJ":
                lettre = 'j';
                break;
            case "btnK":
                lettre = 'k';
                break;
            case "btnL":
                lettre = 'l';
                break;
            case "btnM":
                lettre = 'm';
                break;
            case "btnN":
                lettre = 'n';
                break;
            case "btnO":
                lettre = 'o';
                break;
            case "btnP":
                lettre = 'p';
                break;
            case "btnQ":
                lettre = 'q';
                break;
            case "btnR":
                lettre = 'r';
                break;
            case "btnS":
                lettre = 's';
                break;
            case "btnT":
                lettre = 't';
                break;
            case "btnU":
                lettre = 'u';
                break;
            case "btnV":
                lettre = 'v';
                break;
            case "btnW":
                lettre = 'w';
                break;
            case "btnX":
                lettre = 'x';
                break;
            case "btnY":
                lettre = 'y';
                break;
            case "btnZ":
                lettre = 'z';
                break;
            default:
                lettre = '0';
                break;
        }
        if(lettre != '0'){
            if(!pendu.contientChar(lettres, lettre)){
                this.lettres = pendu.ajouterChar(this.lettres, lettre);
                if(!pendu.contientChar(mot.toCharArray(), lettre)){
                    nbChance--;
                    this.lblChance.setText("Chances restantes : " + nbChance);
                }
                this.motAleatoire.setText(pendu.changeMot(mot, lettres));
            }
            if(nbChance == 0){
                this.motAleatoire.setText("Perdu! Le mot était " + this.mot);
                this.motAleatoire.setTextFill(Color.RED);
            }
            if(!this.motAleatoire.getText().contains("_")){
                this.motAleatoire.setText("Gagné! Le mot était " + this.mot);
                this.motAleatoire.setTextFill(Color.GREEN);
            }
        }
    }

    public void recommencerPartie() throws RemoteException {
        this.mot = pendu.motAleatoire();
        this.nbChance = 15;
        this.lettres = new char[26];
        this.motAleatoire.setText(pendu.changeMot(this.mot, this.lettres));
        this.motAleatoire.setTextFill(Color.BLACK);
        this.lblChance.setText("Chances restantes : " + this.nbChance);
    }

    public void quitterPendu() {
        Stage fenetre = (Stage) btnQuitter.getScene().getWindow();
        fenetre.close();
    }
}
