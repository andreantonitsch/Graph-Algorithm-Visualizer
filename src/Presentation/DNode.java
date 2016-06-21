package Presentation;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class DNode extends JComponent {
    
    @Override
    public void paintComponent(Graphics g) {
        
        g.setColor(Color.RED);
        g.drawOval(100, 100, 100, 100);
        
        super.paintComponent(g);
        
    }
    
}
