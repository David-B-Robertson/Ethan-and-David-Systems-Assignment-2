import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
    protected Socket socket       = null;
    protected PrintWriter out     = null;
    protected BufferedReader in   = null;

    protected boolean bLoggedIn   = false;
    protected String strUserID    = null;
    protected String strPassword  = null;

    protected Vector messages     = null;

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