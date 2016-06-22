package Presentation;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class TextEdge extends LineEdge {
    
    String text;
    
    public TextEdge(String text) {
        
        super();
        
        this.text = text;
        
    }
    
    @Override
    public void draw(Graphics2D g2) {
        
        super.draw(g2);
        
        Rectangle2D rect = getBounds(g2);
        FontMetrics fm = g2.getFontMetrics();
        
        double x = rect.getX() + rect.getWidth()/2 ;//- fm.stringWidth(text)/2;
        double y = rect.getY() + rect.getHeight()/2 ;//- fm.getHeight()/2;
        
        AffineTransform orig = g2.getTransform();
        
        g2.rotate(angle(), x, y);
        g2.drawString(text, (int)(x), (int)(y));
        g2.setTransform(orig);
        
    }
    
}
