package controleur;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfacePendu;

import java.rmi.Naming;
import java.rmi.RemoteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PenduControleur {

    @FXML private Label motAleatoire, lblChance;
    @FXML private Button btnQuitter;
    @FXML private Button btnA, btnB, btnC, btnD, 
        btnE, btnF, btnG, btnH, btnI, btnJ, btnK, btnL, btnM, btnN, btnO, 
        btnP, btnQ, btnR, btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ; 
    private String mot;
    private char[] lettres = new char[26];
    private int nbChance;
    private String hote;
    private String port;
    private InterfacePendu pendu;

    public void initialise() {
        try{
            this.nbChance = 15;
            this.lblChance.setText("Chances restantes : " + nbChance);
            pendu = (InterfacePendu) Naming.lookup("rmi://" + hote + ":" + port + "/Pendu");
            this.mot = pendu.motAleatoire();
            this.motAleatoire.setText(pendu.changeMot(this.mot, this.lettres));
            this.motAleatoire.setTextFill(Color.BLACK);
            disableLettres(false);
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
                btnA.setDisable(true);
                break;
            case "btnB":
                lettre = 'b';
                btnB.setDisable(true);
                break;
            case "btnC":
                lettre = 'c';
                btnC.setDisable(true);
                break;
            case "btnD":
                lettre = 'd';
                btnD.setDisable(true);
                break;
            case "btnE":
                lettre = 'e';
                btnE.setDisable(true);
                break;
            case "btnF":
                lettre = 'f';
                btnF.setDisable(true);
                break;
            case "btnG":
                lettre = 'g';
                btnG.setDisable(true);
                break;
            case "btnH":
                lettre = 'h';
                btnH.setDisable(true);
                break;
            case "btnI":
                lettre = 'i';
                btnI.setDisable(true);
                break;
            case "btnJ":
                lettre = 'j';
                btnJ.setDisable(true);
                break;
            case "btnK":
                lettre = 'k';
                btnK.setDisable(true);
                break;
            case "btnL":
                lettre = 'l';
                btnL.setDisable(true);
                break;
            case "btnM":
                lettre = 'm';
                btnM.setDisable(true);
                break;
            case "btnN":
                lettre = 'n';
                btnN.setDisable(true);
                break;
            case "btnO":
                lettre = 'o';
                btnO.setDisable(true);
                break;
            case "btnP":
                lettre = 'p';
                btnP.setDisable(true);
                break;
            case "btnQ":
                lettre = 'q';
                btnQ.setDisable(true);
                break;
            case "btnR":
                lettre = 'r';
                btnR.setDisable(true);
                break;
            case "btnS":
                lettre = 's';
                btnS.setDisable(true);
                break;
            case "btnT":
                lettre = 't';
                btnT.setDisable(true);
                break;
            case "btnU":
                lettre = 'u';
                btnU.setDisable(true);
                break;
            case "btnV":
                lettre = 'v';
                btnV.setDisable(true);
                break;
            case "btnW":
                lettre = 'w';
                btnW.setDisable(true);
                break;
            case "btnX":
                lettre = 'x';
                btnX.setDisable(true);
                break;
            case "btnY":
                lettre = 'y';
                btnY.setDisable(true);
                break;
            case "btnZ":
                lettre = 'z';
                btnZ.setDisable(true);
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
                this.motAleatoire.setText("Perdu! Le mot Ã©tait " + this.mot);
                this.motAleatoire.setTextFill(Color.RED);
                disableLettres(true);
            } else if(!this.motAleatoire.getText().contains("_")){
                this.motAleatoire.setText("GagnÃ©! Le mot Ã©tait " + this.mot);
                this.motAleatoire.setTextFill(Color.GREEN);
                disableLettres(true);
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
        disableLettres(false);
    }

    public void quitterPendu() {
        Stage fenetre = (Stage) btnQuitter.getScene().getWindow();
        fenetre.close();
    }

    public void disableLettres(boolean arg){
        btnA.setDisable(arg);
        btnB.setDisable(arg);
        btnC.setDisable(arg);
        btnD.setDisable(arg);
        btnE.setDisable(arg);
        btnF.setDisable(arg);
        btnG.setDisable(arg);
        btnH.setDisable(arg);
        btnI.setDisable(arg);
        btnJ.setDisable(arg);
        btnK.setDisable(arg);
        btnL.setDisable(arg);
        btnM.setDisable(arg);
        btnN.setDisable(arg);
        btnO.setDisable(arg);
        btnP.setDisable(arg);
        btnQ.setDisable(arg);
        btnR.setDisable(arg);
        btnS.setDisable(arg);
        btnT.setDisable(arg);
        btnU.setDisable(arg);
        btnV.setDisable(arg);
        btnW.setDisable(arg);
        btnX.setDisable(arg);
        btnY.setDisable(arg);
        btnZ.setDisable(arg);
    }

    public void setParametres(String hote, String port) {
    	this.hote = hote;
    	this.port = port;
    }
}
