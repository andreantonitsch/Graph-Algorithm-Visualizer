package Presentation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**

   Código da interface gráfica baseado no exercício disponibilizado aos alunos de
   Técnicas de Programação pelo professor Júlio Machado
   
*/

public class TextEdge extends LineEdge {
    
    boolean painted;
    String from, to, text;
    
    public TextEdge(String from, String to, String text) {
        
        super();
        
        this.from = from;
        this.to = to;
        this.text = text;
        
    }
    
    @Override
    public void draw(Graphics2D g2) {
        
        Color c = g2.getColor();
        Stroke s = g2.getStroke();
        
        if (painted) {
            
            g2.setColor(Color.MAGENTA);
            g2.setStroke(new BasicStroke(3));
            
        } 
        
        super.draw(g2);
        
        Rectangle2D rect = getBounds(g2);
        FontMetrics fm = g2.getFontMetrics();
        
        double x = rect.getX() + rect.getWidth()/2 ;
        double y = rect.getY() + rect.getHeight()/2 ;
        
        // Ortogonal Vector
   
        double angle = angle();
        double angle_variation = Math.abs(angle) > (Math.PI / 2) && Math.abs(angle) < ((3 * Math.PI) / 2) ? -1 : 1;
        
        Line2D v = getConnectionPoints();
        
        double vx = v.getX2() - v.getX1();
        double vy = v.getY2() - v.getY1();
        double vm = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
                
        vx = vx / vm;
        vy = vy / vm;
        
        double ovx = vy;
        double ovy = -1 * vx;
        
        double vcos = Math.cos(angle);
        double vsin = Math.sin(angle);
        
        double xproj = (fm.stringWidth(text) / 2) * vcos;
        double yproj = (fm.stringWidth(text) / 2) * vsin;
        
        if (vx != 0) x -= angle_variation * vx * (xproj / vx);
        if (vy != 0) y -= angle_variation * vy * (yproj / vy);
        
        x += ovx * 12;
        y += ovy * 12;
        
        // End Ortogonal Vector
        
        AffineTransform orig = g2.getTransform();
        if (angle_variation == -1) angle += Math.PI;
        
        g2.rotate(angle, x, y);
        g2.drawString(text, (float)x, (float)y);
        g2.setTransform(orig);
        
        double destX = v.getX2();
        double destY = v.getY2();
        
        Point2D point = new Point2D.Double(destX - (10 * vcos), destY - (10 * vsin));
        
        g2.rotate(Math.PI / 4, destX, destY);
        g2.draw(new Line2D.Double(v.getP2(), point));
        g2.setTransform(orig);
        
        g2.rotate(-1 * Math.PI / 4, destX, destY);
        g2.draw(new Line2D.Double(v.getP2(), point));
        
        g2.setColor(c);
        g2.setStroke(s);
        g2.setTransform(orig);
        
    }
    
    public String getFrom() {
        
        return from;
        
    }
    
    public String getTo() {
        
        return to;
        
    }
    
    public String getText() {
        
        return text;
        
    }
    
    public void setPainted(boolean painted) {
        
        this.painted = painted;
        
    }
    
}
