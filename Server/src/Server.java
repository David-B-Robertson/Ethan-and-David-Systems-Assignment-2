
import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
    protected Socket clientSocket           = null;
    protected ServerSocket serverSocket     = null;
    protected ServerThread[] threads    = null;
    public int numClients                = 0;

    public static int SERVER_PORT = 16789;
    public static int MAX_CLIENTS = 25;

    public Server() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("---------------------------");
            System.out.println("File Sharing Application is running");
            System.out.println("---------------------------");
            System.out.println("Listening to port: "+SERVER_PORT);
            threads = new ServerThread[MAX_CLIENTS];
            while(true) {
                clientSocket = serverSocket.accept();
                System.out.println("Client #"+(numClients+1)+" connected.");
                threads[numClients] = new ServerThread(clientSocket);
                threads[numClients].start();
                numClients++;
            }
        } catch (IOException e) {
                System.err.println("IOException while creating server connection");
        }
    }
        public static void main(String[] args) {
            Server app = new Server();
        }
}

