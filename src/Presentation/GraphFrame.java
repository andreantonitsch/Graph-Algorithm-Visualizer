package Presentation;

import java.awt.*;
import javax.swing.*;

public class GraphFrame extends JFrame
{
    private final Graph graph;
    
    private GraphPanel panel;
    private AlgorithmsBar algBar;
    private JScrollPane scrollPane;
    private JScrollPane algPane;
    private ToolBar toolBar;

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 600;
    
    public GraphFrame(final Graph graph)
    {
        
        this.graph = graph;
       
        super.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        constructFrameComponents();
        
   }

    private void constructFrameComponents()
    {
        
        toolBar = new ToolBar(graph);
      
        panel = new GraphPanel(toolBar, graph);
        scrollPane = new JScrollPane(panel);
      
        if (graph instanceof ObservableGraphBackedGraph) {
            
            ObservableGraphBackedGraph g = (ObservableGraphBackedGraph) graph;
            
            algBar = new AlgorithmsBar(g);
            algPane = new JScrollPane(algBar);
            this.add(algPane, BorderLayout.EAST);
            
        }
      
        this.add(toolBar, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
      
        panel.setFocusable(true);
        panel.requestFocusInWindow();
      
   }

}
