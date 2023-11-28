package monkey;

import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;


public class Monkey 
{
    //Stabilisco la larghezza della finestra (private perchè è solo per la classe gioco; 
    // static perchè la andremo ad usare nel main che è statico e final perchè sono costanti)
    private static final int larghezza = 1280;
    //Stabilisco l'altezza della finestra
    private static final int altezza = 720;
    //Stabilisco il nome del gioco 
    private static final String nome_gioco = "MonkeyGame"; 
    

    public Monkey() {}
    
    
    public static void main(String[] args) 
    {        
        //Creiamo la finestra
        JFrame f = new JFrame (nome_gioco);
        
        //Creiamo la dimensione della finestra
        Dimension dimensione_finestra = new Dimension(larghezza, altezza);
        
        //Impostiamo la finestra
        f.setSize(dimensione_finestra);
        f.setPreferredSize(dimensione_finestra);
        f.setMaximumSize(dimensione_finestra);
        f.setResizable(false);
        
        //Imposta la chiusura della finestra quando clicco la x
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            // Apriamo la connessione con il server (multiplayer) sulla porta 20098
            Server s = new Server(20098);
        } catch (IOException ex) {
            System.err.println("Errore: connessione al server localhost:20098 fallita.");
        }
        
        Menu m = new Menu(f);
        m.inizia();
    }
    
    public static int getLarghezza()
    {
        return larghezza;
    }
    
    public static int getAltezza()
    {
        return altezza;
    }


}

