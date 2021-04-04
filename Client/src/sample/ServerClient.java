package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
/**
 * The server client relies on the observable list to be able to update the list views within the UI as well as communicate directly with the Server using 
 * the pre-determined address and port
* @param socket the individual socket that will be used for server connection
* @param in used to be able to read the contents of the socket
* @param networkOut allows the ability of uploading to the server and writing the textfile data into the input stream
* @param networkIn allows for the download of file and socket data to be read using a buffered reader
* @param SERVER_ADDRESS the name localhost address which should work universally
* @param SERVER_PORT the name the port that this server will use to connect
 */
public class ServerClient extends Frame {
    private static ObservableList<String> fileListServer = FXCollections.observableArrayList();
    private static ObservableList<String> fileListClient = FXCollections.observableArrayList();

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    //we can read this from the user too
    public  static String SERVER_ADDRESS = "localhost";
    public  static int    SERVER_PORT = 16789;
/**
 * The server client relies on the observable list to be able to update the list views within the UI as well as communicate directly with the Server using 
 * the pre-determined address and port
 * @param cmd reads the command from the controller to communicate the correct action
 * @param serverFolder the correct server folder gives the internal location of where the servers files are located
 */
    public ServerClient(String cmd, File serverFolder) {

        //try's to create a socket and establish a connection with the server on command
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: "+SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("IOEXception while connecting to server: "+SERVER_ADDRESS);
        }
        if (socket == null) {
            System.err.println("socket is null");
        }
        try {
            networkOut = new PrintWriter(socket.getOutputStream(), true);
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
        //a seperate method is called to perform the correct action
        processUserInput(cmd, serverFolder);
    }

    //getters for the observable list within the controller
    public static ObservableList<String> getFileListServer() {
        return fileListServer;
    }

    public static ObservableList<String> getFileListClient() {
        return fileListClient;
    }
/**
Every time a button is pushed this method is called
The appropriate if statement duns based off of what cmd is
then the socket is closed
 * @param cmd reads the command from the controller to communicate the correct action
 * @param serverFolder the correct server folder gives the internal location of where the servers files are located
 */

    protected void processUserInput(String cmd, File serverFolder) {
        String input = null;


        if(cmd == null){
            System.out.println("Please click one of the \"Download\", \"Upload\", or \"Refresh\" buttons");

            //refresh
        } else if(cmd == "refresh"){


            System.out.println("refreshing files...");

            //set path to client's local folder
            String clientPath = (System.getProperty("user.dir")+"/src/resources");
            File clientFolder = new File(clientPath);

            fileListServer.clear();
            fileListClient.clear();

            String serverContents[] = serverFolder.list();
            String clientContents[] = clientFolder.list();


            //get server files
            for(int i=0; i<serverContents.length; i++) {
                fileListServer.add(serverContents[i]);
            }

            //get client files
            for(int i=0; i<clientContents.length; i++){
                fileListClient.add(clientContents[i]);
            }

            System.out.println("\n");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //download
        } else if(cmd == "download"){
            System.out.println("downloading files...\n");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //upload
        } else if(cmd == "upload"){
            System.out.println("uploading files...\n");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

//initial main function for server client
    public static void main(String[] args) {
        //generic file object
        File file = new File(System.getProperty("user.dir"));
        ServerClient client = new ServerClient("cmd", file);
    }
}
