package Presentation;

import Logic.ObservableGraph;
import javax.swing.*;

/**
   A program for editing UML diagrams.
*/
public class SimpleGraphEditor
{
   public static void main(String[] args) {
       
       Logic.Graph<String, Integer> g = new Logic.Graph<String, Integer>();
       ObservableGraph<String, Integer> og = g;
       
       JFrame frame = new GraphFrame(new ObservableGraphBackedGraph(og));
       frame.setVisible(true);
      
   }
}

