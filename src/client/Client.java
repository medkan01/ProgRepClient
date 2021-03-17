package client;

import java.net.URL;
import java.rmi.Naming;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.interfaceRMI.*;

public class Client extends Application{

    public void start(Stage primaryStage) {
        try {
            URL fxmlURL=getClass().getResource("../vue/MenuPrincipalVue.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
            Node root = fxmlLoader.load();
            Scene scene = new Scene((VBox) root, 720, 480);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Accueil");
            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String hote = args[0], port = args[1];
            // Initialisation de la connexion pour empêcher le lancement du client si le serveur n'est pas lancé.
            InterfacePendu pendu = (InterfacePendu) Naming.lookup("rmi://" + hote + ":" + port + "/Pendu");
            InterfaceAllumettes allumettes = (InterfaceAllumettes) Naming.lookup("rmi://" + hote + ":" + port + "/Allumettes");
            InterfaceTicTacToe ticTacToe = (InterfaceTicTacToe) Naming.lookup("rmi://" + hote + ":" + port + "/TicTacToe");
            launch(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
