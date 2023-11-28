package monkey;

import fonts.GetFont;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Regole implements ActionListener
{    
    private JFrame f;
    private JLabel titolo, regole;
    private JButton hocapito;
    
    public Regole(JFrame f)
    {
        this.f = f;
    }
    
    public void inizia()
    {
        f.getContentPane().removeAll();
        f.repaint();
        
        CaricatoreSfondo sfondo = new CaricatoreSfondo("sfondo.jpg");   
        f.getContentPane().add(sfondo);
        
        //Impostiamo un layout libero
        sfondo.setLayout(null);
        
        titolo = new JLabel("Regole di Gioco");
        new GetFont("Waverly.ttf", 28f);
        titolo.setFont(GetFont.GetThisFont());
        titolo.setForeground(new java.awt.Color(255, 255, 255));
        titolo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titolo.setBounds(197, 100, 900, 50);
        
        regole = new JLabel("TESTO DELLE REGOLEdf sd fsd fsd fsf sdfdsfsdfsdfsd fsdfsdfsdfsd fsdfsdfs dfsdfsdfsdfsdfsd fsdfsdfsd"
                + "dfsd fsdfsdfsdf sdfsdfsdfsdf sdfsdfsdfsd fsdfsdfsdf sdfsdfsdfsdfsdfsd fsdf sdf sdfdsfsdfds");
        new GetFont("Waverly.ttf", 23f);
        regole.setFont(GetFont.GetThisFont());
        regole.setForeground(new java.awt.Color(255, 255, 255));
        regole.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        regole.setBounds(197, 120, 900, 400);
        
        hocapito = new JButton("Ho capito");
        hocapito.setBackground(new java.awt.Color(204, 204, 204));
        new GetFont("Waverly.ttf", 20f);
        hocapito.setFont(GetFont.GetThisFont().deriveFont(Font.BOLD));
        hocapito.setBounds(480, 570, 300, 50);
        
        sfondo.add(titolo);
        sfondo.add(regole);
        sfondo.add(hocapito);
        
        hocapito.addActionListener(this);

        f.pack();
        f.setVisible(true);
    }

    //Sovrasciviamo il metodo che indica quando il bottone è premuto
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        //Prendo il bottone che ha scatenato l'evento
        Object source = e.getSource();
        //A seconda del bottone eseguo codice differente
        if(source.equals(hocapito)) 
        {
            Sounds click = new Sounds("click.wav");
            click.start();
            Menu m = new Menu(f);
            m.inizia();
        }
    }
}