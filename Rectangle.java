package monkey;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Rectangle extends Shape
{
    private int r = 255, g = 255, b = 255; //Default bianco
    private int opacity = 0;
    
    public Rectangle(){}

    @Override
    public void paintComponent(Graphics g)
    {
        Dimension dim = getSize();
        g.setColor(new Color(this.r, this.g, this.b, this.opacity));
        g.fillRect(0, 0, dim.width, dim.height);
    }
    
    public void setBackground(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public void setOpacity(int opacity)
    {
        this.opacity = opacity;
    }
}
