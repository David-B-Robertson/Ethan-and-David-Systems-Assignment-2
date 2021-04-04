package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import static java.nio.file.StandardCopyOption.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    //String serverPath = "/Users/davidrobertson/Desktop/SERVERASSG/Server/src/resources";

    File serverDir = new File(System.getProperty("user.dir"));

    String parentPath = serverDir.getParent();
    String serverPath = parentPath+"/Server/src/resources";

    String clientPath = (System.getProperty("user.dir")+"/src/resources");



    ObservableList<String> fileListServer = FXCollections.observableArrayList();
    ObservableList<String> fileListClient = FXCollections.observableArrayList();

    @FXML private TextArea previewArea;
    @FXML private ListView listViewServer;
    @FXML private ListView listViewClient;


    public void refresh(ActionEvent e){
        String cmd = "refresh";

        //String serverPath = "/Users/davidrobertson/Desktop/SERVERASSG/Server/src/resources";
        File serverDir = new File(serverPath);

        //this will populate an observableList which will then need to be put in tableView with a cellValueFactory
        ServerClient refreshclient = new ServerClient(cmd, serverDir);
    }

    public void download(ActionEvent e) throws IOException {
        String cmd = "download";

        //String serverPath = "/Users/davidrobertson/Desktop/SERVERASSG/Server/src/resources";
        File serverDir = new File(serverPath);

        String clientPath = (System.getProperty("user.dir")+"/src/resources");

        File source = new File(serverPath);
        File destination = new File(clientPath);

        downloadSelector(source, destination);

        ServerClient refreshclient = new ServerClient(cmd, serverDir);
    }


    public void upload(ActionEvent e) throws IOException {
        String cmd = "upload";

        //String serverPath = "/Users/davidrobertson/Desktop/SERVERASSG/Server/src/resources";
        File serverDir = new File(serverPath);

        String clientPath = (System.getProperty("user.dir")+"/src/resources");

        File destination = new File(serverPath);
        File source = new File(clientPath);

        uploadSelector(source, destination);

        ServerClient refreshclient = new ServerClient(cmd, serverDir);
    }

    public void preview(ActionEvent e) throws IOException {
        previewArea.clear();
        String previewDisplay = "";

        int sClientFileIndex = listViewClient.getSelectionModel().getSelectedIndex();
        int sServerFileIndex = listViewClient.getSelectionModel().getSelectedIndex();

        if(sClientFileIndex!=-1){
            String sClientFileName = fileListClient.get(sClientFileIndex);

            List<String> lines = new ArrayList<String>();
            String line;
            try {
                BufferedReader br = new BufferedReader(new FileReader(clientPath+"/"+sClientFileName));

                while ((line = br.readLine()) != null){
                    lines.add(line);
                    lines.add("\n");
                }
                br.close();

                for(int i = 0; i<lines.size(); i++){
                    previewArea.appendText(lines.get(i));
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

        } else if(sClientFileIndex==-1 && sServerFileIndex==-1){

            System.out.println("Please select a file in the table");
        }
    }


    public void uploadSelector(File source, File destination) throws IOException {

        int sFileIndex = listViewClient.getSelectionModel().getSelectedIndex();

        if(sFileIndex==-1){
            System.out.println("Please select a file in the list view");
        } else {
            String sFileName = fileListClient.get(sFileIndex);
            uploadToServer(source, destination, sFileName);
        }
    }

    public void uploadToServer(File source, File destination, String filename) throws IOException{
        InputStream is = null;
        OutputStream os = null;

        File sourcePath = new File(source.getPath()+"/"+filename);
        File destPath = new File(destination.getPath()+"/"+filename);

        try {
            is = new FileInputStream(sourcePath);
            os = new FileOutputStream(destPath);

            byte[] buf = new byte[1024];

            int byteCount;
            while ((byteCount = is.read(buf)) > 0){
                os.write(buf, 0, byteCount);
            }
        } finally {
            is.close();
            os.close();
        }
    }


    public void downloadSelector(File source, File destination) throws IOException {
        System.out.println("selecting");

        int sFileIndex = listViewServer.getSelectionModel().getSelectedIndex();

        if(sFileIndex==-1){
            System.out.println("Please select a file in the list view");
        } else {
            String sFileName = fileListServer.get(sFileIndex);
            System.out.println(sFileName);

            downloadFromServer(source, destination, sFileName);
        }
    }

    public void downloadFromServer(File source, File destination, String filename)throws IOException{
        InputStream is = null;
        OutputStream os = null;

        File sourcePath = new File(source.getPath()+"/"+filename);
        File destPath = new File(destination.getPath()+"/"+filename);



        try {
            is = new FileInputStream(sourcePath);
            os = new FileOutputStream(destPath);

            byte[] buf = new byte[1024];

            int byteCount;
            while ((byteCount = is.read(buf)) > 0){
                os.write(buf, 0, byteCount);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    @FXML
    public void initialize(){

        fileListServer = ServerClient.getFileListServer();
        fileListClient = ServerClient.getFileListClient();

        //previewArea.setText("");

        listViewServer.setItems(fileListServer);
        listViewClient.setItems(fileListClient);
    }
}
