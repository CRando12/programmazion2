package monkey;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    
    private String ip;
    private int port;
    private Socket s = null;
    
    public static void main(String[] args) throws IOException
    {
        Client c = new Client();
        c.connect();
    }
    
    public void connect() throws IOException, ConnectException
    {
        System.out.print("IP: ");
        Scanner in_ip = new Scanner(System.in);
        ip = in_ip.nextLine();
        
        System.out.print("Porta: ");
        Scanner in_port = new Scanner(System.in);
        port = in_port.nextInt();
        
        try {
            s = new Socket(ip, port);
        } catch (ConnectException e) {
            System.out.println("\n[Monkey Game]: errore di connessione al server ["+ip+":"+port+"]");
        }
        
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        System.out.println("\n[Monkey Game]: connesso al server");
        System.out.println("[Server]: " + bf.readLine() + "\n");

        System.out.print("Nome utente: ");
        Scanner scan = new Scanner(System.in);
        String text = scan.nextLine();

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(text);
        pr.flush();
        
        System.out.println(s.isConnected());
    }
    
}
