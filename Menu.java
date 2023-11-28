package monkey;

import fonts.GetFont;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Menu implements ActionListener, Runnable
{
    private JFrame f;
    private JButton newgame;
    private JButton regole;
    private JButton exitgame;
    private JButton carriera;
    
    public Menu(JFrame f)
    {
        this.f = f;
    }
    
    public void inizia() 
    {
        f.getContentPane().removeAll();
        f.getLayeredPane().revalidate();
        f.repaint();

        //Carichiamo il pannello iniziale
        
        
        //Nome class      oggetto        Costruttore
        CaricatoreSfondo sfondo = new CaricatoreSfondo("sfondo4.jpg");
     
        
        //Aggiungiamo il pannello al frame
        f.getContentPane().add(sfondo);
        //Impostiamo un layout libero
        sfondo.setLayout(null);
        
        // Riproduco la soundtrack
        Sounds.stopSoundtrack();
        Sounds.playSoundtrack("soundtrack.wav");
                
        //Definiamo i bottoni
        newgame = new JButton("PARTITA SINGOLA");
        new GetFont("Waverly.ttf", 20f);
        newgame.setBackground(new java.awt.Color(204, 204, 204));
        newgame.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        newgame.setBounds(480, 280, 300, 50);
        
        regole = new JButton("REGOLE DI GIOCO");
        new GetFont("Waverly.ttf", 20f);
        regole.setBackground(new java.awt.Color(204, 204, 204));
        regole.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        regole.setBounds(480, 350, 300, 50);
        
        carriera = new JButton("CARRIERA");
        new GetFont("Waverly.ttf", 20f);
        carriera.setBackground(new java.awt.Color(204, 204, 204));
        carriera.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        carriera.setBounds(480, 420, 300, 50);
        
        exitgame = new JButton("ESCI");
        exitgame.setBackground(new java.awt.Color(204, 204, 204));
        new GetFont("Waverly.ttf", 20f);
        exitgame.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        exitgame.setBounds(480, 490, 300, 50);
        
        //Aggiungiamo i bottoni al pannello
        sfondo.add(newgame);
        sfondo.add(regole);
        sfondo.add(carriera);
        sfondo.add(exitgame);
        
        //Chiamiamo l'actionlistener
        newgame.addActionListener(this);
        regole.addActionListener(this);
        carriera.addActionListener(this);
        exitgame.addActionListener(this);
                   
        //Fondiamo tutti i componenti        
        f.pack();
        
        //Impostiamo la finestra visibile
        f.setVisible(true);
        
        Thread client = new Thread(this);
        client.start();
    }
    
    public void restart()
    {
        f.getContentPane().removeAll();
        inizia();
    }
    
    //Sovrasciviamo il metodo che indica quando il bottone è premuto
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        //Prendo il bottone che ha scatenato l'evento
        Object source = e.getSource();
        
        //A seconda del bottone eseguo codice differente
        if(source.equals(newgame)) 
        {
            Sounds click = new Sounds("click.wav");
            click.start();
            // Il secondo parametro è settato su false perchè vuol dire utente loggato
            Game g = new Game(f, false);
            g.inizia();
        }
        else if(source.equals(regole))
        {
            Sounds click = new Sounds("click.wav");
            click.start();
            Regole r = new Regole(f);
            r.inizia();
        }
        else if(source.equals(carriera))
        {
            Sounds click = new Sounds("click.wav");
            click.start();
            Login l = new Login(f);
            l.inizia();
        }
        else if(source.equals(exitgame))
        {
            Sounds click = new Sounds("click.wav");
            click.start();
            System.out.println("Sei uscito dal gioco.");
            System.exit(0);
        }
    }
    
    @Override
    public void run()
    {
        try {
            Server.connect();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}