package monkey;

import fonts.GetFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login implements MouseListener, FocusListener {
    
    private JFrame f;
    private CaricatoreSfondo sfondo;
    private JTextField nickname;
    private JPasswordField password;
    private JButton enter, back, newaccount;
    private JLabel label_nickname, label_password, error;
    
    private String nick = "";
    private String pass = "";
    
    // Variabili del database
    private Db db;
    private Statement st;
    
    public Login(JFrame f)
    {
        this.f = f;
    }
    
    public void inizia()
    {
        f.getContentPane().removeAll();
        f.repaint();
        
        sfondo = new CaricatoreSfondo("sfondo1.jpg");   
        f.getContentPane().add(sfondo);
        
        //Impostiamo un layout libero
        sfondo.setLayout(null);
        
        f.pack();
        f.setVisible(true);
        
        new GetFont("Waverly.ttf", 20f);
        
        label_nickname = new JLabel("Nickname: ");
        label_nickname.setBounds(515, 170, 220, 40);
        label_nickname.setForeground(Color.WHITE);
        label_nickname.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        
        label_password = new JLabel("Password: ");
        label_password.setBounds(515, 280, 220, 40);
        label_password.setForeground(Color.WHITE);
        label_password.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        
        nickname = new JTextField();
        nickname.setBounds(515, 230, 220, 40);
        nickname.setHorizontalAlignment(JTextField.CENTER);
        nickname.setEditable(true);
        
        password = new JPasswordField();  
        password.setBounds(515, 340, 220, 40);
        password.setHorizontalAlignment(JTextField.CENTER);
        password.setEditable(true);
        
        enter = new JButton("Entra");
        enter.setBounds(515, 420, 220, 40);
        enter.addMouseListener(this);
        
        new GetFont("Nunito.ttf", 14f);
        error = new JLabel("");
        error.setBounds(400, 490, 500, 40);
        error.setForeground(Color.red);
        error.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        error.setHorizontalAlignment(SwingConstants.CENTER);
        error.setVisible(false);
        
        newaccount = new JButton("Crea account");
        newaccount.setBounds(515, 560, 220, 40);
        newaccount.addMouseListener(this);
        newaccount.setVisible(false);
        
        back = new JButton("");
        back.setIcon(new ImageIcon(getClass().getResource("/images/arrow_left.png")));
        back.setFont(GetFont.GetThisFont());
        back.setBounds(20, 20, 40, 40);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.addMouseListener(this);
        back.setVisible(true);
        
        sfondo.add(nickname);
        sfondo.add(password);
        sfondo.add(enter);
        sfondo.add(newaccount);
        sfondo.add(label_nickname);
        sfondo.add(label_password);
        sfondo.add(error);
        sfondo.add(back);
        
        db = new Db();
        st = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        enter.setEnabled(false);
        error.setText("Connessione ...");
        error.setForeground(Color.green);
        error.setVisible(true);
        
        if(e.getSource() == enter)
        {
            char[] keyword = password.getPassword();
            pass = String.valueOf(keyword);
            nick = nickname.getText();
            
            ResultSet rs;
            
            String checkForUser = "SELECT * FROM players WHERE nickname = '" + nick + "'";
            String checkUserAndPass = "SELECT * FROM players WHERE nickname = '" + nick + "' AND password = '" + pass + "'";
            
            if(!"".equals(nick) && !"".equals(pass))
            {
                try {
                    st = (Statement) db.GetConn().createStatement();
                    rs = st.executeQuery(checkForUser);

                    if(rs.next())
                    {
                        try {
                            st = (Statement) db.GetConn().createStatement();
                            rs = st.executeQuery(checkUserAndPass);

                            if(rs.next())
                            {
                                // Se nickname e password sono corretti
                                // Entro nell'account del player
                                Account a = new Account(f, nick);
                            } 
                            else 
                            {
                                error.setText("Errore: la password dell'account " + nick + " è errata!");
                                error.setForeground(Color.red);
                                error.setVisible(true);
                                enter.setEnabled(true);
                                newaccount.setVisible(false);
                            }
                        } catch (SQLException ex){
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        }   
                    }
                    else 
                    {
                        error.setText("<html>Errore: il nome utente non esiste.\n<br>Creare un nuovo account?</html>");
                        error.setForeground(Color.red);
                        error.setVisible(true);
                        enter.setEnabled(true);
                        newaccount.setVisible(true);
                    }

                    st.close();
                    
                } 
                catch (SQLException ex)
                {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }   
            }
            else 
            {
                error.setText("Attenzione: compila tutti i campi");
                error.setForeground(Color.red);
                error.setVisible(true);
                enter.setEnabled(true);
                newaccount.setVisible(false);
            }
        } 
        else if(e.getSource() == newaccount)
        {
            String newUserData = "INSERT INTO players (nickname, password, level, score, played, credits) VALUES ('"+nick+"', '"+pass+"', '1', '0', '0', '0')";
            
            try {
                db.GetConn().createStatement().executeUpdate(newUserData);
                // Entro nell'account del player
                Account a = new Account(f, nick);
            } 
            catch (SQLException ex)
            {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        else if(e.getSource() == back)
        {
            Sounds.stopSoundtrack();
            Menu m = new Menu(f);
            m.restart();
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

    @Override
    public void focusGained(FocusEvent e) {
        if(e.getSource() == nickname || e.getSource() == password)  
            error.setVisible(false);
    }

    @Override
    public void focusLost(FocusEvent e) {}
    
}
