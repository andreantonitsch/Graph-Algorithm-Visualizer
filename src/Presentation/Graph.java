package Presentation;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;

public abstract class Graph extends Observable implements Serializable {

    protected final Map<String, Node> id_node;
    protected final Map<String, Edge> id_edge;
    
    protected final ArrayList<Node> nodes;
    protected final ArrayList<Edge> edges;

    public Graph() {
        
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        
        id_node = new HashMap<>();
        id_edge = new HashMap<>();
        
    }

    public boolean connect(String id, Edge e, Point2D p1, Point2D p2) {
 
        Node n1 = findNode(p1);
        Node n2 = findNode(p2);

        if (n1 != null && n2 != null) {
            
            id_edge.put(id, e);
            
            e.connect(n1, n2);
            edges.add(e);
            return true;
            
        }

        return false;
    }

    public boolean add(String id, Node n, Point2D p) {
        
        id_node.put(id, n);
        
        Rectangle2D bounds = n.getBounds();
        n.translate(p.getX() - bounds.getX(), p.getY() - bounds.getY());
        nodes.add(n);
        
        return true;
    }

    public Node findNode(Point2D p) {
        
        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node n = nodes.get(i);
            if (n.contains(p)) {
                return n;
            }
        }
        
        return null;
    }

    public Node findNode(String s) {

        if (id_node.containsKey(s)) return id_node.get(s);
        return null;

    }

    public Edge findEdge(String s) {
        
        if (id_edge.containsKey(s)) return id_edge.get(s);
        return null;
        
    }
    
    public Edge findEdge(Point2D p) {
        for (int i = edges.size() - 1; i >= 0; i--) {
            Edge e = edges.get(i);
            if (e.contains(p)) {
                return e;
            }
        }
        return null;
    }

    public void draw(Graphics2D g2) {
        for (Node n : nodes) {
            n.draw(g2);
        }

        for (Edge e : edges) {
            e.draw(g2);
        }

    }

    public void removeNode(Node n) {
        
        for (int i = edges.size() - 1; i >= 0; i--) {
            
            Edge e = edges.get(i);
            
            if (e.getStart() == n || e.getEnd() == n) {
                
                removeEdge(e);
                
            }
            
        }
        nodes.remove(n);
    }

    public void removeEdge(Edge e) {
        edges.remove(e);
    }

    public Rectangle2D getBounds(Graphics2D g2) {
        Rectangle2D r = null;
        for (Node n : nodes) {
            Rectangle2D b = n.getBounds();
            if (r == null) {
                r = b;
            } else {
                r.add(b);
            }
        }
        for (Edge e : edges) {
            r.add(e.getBounds(g2));
        }
        return r == null ? new Rectangle2D.Double() : r;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

}
