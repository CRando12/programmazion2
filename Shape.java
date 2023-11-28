package monkey;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

public abstract class Shape extends JComponent
{
    private static final long serialVersionUID = 1L;

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
}



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 100);
    }



    @Override
    public Dimension getMaximumSize() {
        return new Dimension(1280, 720);
    }

    @Override
    public abstract void paintComponent(Graphics g);
}