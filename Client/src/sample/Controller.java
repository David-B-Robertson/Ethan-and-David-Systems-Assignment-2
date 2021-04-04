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

/**
 * The controller is the heart of the UI element allowing for it to communicate using its FXML
 * as well as calling methods to ServerClient to create new objects
 * @param serverDir the name of the server directory
 * @param serverPath the additional path that is added to allow it to work on any system
 * @param clientPath the local client path in which files are found
 */

public class Controller {

    File serverDir = new File(System.getProperty("user.dir"));

    String parentPath = serverDir.getParent();
    String serverPath = parentPath+"/Server/src/resources";

    String clientPath = (System.getProperty("user.dir")+"/src/resources");


    //These lists are the key elements on display inside of our UI containing the filenames of both directory's
    ObservableList<String> fileListServer = FXCollections.observableArrayList();
    ObservableList<String> fileListClient = FXCollections.observableArrayList();

    @FXML private TextArea previewArea;
    @FXML private ListView listViewServer;
    @FXML private ListView listViewClient;



    /**
     * The action event method for the refresh button that will cretae a ServerClient object with the refresh command
     */
    public void refresh(ActionEvent e){
        String cmd = "refresh";

        File serverDir = new File(serverPath);
        //this will populate an observableList which will then need to be put in tableView with a cellValueFactory
        ServerClient refreshclient = new ServerClient(cmd, serverDir);
    }

        /**
     * The action event method for the download button that will cretae a ServerClient object with the "download" command that correctly contains
     * the proper directory
     * @throws IOException
     */
    public void download(ActionEvent e) throws IOException {
        String cmd = "download";

        File serverDir = new File(serverPath);

        String clientPath = (System.getProperty("user.dir")+"/src/resources");

        File source = new File(serverPath);
        File destination = new File(clientPath);

        //download selector is called to achieve the action of selecting the correct file within the listview for the upcoming download
        downloadSelector(source, destination);

        ServerClient refreshclient = new ServerClient(cmd, serverDir);
    }

           /**
     * The action event method for the upload button that will cretae a ServerClient object with the "upload" command that correctly contains
     * the proper directory's
     * @throws IOException
     */
    public void upload(ActionEvent e) throws IOException {
        String cmd = "upload";
        File serverDir = new File(serverPath);

        String clientPath = (System.getProperty("user.dir")+"/src/resources");

        File destination = new File(serverPath);
        File source = new File(clientPath);

        //upload selector is called to achieve the action of selecting the correct file within the listview for the upcoming upload
        uploadSelector(source, destination);

        ServerClient refreshclient = new ServerClient(cmd, serverDir);
    }

               /**
     * The action event method for the preview button that will dispaly the currently selected local file within the UI inside of the textarea
     * this button requries your to previously select a local file before previewing it
     * @throws IOException
     */
    public void preview(ActionEvent e) throws IOException {

        //clears the current area to allow for another file preview
        previewArea.clear();
        String previewDisplay = "";

        //these index's allow for us to discover which file from the clients file view is to be previewed
        int sClientFileIndex = listViewClient.getSelectionModel().getSelectedIndex();

        //when an index is = to -1 this notifies us that there is currently no file selected
        if(sClientFileIndex!=-1){
            String sClientFileName = fileListClient.get(sClientFileIndex);

            List<String> lines = new ArrayList<String>();
            String line;
            try {
                //reads the currently selected file line by line and saves it to a string that will be populated within the text area
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
            //if no file is selected and the preview is attempted than an message is displayed in console
        } else if(sClientFileIndex==-1){

            System.out.println("Please select a file in the table");
        }
    }

       /**
     * The upload selector is called prior to the upload to ensure that the selected file is currently availible to be uploaded
     * as this is based on which selection is current before upload button is pressed
     * @param source the directory of the currently selected local file
     * @param destination the file directory that the local file will be uploaded to 
     * @throws IOException
     */
    public void uploadSelector(File source, File destination) throws IOException {

        int sFileIndex = listViewClient.getSelectionModel().getSelectedIndex();
        //prints message if no file is selected
        if(sFileIndex==-1){
            System.out.println("Please select a file in the list view");
        } else {
            String sFileName = fileListClient.get(sFileIndex);
            //calls the upload to server button to stream to the socket
            uploadToServer(source, destination, sFileName);
        }
    }
       /**
     * The upload is streamed from the correct source and destination from the selected file using the input stream of the to be created socket its 
     * out and input streams are set equal to the correct paths of the file's upload
     * @param source the directory of the currently selected local file
     * @param destination the file directory that the local file will be uploaded to 
     * @param filename the name of the file that was taken from selection
     * @throws IOException
     */
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

       /**
     * The download selector is called prior to the download to ensure that the selected file is currently availible to be downloaded from the server
     * as this is based on which selection is current before download button is pressed.  
     * @param source the directory of the currently selected server file
     * @param destination the file directory that the server file will be downloaded to i.e. the local directory
     * @throws IOException
     */
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
       /**
   * The download is streamed from the correct source and destination from the selected file using the input stream of the to be created socket its 
     * out and input streams are set equal to the correct paths of the file's upload. Reading the contents of the file and transmitting them into the
     * correct inputstream
     * @param source the directory of the currently selected local file
     * @param destination the file directory that the local file will be uploaded to 
     * @param filename the name of the file that was taken from selection
     * @throws IOException
     */
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
       /**
     * Initializes a serverclient and finds the local directory and server directory that is to be displayed and updated consistently within the 
     * FXML listview
     */
    @FXML
    public void initialize(){

        fileListServer = ServerClient.getFileListServer();
        fileListClient = ServerClient.getFileListClient();

        //previewArea.setText("");

        listViewServer.setItems(fileListServer);
        listViewClient.setItems(fileListClient);
    }
}
