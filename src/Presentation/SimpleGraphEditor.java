package Presentation;

import javax.swing.*;

/**
   A program for editing UML diagrams.
*/
public class SimpleGraphEditor
{
   public static void main(String[] args)
   {
       
       JFrame frame = new GraphFrame(new Graph() {}, new Logic.Graph<String, Integer>());
       frame.setVisible(true);
      
   }
}

