import java.io.*;
import java.net.*;
import java.util.*;
/**
 * The server thread handles all outside connections from the client and establishes a threaded connection and communicates that to the server
 * @param socket the socket that will be created on server call
 * @param out used to read from sockets outstream
 * @param in used to write to sockets instream
 */
public class ServerThread extends Thread {
    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;


    public ServerThread(Socket socket) {
        super();
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOEXception while opening a read/write connection");
        }
    }
    //allows for each thread to be created but will only be used until the action is completed then it will close the socket
    public void run() {
        // initialize interaction
        out.println("Connected to Server");

        boolean endOfSession = false;
        while(!endOfSession) {
            endOfSession = true;
        }
        try {
            System.out.println("Client disconnected\n");
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


}