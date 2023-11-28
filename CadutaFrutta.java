package monkey;

import java.awt.image.BufferedImage;

public abstract class CadutaFrutta {
    
    protected int x;
    protected int y = 0;
    protected int width;
    protected int height;
    protected double velocita;
    
    public void genera() {}
    
    public void aggiorna(){}
    
    public int getX()
    { 
        return 0; 
    }
    
    public int getY()
    { 
        return 0; 
    }
    
    public void setX(int x){}
    
    public void setY(int y){}
    
    public double getVelocita()
    {
        return 0;
    }
    
}
