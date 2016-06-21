
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class TextNode extends CircleNode {
    
    String text;
    
    public TextNode(String text) {
        
        super(Color.WHITE, 40);
        
        this.text = text;
        
    }
    
    @Override
    public void draw(Graphics2D g2) {
        
        super.draw(g2);
        
        FontMetrics fm = g2.getFontMetrics();
        
        double x = getBounds().getX() + (getBounds().getWidth()/2) - (fm.stringWidth(text)/2);
        double y = getBounds().getY() + (getBounds().getHeight()/2) + (fm.getHeight()/4);
        
        g2.drawString(text, (int)(x), (int)(y));
        
    }
    
}
