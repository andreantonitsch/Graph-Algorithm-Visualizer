package Presentation;

import Logic.ObservableGraph;
import java.awt.*;
import javax.swing.*;

/**
   This frame shows the toolbar and the graph.
*/
public class GraphFrame extends JFrame
{
   /**
      Constructs a graph frame that displays a given graph.
      @param graph the graph to display
   */
   public GraphFrame(final Graph graph, final ObservableGraph ograph)
   {
      this.graph = graph;
      this.oGraph = ograph;
       
      setSize(FRAME_WIDTH, FRAME_HEIGHT);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      constructFrameComponents();
   }

   /**
      Constructs the tool bar and graph panel.
   */
   private void constructFrameComponents()
   {
      toolBar = new ToolBar(graph);
      
      panel = new GraphPanel(toolBar, graph);
      scrollPane = new JScrollPane(panel);
      
      algBar = new AlgorithmsBar(oGraph);
      algPane = new JScrollPane(algBar);
      
      this.add(toolBar, BorderLayout.NORTH);
      this.add(scrollPane, BorderLayout.CENTER);
      this.add(algPane, BorderLayout.EAST);
      
      panel.setFocusable(true);
      panel.requestFocusInWindow();
      
   }

   private Graph graph;
   ObservableGraph oGraph;
   private GraphPanel panel;
   private AlgorithmsBar algBar;
   private JScrollPane scrollPane;
   private JScrollPane algPane;
   private ToolBar toolBar;

   public static final int FRAME_WIDTH = 600;
   public static final int FRAME_HEIGHT = 400;
}
