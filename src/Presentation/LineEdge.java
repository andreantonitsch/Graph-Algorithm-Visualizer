package Presentation;

import Presentation.AbstractEdge;
import java.awt.*;
import java.awt.geom.*;

/**

   Código da interface gráfica baseado no exercício disponibilizado aos alunos de
   Técnicas de Programação pelo professor Júlio Machado
   
*/

/**
   An edge that is shaped like a straight line.
*/
public class LineEdge extends AbstractEdge
{
   @Override
   public void draw(Graphics2D g2)
   {
      g2.draw(getConnectionPoints());
   }

   @Override
   public boolean contains(Point2D aPoint)
   {
       
      final double MAX_DIST = 2;
      return getConnectionPoints().ptSegDist(aPoint) 
         < MAX_DIST;
      
   }
}
