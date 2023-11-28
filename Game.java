package monkey;

import fonts.GetFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Game implements Runnable, ActionListener, KeyListener
{    
    private JFrame f;
    private JLayeredPane finestraFineGioco;
    private JPanel transparentPanel;
    private JLabel lab_frutta, lab_score, lab_timer;
    private JButton home;
    private CaricatoreSfondo gioco;
    private CaricatoreImmagini img_scimmia;
    private Rectangle scimmia;
    private Timer t_game, t_countdown, t_frutta;
    
    // Se l'utente è loggato, default falso
    private boolean isLogged = false;
    private String nickname;
    
    // Database
    private Db db;
    
    // Impostazioni di gioco
    private final int N_MAX_FRUTTA_INGAME = 10; // n frutta in gioco
    private final int TIME_GENERATE_FRUTTA = 500; // msec
    private final int PUNTI_FRUTTA = 5; // punti per ogni frutta presa
    private final double VELOCITA_CADUTA = 1.60; 
    
    // Variabili di gioco
    private int score=0, nFruttaPresa = 0;
    private boolean gameOver = false;
    
    // Variabili del countdown
    private int t_min = 1;
    private int t_sec = 0;
    private DecimalFormat dFormat = new DecimalFormat("00");
    private String ddSecond, ddMinute;
    
    // Variabili della scimmia
    private int x_scimmia = 150;
    private final int y_scimmia = 535;
    private final int lar_scimmia = 170;
    private final int alt_scimmia = 130;
    private final int velocita = 40;
    
    // Variabili della frutta
    private ArrayList<Frutta> frutta = new ArrayList();
    
    public Game(JFrame f, boolean isLogged)
    {
        this.f = f;
        this.isLogged = isLogged;
        
        if(isLogged)
        {
            // Apro la connessione al database
            db = new Db();
        }
    }
    
    public void inizia()
    {
        f.getContentPane().removeAll();
        f.repaint();
        
        Sounds startgame = new Sounds("startgame.wav");
        startgame.start();
        
        // Riproduco la soundtrack
        Sounds.stopSoundtrack();
        Sounds.playSoundtrack("soundtrack.wav");
        
        // Se è impostato su partita singola
        // Carica le impostazioni di default
        if(isLogged == false)
        {
            Impostazioni.loadDefaultSettings();
        }
        
        // Prelevo il background dal metodo all'interno delle impostazioni
        // Se non è stato scelto un altro background, verrà impostato il primo di default
        // Solo se l'utente è in modalità carriera verrà scelto un altro diverso dal default
        String bg_image = Impostazioni.getBackground();
        
        //Carichiamo il pannello iniziale
        gioco = new CaricatoreSfondo(bg_image);
        //Aggiungiamo il pannello al frame
        f.getContentPane().add(gioco);
        //Impostiamo un layout libero
        gioco.setLayout(null);
        
        //Impostiamo un layout libero
        gioco.setLayout(null);
        
        f.addKeyListener(this);
        f.requestFocusInWindow();
        f.pack();
        f.setVisible(true);
        
        String monkey_image = Impostazioni.getMonkey();
        img_scimmia = new CaricatoreImmagini(monkey_image, lar_scimmia, alt_scimmia);
        img_scimmia.setBounds(0, 0, lar_scimmia, alt_scimmia);
        img_scimmia.setOpaque(false);
        
        //posizione di partenza
        scimmia = new Rectangle();
        scimmia.setBounds(x_scimmia, y_scimmia, lar_scimmia, alt_scimmia);
        scimmia.add(img_scimmia);
        scimmia.setVisible(true);
        
        lab_frutta = new JLabel("Frutta raccolta: " + nFruttaPresa);
        new GetFont("Digital.TTF", 20f);
        lab_frutta.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        lab_frutta.setBounds(50, 20, 250, 20);
        lab_frutta.setForeground(Color.WHITE);
        lab_frutta.setVisible(true);
        
        lab_score = new JLabel("Punteggio: " + score);
        new GetFont("Digital.TTF", 20f);
        lab_score.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        lab_score.setBounds(50, 50, 150, 20);
        lab_score.setForeground(Color.WHITE);
        lab_score.setVisible(true);
        
        ddSecond = dFormat.format(t_sec);
        ddMinute = dFormat.format(t_min);
        
        lab_timer = new JLabel(ddMinute + ":" + ddSecond);
        new GetFont("Digital.TTF", 44f);
        lab_timer.setFont(GetFont.GetThisFont());
        lab_timer.setForeground(new java.awt.Color(21, 214, 108));
        lab_timer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lab_timer.setBounds(1140, 0, 120, 50);
        
        gioco.add(scimmia, JLayeredPane.DRAG_LAYER);
        gioco.add(lab_frutta);
        gioco.add(lab_score);
        gioco.add(lab_timer);
        
        Thread thread_gioco = new Thread(this);
        thread_gioco.start();
        
        generaFrutta();
        timerStart();
    }
    
    @Override
    public void run() 
    {
        t_game = new Timer(1, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            { 
                if(gameOver == false)
                {
                    for(int i=0; i<frutta.size(); i++)    
                    {
                        Frutta ft = frutta.get(i);      

                        if (ft.getY() > (Monkey.getAltezza()))
                        {
                            gioco.remove(ft.getFrutta());
                            frutta.remove(i);
                        }
                        else
                        {
                            double y = (ft.getY() + (2 * ft.getVelocita()));
                            ft.setY((int) y);
                            ft.aggiorna();
                            gioco.add(ft.getFrutta(), JLayeredPane.POPUP_LAYER);
                        }

                        // Se la scimmia collide con la frutta
                        if(intersects(scimmia, ft.getFrutta()) == true) 
                        {
                            Sounds grabfruit = new Sounds("grabfruit.wav");
                            grabfruit.start();
                            gioco.remove(ft.getFrutta());
                            frutta.remove(i);
                            nFruttaPresa++;
                            aggiornaScore();
                        }
                    }
                }

                f.pack();
                f.repaint();
            }
        });
        t_game.start();
    }
    
    private void generaFrutta()
    {
        String fruit_image = Impostazioni.getFruit();
        
        t_frutta = new Timer(TIME_GENERATE_FRUTTA, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if (frutta.size() < N_MAX_FRUTTA_INGAME)
                {
                    frutta.add(new Frutta(fruit_image, 50, 50, VELOCITA_CADUTA));
                }
            }
        });
        t_frutta.start();
    }
    
    private void timerStart()
    {
        t_countdown = new Timer(1000, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                t_sec--;
                ddSecond = dFormat.format(t_sec);
                ddMinute = dFormat.format(t_min);

                lab_timer.setText(ddMinute+":"+ddSecond);

                if(t_sec==-1)
                {
                    t_sec=59;
                    t_min--;
                    ddSecond = dFormat.format(t_sec);
                    ddMinute = dFormat.format(t_min);
                    lab_timer.setText(ddMinute+":"+ddSecond);
                }

                // Se il countdown è scaduto, riproduci gameover
                if(t_min==0 && t_sec==0)
                {
                    t_countdown.stop();
                    finePartita();                    
                }
            }
        });
        
        t_countdown.start();
    }
    
    // Metodo pubblico setter
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }
    
    private void aggiornaScore()
    {
        score = score + PUNTI_FRUTTA;
        lab_score.setText("Score: " + score);
        lab_frutta.setText("Frutta raccolta: " + nFruttaPresa);
    }
    
    private void updateAccountScore()
    {
        // Se l'utente è loggato
        if(isLogged == true)
        {
            // Update dello score del player
            try {
                int credits = (score * 10) / 500;
                
                String q_updates = "UPDATE players SET score = score + '"+score+"', credits = credits + '"+credits+"', played = played+1 WHERE nickname = '"+nickname+"'";
                db.GetConn().createStatement().executeUpdate(q_updates);
                
                String userQuery = "SELECT * FROM players WHERE nickname = '"+nickname+"'";
                Statement stmt = (Statement) db.GetConn().createStatement();
                ResultSet user_set = stmt.executeQuery(userQuery);
                
                int user_level=0, user_score=0;
                
                if(user_set.next())
                {
                    user_level = user_set.getInt("level");
                    user_score = user_set.getInt("score");
                }
                
                boolean newLevel = false;
                
                switch(user_level)
                {
                    case 1:
                        if(user_score > 50) newLevel = true;
                        break;
                    case 2:
                        if(user_score > 70) newLevel = true;
                        break;
                    case 3:
                        if(user_score > 500) newLevel = true;
                        break;
                }
                
                if(newLevel)
                {
                    int lv = user_level + 1;
                    String q_nextLevel = "UPDATE players SET level = '"+lv+"' WHERE nickname = '"+nickname+"'";
                    db.GetConn().createStatement().executeUpdate(q_nextLevel);
                }
            } 
            catch (SQLException ex){
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void finePartita()
    {
        gameOver = true;
        Sounds.stopSoundtrack();
        Sounds gameover = new Sounds("gameover.wav");
        gameover.start();
        
        updateAccountScore();
        
        // Creazione del pannello trasparente
        transparentPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 100)); // Colore di sfondo trasparente
                g.fillRect(0, 0, getWidth(), getHeight()); // Riempimento del pannello
                g.setColor(Color.WHITE); // Colore del testo
                g.setFont(new Font("Arial", Font.BOLD, 30)); // Impostazione del font del testo
                String testo = "Fine partita!\nPunteggio ottenuto: "+score+"\nFrutta raccolta: "+nFruttaPresa; // Testo con andamento a capo
                String[] righe = testo.split("\n"); // Suddivisione del testo in righe separate
                int y = getHeight() / 2 - righe.length * 15; // Calcolo della posizione verticale delle righe
                for (String riga : righe) {
                    g.drawString(riga, getWidth() / 2 - g.getFontMetrics().stringWidth(riga) / 2, y);
                    y += 30; // Spaziatura tra le righe
                }
            }
        };
        transparentPanel.setBounds(0, 0, f.getWidth(), f.getHeight()); // Impostazione delle dimensioni del pannello
        transparentPanel.setOpaque(false); // Impostazione del pannello come trasparente

        // Aggiunta del pannello al layer più alto del JLayeredPane
        finestraFineGioco = f.getLayeredPane();
        finestraFineGioco.add(transparentPanel, JLayeredPane.POPUP_LAYER);
        
        home = new JButton("Torna al Menu");
        new GetFont("Waverly.ttf", 20f);
        home.setBackground(new java.awt.Color(240, 240, 240));
        home.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        home.setBounds(480, 420, 300, 50);
        home.addActionListener(this);
        finestraFineGioco.add(home, JLayeredPane.POPUP_LAYER);
        
        // Aggiornamento del frame
        f.revalidate();
        f.repaint();
    }
    
    private boolean intersects(Rectangle obj1, Rectangle obj2)
    {
        Area a1 = new Area(obj1.getBounds());
        Area a2 = new Area(obj2.getBounds());
        
        return a1.intersects(a2.getBounds2D());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Prendo il bottone che ha scatenato l'evento
        Object source = e.getSource();
        
        //A seconda del bottone eseguo codice differente
        if(source.equals(home)) 
        {
            finestraFineGioco.remove(transparentPanel);
            finestraFineGioco.remove(home);
            Sounds click = new Sounds("click.wav");
            click.start();
            
            if(isLogged)
            {
                Account a = new Account(f, nickname);
            } else {
                Menu m = new Menu(f);
                m.inizia();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    //quando premo un tasto sulla tastiera
    public void keyPressed(KeyEvent e)
    {       
        if(gameOver == false)
        {
            Sounds walk = new Sounds("walk.wav");
            
            int keycode = e.getKeyCode();  //metodo che rappresenta il tasto
            switch(keycode)
            {
                case KeyEvent.VK_LEFT:  //quandopremiamo a sinistra fa qualcosa
                    if(x_scimmia>0){
                        x_scimmia-=velocita;
                    }
                    walk.start();
                    break;
                case KeyEvent.VK_RIGHT:
                    if(x_scimmia+lar_scimmia<Monkey.getLarghezza()){
                        x_scimmia+=velocita;  
                    }
                    walk.start();
                    break;
            }

            scimmia.setBounds(x_scimmia, y_scimmia, lar_scimmia, alt_scimmia);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
    
}