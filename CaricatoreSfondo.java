package monkey;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JPanel;


public class CaricatoreSfondo extends JPanel
{
    //Definiamo le variabili che ci servono
    private Image img = null;
    private int x=1280;
    private int y=720;
    
    public CaricatoreSfondo(String file)
    {
        String path = "/images/" + file;
        //Recuperiamo il toolkit
        Toolkit toolkit = getToolkit();
        //Recuperiamo l'url del file
        URL url = getClass().getResource(path);
        if (url != null)
        {
            //Si chiede a toolkit di creare l'immagine
            img = toolkit.createImage(url);
            loadImage(img);
        }
    }
    
    public CaricatoreSfondo(int x)
    {
        
    }
    
    public CaricatoreSfondo(String file, int x, int y)
    {
        this.x = x;
        this.y = y;
        String path = "/images/" + file;
        //Recuperiamo il toolkit
        Toolkit toolkit = getToolkit();
        //Recuperiamo l'url del file
        URL url = getClass().getResource(path);
        if (url != null)
        {
            //Si chiede a toolkit di creare l'immagine
            img = toolkit.createImage(url);
            loadImage(img);
        }
    }
    
    private void loadImage(Image img) 
    {
        try 
        {
            MediaTracker track = new MediaTracker(this);
            track.addImage(img, 0);
            track.waitForID(0);
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
   
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //Recupera i bordi del container
	Insets i = getInsets();
        //Disegna l'immagine nel container
        g.drawImage(img, i.left, i.top, x, y, this);
    }
}
