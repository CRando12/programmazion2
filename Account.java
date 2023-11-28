package monkey;

import fonts.GetFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class Account implements MouseListener {
    
    private JFrame f;
    private CaricatoreSfondo sfondo;
    private JLabel label, desc, shop_title, error;
    private JButton enter, back;
    private JButton[] buys = new JButton[100];
    private String accountText;
    private Rectangle bg_shop;
    private CaricatoreImmagini[] imgs = new CaricatoreImmagini[100];
    
    // User-data
    private String nickname;
    
    // Variabili dello shop
    private JPanel p;
    private JLabel label_descitem;
    private Rectangle r_shop;
    private JScrollPane scroll_shop;
    
    public Account(JFrame f, String nickname)
    {
        this.f = f;
        this.nickname = nickname;
        account();
    }
    
    private void account() 
    {
        f.getContentPane().removeAll();
        f.repaint();
        
        sfondo = new CaricatoreSfondo("sfondo1.jpg");   
        f.getContentPane().add(sfondo);
        
        //Impostiamo un layout libero
        sfondo.setLayout(null);
        
        f.pack();
        f.setVisible(true);
        
        // Grafica dell'account del player
        
        back = new JButton("");
        back.setIcon(new ImageIcon(getClass().getResource("/images/arrow_left.png")));
        back.setFont(GetFont.GetThisFont());
        back.setBounds(20, 20, 40, 40);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.addMouseListener(this);
        back.setVisible(true);
        sfondo.add(back);
        
        Rectangle content = new Rectangle();
        content.setBackground(0, 0, 0); // Nero
        content.setOpacity(140);
        content.setBounds(50, 80, 320, 410); //640
        sfondo.add(content);
        
        enter = new JButton("GIOCA");
        enter.setBounds(110, 550, 180, 40);
        enter.setBackground(Color.WHITE);
        enter.addMouseListener(this);
        sfondo.add(enter);
        
        JLabel title = new JLabel("<html>Account di \n" + nickname + "</html>");
        title.setBounds(20, 20, 250, 60);
        title.setForeground(Color.WHITE);
        new GetFont("Nimbus.otf", 30f);
        title.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        content.add(title);
        
        try {
            accountText = "<html>"
                    + "Livello: " + Db.getColFromPlayers("level", nickname)
                    + "\n<br>"
                    + "Score: " + Db.getColFromPlayers("score", nickname)
                    + "\n<br>"
                    + "Partite giocate: " + Db.getColFromPlayers("played", nickname)
                    + "\n<br>\n<br>"
                    + "Crediti: " + Db.getColFromPlayers("credits", nickname)
                    + "</html>";
        } catch (SQLException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        desc = new JLabel(accountText);
        desc.setBounds(20, 80, 250, 290);
        desc.setForeground(Color.WHITE);
        new GetFont("Nimbus.otf", 22f);
        desc.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        content.add(desc);
        
        // SHOP
        
        bg_shop = new Rectangle();
        bg_shop.setBackground(0, 0, 0); // Nero
        bg_shop.setOpacity(140);
        bg_shop.setBounds(380, 80, 830, 550); //700 - 500
        sfondo.add(bg_shop);
        
        shop_title = new JLabel("SHOP");
        shop_title.setBounds(20, 20, 100, 60);
        shop_title.setForeground(Color.WHITE);
        new GetFont("Nimbus.otf", 30f);
        shop_title.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        shop_title.setHorizontalAlignment(SwingConstants.CENTER);
        bg_shop.add(shop_title);
        
        error = new JLabel("");
        error.setBounds(25, 50, 600, 60);
        error.setForeground(Color.RED);
        new GetFont("Nimbus.otf", 18f);
        error.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        bg_shop.add(error);
        
        aggiornaShop();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Se l'utente clicca il pulsante "gioca"
        if(e.getSource() == enter)
        {
            // Dato che l'utente è loggato, passo come parametro "true"
            // e setto successivamente il nickname
            Impostazioni m = new Impostazioni(f, nickname);
            m.inizia();
        }
        else if(e.getSource() == back)
        {
            Sounds.stopSoundtrack();
            Menu m = new Menu(f);
            m.restart();
        }
        else
        {
            // In base a quale bottone dello shop clicco
            Db db = new Db();

            try {
                String query = "SELECT * FROM items";
                Statement stmt = (Statement) db.GetConn().createStatement();
                ResultSet items_set = stmt.executeQuery(query);

                int i = 0;
                int prezzo = 0;
                int credits = 0;
                int id_item = 0;
                int level_item = 0;

                while(items_set.next())
                {
                    if(e.getSource() == buys[i])
                    {
                        id_item = items_set.getInt("id");
                        prezzo = items_set.getInt("price");
                        level_item = items_set.getInt("level");
                        break;
                    }

                    i++;
                }

                String check_coins = "SELECT * FROM players WHERE nickname = '" + nickname + "'";
                Statement stmt2 = (Statement) db.GetConn().createStatement();
                ResultSet result = stmt2.executeQuery(check_coins);

                if(result.next())
                {
                    credits = result.getInt("credits");

                    if((int) Db.getColFromPlayers("level", nickname) >= level_item)
                    {
                        if(credits >= prezzo)
                        {
                            // Acquisto il prodotto e aggiorno i coins del player
                            int differenza = credits - prezzo;
                            String upd_coins = "UPDATE players SET credits = '" + differenza + "' WHERE nickname = '" + nickname + "'";
                            db.GetConn().createStatement().executeUpdate(upd_coins);

                            // Inserisco nel database l'oggetto acquistato al player
                            String insert_item = "INSERT INTO player_item (id, nickname) VALUES ('" + id_item + "', '" + nickname + "')";
                            db.GetConn().createStatement().executeUpdate(insert_item);

                            error.setForeground(Color.GREEN);
                            error.setText("Acquisto effettuato con successo!");

                            aggiornaPannello();
                            aggiornaShop();
                        }
                        else 
                        {
                            error.setForeground(Color.RED);
                            error.setText("Attenzione: non hai credito sufficiente");
                        }
                    }
                    else
                    {
                        error.setForeground(Color.RED);
                        error.setText("Attenzione: devi avere livello " + level_item + " per acquistare l'oggetto");
                    }
                }

                stmt.close();
                stmt2.close();

            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // Metodo per aggiornare la descrizione del pannello account
    private void aggiornaPannello()
    {
        try {
            accountText = "<html>"
                    + "Livello: " + Db.getColFromPlayers("level", nickname)
                    + "\n<br>"
                    + "Score: " + Db.getColFromPlayers("score", nickname)
                    + "\n<br>"
                    + "Partite giocate: " + Db.getColFromPlayers("played", nickname)
                    + "\n<br>\n<br>"
                    + "Crediti: " + Db.getColFromPlayers("credits", nickname)
                    + "</html>";
        } catch (SQLException ex) { 
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        desc.setText(accountText);
    }
    
    private void aggiornaShop()
    {
        Db db = new Db();

        try {
            String query = "SELECT * FROM items";
            Statement stmt = (Statement) db.GetConn().createStatement();
            ResultSet rows = stmt.executeQuery(query);
                
            p = new JPanel();
            p.setLayout(new GridLayout(rows.getRow(), 3, 10, 10));
            int i = 0;
            
            while(rows.next())
            {
                int id = rows.getInt("id");
                String nome_item = rows.getString("name");
                int prezzo = rows.getInt("price");
                String img_item = rows.getString("img_url");
                String livello = rows.getString("level");
                
                String q = "SELECT * FROM player_item WHERE id = '" + id + "' AND nickname = '" + nickname + "'";
                ResultSet rw = db.GetConn().createStatement().executeQuery(q);

                // Descrizione dell'oggetto
                String desc_item = "<html>" 
                        + nome_item
                        + "\n<br>"
                        + "Crediti: " + prezzo
                        + "\n<br>"
                        + "Livello: " + livello
                        + "</html>";

                label_descitem = new JLabel(desc_item);
                p.add(label_descitem);
                r_shop = new Rectangle();
                r_shop.setBounds(0, 0, 100, 48);
                imgs[i] = new CaricatoreImmagini(img_item, 100, 48);
                imgs[i].setBounds(0, 40, 100, 48);
                imgs[i].setOpaque(false);
                r_shop.add(imgs[i]);
                p.add(r_shop);

                buys[i] = new JButton("");
                String img_url = "";
                
                // Controllo se ho già acquistato l'oggetto 
                if(rw.next()) 
                    img_url = "/images/v_verde.png"; 
                else
                {
                    img_url = "/images/buy.png"; 
                    buys[i].addMouseListener(this);
                }
                
                buys[i].setIcon(new ImageIcon(getClass().getResource(img_url)));
                buys[i].setFont(GetFont.GetThisFont());
                buys[i].setBounds(0, 0, 80, 80);
                buys[i].setOpaque(false);
                buys[i].setContentAreaFilled(false);
                buys[i].setBorderPainted(false);
                buys[i].setVisible(true);
                p.add(buys[i]);

                i++;
            }
            
            bg_shop.removeAll();
            scroll_shop = new JScrollPane(p);
            bg_shop.add(scroll_shop);
            bg_shop.add(shop_title);
            bg_shop.add(error);
            scroll_shop.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scroll_shop.setSize(790, 420);
            scroll_shop.setLocation(20, 100);
            scroll_shop.setVisible(true);
        
            f.pack();

            stmt.close();
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
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
