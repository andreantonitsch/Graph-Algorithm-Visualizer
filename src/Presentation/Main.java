package Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
    
    // Methods
    
    public void init() {
        
        DNode node = new DNode();
        
        JPanel panel = new JPanel();
        panel.setSize(500, 500);
        panel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        panel.add(node);
        this.add(panel, BorderLayout.CENTER);
        
        node.repaint();
        
        this.setSize(800, 600);
        this.setTitle("Graph Testbed V1.0");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        repaint();
        
    }
    
    // Constructor
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            
            Main main = new Main();
            main.init();
            
        });
        
    }
    
}
