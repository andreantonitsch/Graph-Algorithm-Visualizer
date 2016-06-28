package Presentation;

import Logic.RunnableAction;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**

   Código da interface gráfica baseado no exercício disponibilizado aos alunos de
   Técnicas de Programação pelo professor Júlio Machado
   
*/

public class AlgorithmsBar extends JPanel implements Observer {
    
    JButton nextR, prevR;
    ObservableGraphBackedGraph graph;
    
    public AlgorithmsBar(ObservableGraphBackedGraph graph) {
        
        super();
        this.graph = graph;
        graph.addObserver(this);
        
        initComponents();
        
    }
    
    private void initComponents() {
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        List<RunnableAction> actions = graph.getObservableGraph().getActions();
        
        for (RunnableAction a : actions) {
            
            JButton b = new JButton(a.getName());
            b.setToolTipText(a.getDescription());
            this.add(b);
            
            b.addActionListener((ActionEvent e) -> {
                
                runAction(a);
                
            });
            
        }
        
        nextR = new JButton(">>");
        prevR = new JButton("<<");
        
        nextR.addActionListener((ActionEvent e) -> { changeReport(2); });
        prevR.addActionListener((ActionEvent e) -> { changeReport(1); });
        
        this.add(nextR);
        this.add(prevR);
        
        changeReport(-1);
        
    }
    
    private void changeReport(int direction) {
        
        switch (direction) {
            
            case 0:
                graph.clearReport();
                break;
                
            case 1:
                graph.previousReport();
                break;
                
            case 2:
                graph.nextReport();
                break;
            
        }
        
        nextR.setEnabled(graph.hasNextReport());
        prevR.setEnabled(graph.hasPreviousReport());
        
    }
    
    private void runAction(RunnableAction a) {
        
        int aux = 0;
        String[] params = new String[a.getParameters().length];

        for (int i = 0; i < params.length; i++) {

            String value = JOptionPane.showInputDialog("Enter parameter " + (i+1) + ":");
            if (value != null) params[aux++] = value;
            else return;

        }

        try {

            changeReport(0);
            graph.getObservableGraph().runAction(a, params);
            
        }
        catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Error running action!" + ex.getMessage());

        }
        
    }

    @Override
    public void update(Observable o, Object arg) {
    
        changeReport(-1);
    
    }
    
}
