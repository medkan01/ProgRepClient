package controleur;

import java.net.URL;
import java.rmi.Naming;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modele.interfaceRMI.InterfaceTicTacToe;

public class TicTacToeControleur implements Initializable {
    private int taille = 3;
    private String[][] coord = new String[taille][taille];
    private String hote = "localhost";
    private String port = "3000";
    private InterfaceTicTacToe tic;
    private String[] joueurs = new String[2];
    private int joueur;

    @FXML private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22, btnQuitter, btnRecommencer;
    @FXML private Label lblPartie;

    public void initialize(URL arg0, ResourceBundle arg1) {
        try{
            tic = (InterfaceTicTacToe) Naming.lookup("rmi://" + hote + ":" + port + "/TicTacToe");
            joueurs[0] = "O";
            joueurs[1] = "X";
            debutPartie();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void tourBtn00() {
        try {
            int x = 0;
            int y = 0;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn00.setText(coord[x][y]);
                btn00.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn01() {
        try {
            int x = 0;
            int y = 1;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn01.setText(coord[x][y]);
                btn01.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn02() {
        try {
            int x = 0;
            int y = 2;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn02.setText(coord[x][y]);
                btn02.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn10() {
        try {
            int x = 1;
            int y = 0;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn10.setText(coord[x][y]);
                btn10.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn11() {
        try {
            int x = 1;
            int y = 1;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn11.setText(coord[x][y]);
                btn11.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn12() {
        try {
            int x = 1;
            int y = 2;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn12.setText(coord[x][y]);
                btn12.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn20() {
        try {
            int x = 2;
            int y = 0;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn20.setText(coord[x][y]);
                btn20.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn21() {
        try {
            int x = 2;
            int y = 1;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn21.setText(coord[x][y]);
                btn21.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void tourBtn22() {
        try {
            int x = 2;
            int y = 2;
            if(tic.verifierCase(x, y, coord)){
                coord = tic.remplirCase(x, y, joueur, coord);
                btn22.setText(coord[x][y]);
                btn22.setDisable(true);
            }
            finDePartie(x, y);
            switch (joueur){
                case 0: 
                    joueur = 1;
                    break;
                case 1:
                    joueur = 0;
                    break;
            }
        } catch (Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void finDePartie(int x, int y){
        if(!tic.grilleRemplie(coord)){
            if(tic.verifierTour(x, y, coord, joueurs[joueur])) {
                lblPartie.setText("Le joueur " + joueur + " gagne la partie. Bravo!");
                setDisableAllButton(true);
            } else {
                lblPartie.setText("Tour du joueur " + joueur + " Symbole [" + joueurs[joueur] + "]");
            }
        } else {
            lblPartie.setText("Egalité !");
        }
    }
 
    public void debutPartie(){
        try{
            btn00.setText("");
            btn01.setText("");
            btn02.setText("");
            btn10.setText("");
            btn11.setText("");
            btn12.setText("");
            btn20.setText("");
            btn21.setText("");
            btn22.setText("");
            setDisableAllButton(false);
            coord = tic.initialiserCoord(this.taille);
            joueur = (new Random()).nextInt(2);
            lblPartie.setText("Début de la partie. Tour du joueur " + joueur + " Symbole [" + joueurs[joueur] + "]");
        } catch(Exception e) {
            lblPartie.setText(e.getMessage());
        }
    }

    public void quitterTicTacToe(){
        Stage fenetre = (Stage) btnQuitter.getScene().getWindow();
        fenetre.close();
    }

    public void setDisableAllButton(boolean arg){
        btn00.setDisable(arg);
        btn01.setDisable(arg);
        btn02.setDisable(arg);
        btn10.setDisable(arg);
        btn11.setDisable(arg);
        btn12.setDisable(arg);
        btn20.setDisable(arg);
        btn21.setDisable(arg);
        btn22.setDisable(arg);
    }
}
