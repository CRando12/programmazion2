package monkey;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
    
    public static int nConnection = 0;
    public static ServerSocket ss;
    
    public Server(int port) throws IOException {
        ss = new ServerSocket(port);
        System.out.println(">>> Server connesso sulla porta " + port);
    }
    
    public static void connect() throws IOException
    {
        Socket s = ss.accept();
        
        nConnection++;
        
        System.out.println("Client ["+nConnection+"] si è connesso.");
        
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("Benvenuto nel gioco");

        pr.flush();
        
        String str = bf.readLine();
        System.out.println("Client ["+nConnection+"] nome utente: " + str);
        
        connect();
    }
    
}
