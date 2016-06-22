package Presentation;

import Logic.ObservableGraph;
import Logic.RunnableAction;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class AlgorithmsBar extends JPanel {
    
    ObservableGraph graph;
    
    public AlgorithmsBar(ObservableGraph graph) {
        
        super();
        this.graph = graph;
        
        initComponents();
        
    }
    
    private void initComponents() {
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        List<RunnableAction> actions = graph.getActions();
        
        for (RunnableAction a : actions) {
            
            JButton b = new JButton(a.getName());
            b.setToolTipText(a.getDescription());
            this.add(b);
            
        }
        
    }
    
}
