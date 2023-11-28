package monkey;

import fonts.GetFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Impostazioni implements MouseListener {
    
    private JFrame f;
    private CaricatoreSfondo sfondo;
    private JLabel title, lab_score;
    private Rectangle bg_image_box, fruit_image_box, monkey_image_box;
    private CaricatoreImmagini bg_background, fruit_background, monkey_background;
    private JButton conferma;
    private JButton bg_left, bg_right;
    private JButton fruit_left, fruit_right;
    private JButton monkey_left, monkey_right;
    
    // Vettore di background
    private static String[] bgs = {"sfondo0.jpg", "sfondo1.jpg", "sfondo2.jpg", "sfondo3.jpg", "sfondo4.jpg"};
    private static String[] fruits = {"banana.png", "fragola.png", "mela.png", "pera.png"};
    private static String[] monkeys = {"scimmia0.png", "scimmia1.png", "scimmia2.png"};
    private static int bgs_count = 0;
    private static int fruit_count = 0;
    private static int monkey_count = 0;
    
    // Nuove liste 
    private static ArrayList<String> my_bgs = new ArrayList();
    private static ArrayList<String> my_fruits = new ArrayList();
    private static ArrayList<String> my_monkeys = new ArrayList();
    
    // Connessione al db
    private Db db;
    
    // User-data
    private String nickname;
    private boolean isLogged = false;
    
    public Impostazioni(JFrame f)
    {
        this.f = f;
    }
    
    public Impostazioni(JFrame f, String nickname)
    {
        this.f = f;
        this.nickname = nickname;
        this.isLogged = true;
        
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
        
        sfondo = new CaricatoreSfondo("sfondo0.jpg");   
        f.getContentPane().add(sfondo);
        
        //Impostiamo un layout libero
        sfondo.setLayout(null);
        
        f.pack();
        f.setVisible(true);
        
        lab_score = new JLabel("Account: " + nickname);
        new GetFont("Digital.TTF", 20f);
        lab_score.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        lab_score.setBounds(50, 20, 250, 20);
        lab_score.setForeground(Color.WHITE);
        lab_score.setVisible(true);
        
        new GetFont("Waverly.ttf" ,36f);
        title = new JLabel("Scegli le impostazioni di gioco");
        title.setBounds(400, 30, 800, 80);
        title.setForeground(Color.WHITE);
        title.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        
        /* INIZIO SCELTA DELLO SFONDO */
        bg_left = new JButton("");
        bg_left.setIcon(new ImageIcon(getClass().getResource("/images/arrow_left.png")));
        bg_left.setFont(GetFont.GetThisFont());
        bg_left.setBounds(340, 200, 40, 40);
        bg_left.setOpaque(false);
        bg_left.setContentAreaFilled(false);
        bg_left.setBorderPainted(false);
        bg_left.addMouseListener(this);
        bg_left.setVisible(true);
        
        bg_image_box = new Rectangle();
        bg_image_box.setOpacity(50);
        bg_image_box.setBounds(450, 120, 280, 150);
        bg_image_box.setOpaque(true);
        
        bg_background = new CaricatoreImmagini(bgs[0], 300, 170);
        bg_background.setBounds(10, 10, 300, 170);
        bg_background.setOpaque(false);
        
        bg_right = new JButton("");
        bg_right.setIcon(new ImageIcon(getClass().getResource("/images/arrow_right.png")));
        bg_right.setFont(GetFont.GetThisFont());
        bg_right.setBounds(800, 200, 40, 40);
        bg_right.setOpaque(false);
        bg_right.setContentAreaFilled(false);
        bg_right.setBorderPainted(false);
        bg_right.addMouseListener(this);
        bg_right.setVisible(true);
        /* FINE SCELTA DELLO SFONDO */
        
        
        /* INIZIO SCELTA FRUTTA */
        fruit_left = new JButton("");
        fruit_left.setIcon(new ImageIcon(getClass().getResource("/images/arrow_left.png")));
        fruit_left.setFont(GetFont.GetThisFont());
        fruit_left.setBounds(340, 340, 40, 40);
        fruit_left.setOpaque(false);
        fruit_left.setContentAreaFilled(false);
        fruit_left.setBorderPainted(false);
        fruit_left.addMouseListener(this);
        fruit_left.setVisible(true);
        
        fruit_image_box = new Rectangle();
        fruit_image_box.setOpacity(50);
        fruit_image_box.setBounds(540, 300, 100, 100);
        fruit_image_box.setOpaque(true);
        
        fruit_background = new CaricatoreImmagini(fruits[0], 100, 100);
        fruit_background.setBounds(10, 10, 100, 100);
        fruit_background.setOpaque(false);
        
        fruit_right = new JButton("");
        fruit_right.setIcon(new ImageIcon(getClass().getResource("/images/arrow_right.png")));
        fruit_right.setFont(GetFont.GetThisFont());
        fruit_right.setBounds(800, 340, 40, 40);
        fruit_right.setOpaque(false);
        fruit_right.setContentAreaFilled(false);
        fruit_right.setBorderPainted(false);
        fruit_right.addMouseListener(this);
        fruit_right.setVisible(true);
        /* FINE SCELTA FRUTTA */
        
        
        /* INIZIO SCELTA SCIMMIA */
        monkey_left = new JButton("");
        monkey_left.setIcon(new ImageIcon(getClass().getResource("/images/arrow_left.png")));
        monkey_left.setFont(GetFont.GetThisFont());
        monkey_left.setBounds(340, 480, 40, 40);
        monkey_left.setOpaque(false);
        monkey_left.setContentAreaFilled(false);
        monkey_left.setBorderPainted(false);
        monkey_left.addMouseListener(this);
        monkey_left.setVisible(true);
        
        monkey_image_box = new Rectangle();
        monkey_image_box.setOpacity(50);
        monkey_image_box.setBounds(540, 450, 100, 100);
        monkey_image_box.setOpaque(true);
        
        monkey_background = new CaricatoreImmagini(monkeys[0], 100, 100);
        monkey_background.setBounds(10, 10, 100, 100);
        monkey_background.setOpaque(false);
        
        monkey_right = new JButton("");
        monkey_right.setIcon(new ImageIcon(getClass().getResource("/images/arrow_right.png")));
        monkey_right.setFont(GetFont.GetThisFont());
        monkey_right.setBounds(800, 480, 40, 40);
        monkey_right.setOpaque(false);
        monkey_right.setContentAreaFilled(false);
        monkey_right.setBorderPainted(false);
        monkey_right.addMouseListener(this);
        monkey_right.setVisible(true);
        /* FINE SCELTA SCIMMIA */
        
        
        conferma = new JButton("INIZIA PARTITA");
        new GetFont("Waverly.ttf" ,18f);
        conferma.setBounds(Monkey.getLarghezza()-250, 30, 180, 40);
        conferma.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        conferma.addMouseListener(this);
        
        // Applico allo sfondo ogni elemento creato
        sfondo.add(title);
        sfondo.add(lab_score);
        sfondo.add(bg_image_box);
        sfondo.add(bg_left);
        sfondo.add(bg_right);
        sfondo.add(fruit_image_box);
        sfondo.add(fruit_left);
        sfondo.add(fruit_right);
        sfondo.add(monkey_image_box);
        sfondo.add(monkey_left);
        sfondo.add(monkey_right);
        sfondo.add(conferma, JLayeredPane.POPUP_LAYER);
        bg_image_box.add(bg_background);
        fruit_image_box.add(fruit_background);
        monkey_image_box.add(monkey_background);
        
        
        // Copio gli elementi nelle liste nuove
        //my_bgs.addAll(Arrays.asList(bgs));
        //my_fruits.addAll(Arrays.asList(fruits));
        //my_monkeys.addAll(Arrays.asList(monkeys));
        
        my_bgs.add(bgs[0]);
        my_fruits.add(fruits[0]);
        my_monkeys.add(monkeys[0]);

        
        // Caricamento degli acquisti dell'utente
        try {
            String playerItemsQuery = "SELECT * FROM items";
            Statement stmt1 = (Statement) db.GetConn().createStatement();
            ResultSet items = stmt1.executeQuery(playerItemsQuery);

            while(items.next())
            {
                int id_item = items.getInt("id");
                
                String checkPlayerItem = "SELECT * FROM player_item WHERE id = '" + id_item + "' AND nickname = '" + nickname + "'";
                Statement stmt2 = (Statement) db.GetConn().createStatement();
                ResultSet playerItem = stmt2.executeQuery(checkPlayerItem);
                
                // Se l'utente possiede l'oggetto, aggiungilo dall'array
                if(playerItem.next())
                {
                    String item_type = items.getString("type");
                    String item_url = items.getString("img_url");
                    
                    switch(item_type)
                    {
                        case "fruit":
                            my_fruits.add(item_url);
                            break;
                        case "background":
                            my_bgs.add(item_url);
                            break;
                        case "monkey":
                            my_monkeys.add(item_url);
                            break;
                    }
                }
            }
        } catch (SQLException ex){
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Frecce per il background
        if(e.getSource() == bg_left)
        {
            bgs_count--;
            
            if(bgs_count < 0)
                bgs_count = (my_bgs.size()-1);
            
            bg_image_box.remove(bg_background);
            bg_background = new CaricatoreImmagini(my_bgs.get(bgs_count), 300, 170);
            bg_background.setBounds(10, 10, 300, 170);
            bg_background.setOpaque(false);
            bg_image_box.add(bg_background);
        } 
        else if(e.getSource() == bg_right)
        {
            bgs_count++;
            
            if(bgs_count > (my_bgs.size()-1))
                bgs_count = 0;
            
            bg_image_box.remove(bg_background);
            bg_background = new CaricatoreImmagini(my_bgs.get(bgs_count), 300, 170);
            bg_background.setBounds(10, 10, 300, 170);
            bg_background.setOpaque(false);
            bg_image_box.add(bg_background);
        }
        else if(e.getSource() == fruit_left)
        {
            fruit_count--;
            
            if(fruit_count < 0)
                fruit_count = (my_fruits.size()-1);
            
            fruit_image_box.remove(fruit_background);
            fruit_background = new CaricatoreImmagini(my_fruits.get(fruit_count), 100, 100);
            fruit_background.setBounds(10, 10, 100, 100);
            fruit_background.setOpaque(false);
            fruit_image_box.add(fruit_background);
        } 
        else if(e.getSource() == fruit_right)
        {
            fruit_count++;
            
            if(fruit_count > (my_fruits.size()-1))
                fruit_count = 0;
            
            fruit_image_box.remove(fruit_background);
            fruit_background = new CaricatoreImmagini(my_fruits.get(fruit_count), 100, 100);
            fruit_background.setBounds(10, 10, 100, 100);
            fruit_background.setOpaque(false);
            fruit_image_box.add(fruit_background);
        }
        else if(e.getSource() == monkey_left)
        {
            monkey_count--;
            
            if(monkey_count < 0)
                monkey_count = (my_monkeys.size()-1);
            
            monkey_image_box.remove(monkey_background);
            monkey_background = new CaricatoreImmagini(my_monkeys.get(monkey_count), 100, 100);
            monkey_background.setBounds(10, 10, 100, 100);
            monkey_background.setOpaque(false);
            monkey_image_box.add(monkey_background);
        } 
        else if(e.getSource() == monkey_right)
        {
            monkey_count++;
            
            if(monkey_count > (my_monkeys.size()-1))
                monkey_count = 0;
            
            monkey_image_box.remove(monkey_background);
            monkey_background = new CaricatoreImmagini(my_monkeys.get(monkey_count), 100, 100);
            monkey_background.setBounds(10, 10, 100, 100);
            monkey_background.setOpaque(false);
            monkey_image_box.add(monkey_background);
        }
        else if(e.getSource() == conferma)
        {
            // Parametro utente loggato = true
            Game g = new Game(f, true);
            g.setNickname(nickname);
            g.inizia();
        }
        
        f.pack();
        f.repaint();
    }
    
    public static void loadDefaultSettings()
    {
        bgs_count = 0;
        fruit_count = 0;
        monkey_count = 0;
        
        // Copio gli elementi nelle liste nuove
        my_bgs.add(bgs[0]);
        my_fruits.add(fruits[0]);
        my_monkeys.add(monkeys[0]);
    }
    
    // Metodo getter del background scelto dall'utente
    public static String getBackground()
    {
        return my_bgs.get(bgs_count);
    }
    
    // Metodo getter della frutta scelta dall'utente
    public static String getFruit()
    {
        return my_fruits.get(fruit_count);
    }
    
    // Metodo getter della scimmia scelta dall'utente
    public static String getMonkey()
    {
        return my_monkeys.get(monkey_count);
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}
