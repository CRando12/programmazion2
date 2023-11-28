package monkey;

import java.util.Random;

public class Frutta extends CadutaFrutta 
{
    private CaricatoreImmagini img_frutta;
    private Rectangle frutta;
    private String img_name;
    
    public Frutta(String img_name, int width, int height, double velocita)      
    {
        this.img_name = img_name;
        this.width = width;
        this.height = height;
        this.velocita = velocita;
        this.genera();
    }
    
    @Override 
    public void genera()
    {
        Random rnd = new Random();
                             
        this.x = rnd.nextInt(Monkey.getLarghezza()-50); // Posizione di spawn frutta (x)
        this.y = 50; // Posizione di spawn frutta (y)
        
        img_frutta = new CaricatoreImmagini(img_name, this.width, this.height);
        img_frutta.setBounds(0, 0, this.width, this.height);
        img_frutta.setOpaque(false);
        
        frutta = new Rectangle();
        frutta.setBounds(this.x, this.y, this.width, this.height);
        frutta.add(img_frutta);
        frutta.setVisible(true);
    }
    
    @Override
    public void aggiorna()
    {
        frutta.setBounds(this.x, this.y, this.width, this.height);
    }
    
    @Override
    public int getX()
    {
        return this.x;
    }
    
    @Override
    public int getY()
    {
        return this.y;
    }
    
    @Override
    public void setX(int x)
    {
        this.x = x;
    }
    
    @Override
    public void setY(int y)
    {
        this.y = y;
    }
    
    @Override
    public double getVelocita()
    {
        return this.velocita;
    }
    
    public Rectangle getFrutta()
    {
        return this.frutta;
    }
}