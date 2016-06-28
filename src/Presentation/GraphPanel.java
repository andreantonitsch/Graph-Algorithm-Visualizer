package Presentation;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**

   Código da interface gráfica baseado no exercício disponibilizado aos alunos de
   Técnicas de Programação pelo professor Júlio Machado
   
*/

public class GraphPanel extends JComponent implements Observer {

    // Static
    
    private static final Color PURPLE = new Color(0.7f, 0.4f, 0.7f);
    public static void drawGrabber(Graphics2D g2, double x, double y) {
        
        final int SIZE = 5;
        
        Color oldColor = g2.getColor();
        g2.setColor(PURPLE);
        g2.fill(new Rectangle2D.Double(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE));
        g2.setColor(oldColor);
        
    }
    
    // Variables
    
    private Graph graph;
    private ToolBar toolBar;
    private Point2D lastMousePoint;
    private Point2D rubberBandStart;
    private Point2D dragStartPoint;
    private Rectangle2D dragStartBounds;
    private Object selected;

    // Constructor
    
    public GraphPanel(ToolBar aToolBar, Graph aGraph) {
        
        toolBar = aToolBar;
        graph = aGraph;

        aGraph.addObserver(this);
        
        super.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    removeSelected();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}

        });

        super.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {

                requestFocusInWindow();

                Point2D mousePoint = event.getPoint();
                Node n = graph.findNode(mousePoint);
                Edge e = graph.findEdge(mousePoint);
                Object tool = toolBar.getSelectedTool();
                
                if (tool == null) // select
                {
                    if (e != null) {
                        
                        selected = e;
                        
                    } else if (n != null) {
                        
                        selected = n;
                        dragStartPoint = mousePoint;
                        dragStartBounds = n.getBounds();
                        
                    } else {
                        
                        selected = null;
                        
                    }
                } 
                else if (tool.toString().equals("Node")) {
                    
                    Node newNode;

                    String text = JOptionPane.showInputDialog("Node value: ");
                    if (text != null) {

                        newNode = new TextNode(text);

                        boolean added = graph.add("", newNode, mousePoint);
                        
                        if (added) {
                            
                            selected = newNode;
                            dragStartPoint = mousePoint;
                            dragStartBounds = newNode.getBounds();
                            
                        } else if (n != null) {
                            
                            selected = n;
                            dragStartPoint = mousePoint;
                            dragStartBounds = n.getBounds();
                            
                        }
                    }
                } 
                else if (tool.toString().equals("Edge")) {
                    
                    selected = null;
                    
                    if (n != null) {
                        
                        rubberBandStart = mousePoint;
                        
                    }
                    
                }
                
                lastMousePoint = mousePoint;
                repaint();
                
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                
                Object tool = toolBar.getSelectedTool();
                
                if (rubberBandStart != null) {
                    
                    Point2D mousePoint = event.getPoint();
                    String text = JOptionPane.showInputDialog("Edge value: ");
                    
                    if (text != null) {

                        Node n1 = graph.findNode(rubberBandStart);
                        Node n2 = graph.findNode(mousePoint);
                        
                        if (n1 != null && n2 != null && n1 instanceof TextNode && n2 instanceof TextNode) {
                        
                            TextNode tn1 = (TextNode) n1;
                            TextNode tn2 = (TextNode) n2;
                            
                            Edge newEdge = (Edge) new TextEdge(tn1.getText(), tn2.getText(), text);
                            
                            if (graph.connect("", newEdge, rubberBandStart, mousePoint)) {
                            
                                selected = newEdge;
                            
                            }
                        
                        }

                    }
                    
                }

                lastMousePoint = null;
                dragStartBounds = null;
                rubberBandStart = null;
                
                validate();
                repaint();
                
            }
        
        });

        super.addMouseMotionListener(new MouseMotionAdapter() {
            
            @Override
            public void mouseDragged(MouseEvent event) {
                
                Point2D mousePoint = event.getPoint();
                
                if (dragStartBounds != null) {
                    
                    if (selected instanceof Node) {
                        
                        Node n = (Node) selected;
                        Rectangle2D bounds = n.getBounds();
                        
                        n.translate(
                                dragStartBounds.getX() - bounds.getX()
                                + mousePoint.getX() - dragStartPoint.getX(),
                                dragStartBounds.getY() - bounds.getY()
                                + mousePoint.getY() - dragStartPoint.getY());
                        
                    }
                    
                }
                
                lastMousePoint = mousePoint;
                repaint();
                
            }
        });

    }

    // Actions
    
    public void removeSelected() {
        
        if (selected instanceof Node) {
            
            graph.removeNode((Node) selected);
            
        } else if (selected instanceof Edge) {
            
            graph.removeEdge((Edge) selected);
            
        }
        
        selected = null;
        repaint();
        
    }

    @Override
    public Dimension getPreferredSize() {

        Rectangle2D bounds = graph.getBounds((Graphics2D) getGraphics());
        return new Dimension((int) bounds.getMaxX(), (int) bounds.getMaxY());

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle2D bounds = getBounds();
        Rectangle2D graphBounds = graph.getBounds(g2);
        graph.draw(g2);

        if (selected instanceof Node) {
            Rectangle2D grabberBounds = ((Node) selected).getBounds();
            drawGrabber(g2, grabberBounds.getMinX(), grabberBounds.getMinY());
            drawGrabber(g2, grabberBounds.getMinX(), grabberBounds.getMaxY());
            drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getMinY());
            drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getMaxY());
        }

        if (selected instanceof Edge) {
            Line2D line = ((Edge) selected).getConnectionPoints();
            drawGrabber(g2, line.getX1(), line.getY1());
            drawGrabber(g2, line.getX2(), line.getY2());
        }

        if (rubberBandStart != null) {
            Color oldColor = g2.getColor();
            g2.setColor(PURPLE);
            g2.draw(new Line2D.Double(rubberBandStart, lastMousePoint));
            g2.setColor(oldColor);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
    
        if (o == graph) {
            
            repaint();
            
        }
    
    }

}
