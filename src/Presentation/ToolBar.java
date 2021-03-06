package Presentation;

import Presentation.Node;
import Presentation.Edge;
import Presentation.Graph;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**

   Código da interface gráfica baseado no exercício disponibilizado aos alunos de
   Técnicas de Programação pelo professor Júlio Machado
   
*/

/**
   A tool bar that contains node and edge prototype icons.
   Exactly one icon is selected at any time.
*/
public class ToolBar extends JPanel
{
   /**
      Constructs a tool bar with no icons.
     * @param graph
   */
   public ToolBar(Graph graph)
   {
      group = new ButtonGroup();
      tools = new ArrayList<Object>();

      JToggleButton grabberButton = new JToggleButton(new
         Icon()
         {
            public int getIconHeight() { return BUTTON_SIZE; }
            public int getIconWidth() { return BUTTON_SIZE; }
            public void paintIcon(Component c, Graphics g,
               int x, int y)
            {
               Graphics2D g2 = (Graphics2D) g;
               GraphPanel.drawGrabber(g2, x + OFFSET, y + OFFSET);
               GraphPanel.drawGrabber(g2, x + OFFSET, y + BUTTON_SIZE - OFFSET);
               GraphPanel.drawGrabber(g2, x + BUTTON_SIZE - OFFSET, y + OFFSET);
               GraphPanel.drawGrabber(g2, x + BUTTON_SIZE - OFFSET, y + BUTTON_SIZE - OFFSET);
            }
         });
      group.add(grabberButton);
      add(grabberButton);
      grabberButton.setSelected(true);
      tools.add(null);

      add("Node");
      add("Edge");
      
   }

   /**
      Gets the node or edge prototype that is associated with
      the currently selected button
      @return a Node or Edge prototype
   */
   public Object getSelectedTool()
   {
      int i=0;
      for (Object o : tools)
      {
         JToggleButton button = (JToggleButton) getComponent(i++);
         if (button.isSelected()) return o;
      }
      return null;
   }

   /**
      Adds a node to the tool bar.
     * @param text
   */
   public void add(String text)
   {
      JToggleButton button = new JToggleButton(new
         Icon()
         {
            @Override
            public int getIconHeight() { return BUTTON_SIZE; }
            
            @Override
            public int getIconWidth() { return BUTTON_SIZE; }
            
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y)
            {
                
                FontMetrics fm = g.getFontMetrics();
                
               double width = fm.stringWidth(text);
               double height = fm.getHeight();
               Graphics2D g2 = (Graphics2D) g;
               double scaleX = (BUTTON_SIZE - OFFSET)/ width;
               double scaleY = (BUTTON_SIZE - OFFSET)/ height;
               double scale = Math.min(scaleX, scaleY);

               float xrel = (float)((BUTTON_SIZE - OFFSET) - (width / 2));
               float yrel = (float)((BUTTON_SIZE - OFFSET) - (height / 2));
               
               AffineTransform oldTransform = g2.getTransform();
               g2.translate(x, y);
               g2.scale(scale, scale);
               g2.translate(Math.max((height - width) / 2, 0), Math.max((width - height) / 2, 0));
               g2.setColor(Color.black);
               g2.drawString(text, xrel, yrel);
               g2.setTransform(oldTransform);
            }
            
         });
      group.add(button);
      add(button);
      tools.add(text);
   }

   private ButtonGroup group;
   private ArrayList<Object> tools;

   private static final int BUTTON_SIZE = 25;
   private static final int OFFSET = 4;
}
