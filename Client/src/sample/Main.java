package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *WRITTEN BY:
 * Ethan Randle-Bragg (100742591)
 * David Robertson (100751769)
 */

/**
 * User will open the Client UI after the server has been launched and will establish a connection with the Server
 * The User will then have the ability to freely upload download and preview files
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Loades the FXML file for Javafx
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Cloud File Server");

        //gets the User dir to access the local files
        File file = new File(System.getProperty("user.dir")).getCanonicalFile();
        Parameters params = getParameters();
        List<String> list = params.getRaw();
        //get the folder from cmd line args allowing the path to be the local file
        String folder = list.get(1);
        String path = file.getParent()+"/Server/"+folder;

        File serverDir = new File(path);

        //root is fxml
        primaryStage.setScene(new Scene(root, 890, 475 , Color.BLACK));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
