package Presentation;

import Logic.GraphObserver;
import Logic.ObservableGraph;
import Logic.Report;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ObservableGraphBackedGraph extends Graph implements GraphObserver {
    
    // Variables
    
    int currentReport = -1;
    ObservableGraph graph;
    ArrayList<Report> reports;
    
    // Constructor
    
    public ObservableGraphBackedGraph(ObservableGraph graph) {
        
        super();
        
        this.graph = graph;
        this.graph.addObserver(this);
        
        reports = new ArrayList<>();
        
    }
    
    // Graph
    
    public ObservableGraph getObservableGraph() {
        
        return graph;
        
    }
    
    @Override
    public boolean add(String id, Node n, Point2D p) {
        
        if (!(n instanceof TextNode)) return false;
        
        TextNode tn = (TextNode) n;
        String tn_value = tn.getText();
        
        try {

            String result = graph.addNode(tn_value);
            
            if (result != null) return super.add(result, n, p);
            else return false;
            
        }
        catch (IllegalArgumentException e) {
            
            return false;
            
        }
        
    }
    
    @Override
    public void removeNode(Node n) {
        
        if (!(n instanceof TextNode)) return;
        
        TextNode tn = (TextNode) n;
        String node_text = tn.getText();
        
        try {

            boolean result = graph.removeNode(node_text);
            if (result) super.removeNode(n);
            
        }
        catch (IllegalArgumentException e) {
            
            // Do Nothing
            
        }
        
    }

    @Override
    public boolean connect(String id, Edge e, Point2D p1, Point2D p2) {
        
        if (!(e instanceof TextEdge)) return false;
        
        TextEdge te = (TextEdge) e;
        
        try {

            String result = graph.addEdge(te.getFrom(), te.getTo(), te.getText());
            
            if (result != null) return super.connect(result, e, p1, p2);
            else return false;
            
        }
        catch (IllegalArgumentException ex) {
            
            return false;
            
        }
        
    }

    @Override
    public void removeEdge(Edge e) {
        
        if (!(e instanceof TextEdge)) return;
        
        TextEdge te = (TextEdge) e;
        
        try {

            boolean result = graph.removeEdge(te.getFrom(), te.getTo(), te.getText());
            if (result) super.removeEdge(e);
            
        }
        catch (IllegalArgumentException ex) {
            
            // Do Nothing
            
        }
        
    }
    
    // Reports
    
    public void nextReport() {
        
        if (currentReport+1 >= reports.size()) return;
        
        currentReport++;
        applyReport(reports.get(currentReport));
        
    }
    
    public void previousReport() {
        
        if (currentReport-1 < 0) return;
        
        applyReport(reports.get(currentReport).getInverse());
        currentReport--;
        applyReport(reports.get(currentReport));
        
    }
    
    public void clearReport() {
        
        for (Node n : nodes) {
            if (n instanceof TextNode) {
            
                ((TextNode) n).setPainted(false);
                
            }
        }
        
        for (Edge e : edges) {
            if (e instanceof TextEdge) {
            
                ((TextEdge) e).setPainted(false);
                
            }
        }
        
        reports.clear();
        currentReport = -1;
        
    }
    
    public boolean hasNextReport() {
        
        return currentReport+1 < reports.size();
        
    }
    
    public boolean hasPreviousReport() {
        
        return currentReport-1 >= 0;
        
    }
    
    private void applyReport(Report report) {
        
        switch (report.getAction()) {

            case Report.ACTION_PAINT:
                
                for (String s : report.getNodes()) {
                    
                    Node n = findNode(s);
                    if (n == null) continue;
                    
                    if (n instanceof TextNode) {
                        
                        ((TextNode) n).setPainted(true);
                        
                    }
                    
                }
                
                for (String s : report.getEdges()) {
                    
                    Edge e = findEdge(s);
                    if (e == null) continue;
                    
                    if (e instanceof TextEdge) {
                        
                        ((TextEdge) e).setPainted(true);
                        
                    }
                    
                }
                
                this.setChanged();
                
                break;

            case Report.ACTION_UNPAINT:
                
                for (String s : report.getNodes()) {
                    
                    Node n = findNode(s);
                    if (n instanceof TextNode) {
                        
                        ((TextNode) n).setPainted(false);
                        
                    }
                    
                }
                
                for (String s : report.getEdges()) {
                    
                    Edge e = findEdge(s);
                    if (e instanceof TextEdge) {
                        
                        ((TextEdge) e).setPainted(false);
                        
                    }
                    
                }
                
                this.setChanged();
                
                break;

        }
        
        this.notifyObservers();
        
    }
    
    // Graph Observer
    
    @Override
    public void update(Report report) {

        reports.add(report);
        
        if (currentReport == -1) nextReport();
        
        this.setChanged();
        this.notifyObservers();

    }

}
