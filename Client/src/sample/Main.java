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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Cloud File Server");

        File file = new File(System.getProperty("user.dir")).getCanonicalFile();
        //System.out.println("Parent directory : " + file.getParent());
        Parameters params = getParameters();
        List<String> list = params.getRaw();
        //get the folder from cmd line args
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
