package Logic;

import java.util.Arrays;

public class Report<N, E extends Comparable> {

    public static final int ACTION_PAINT = 0;
    public static final int ACTION_UNPAINT = 1;

    final int actionType;
    final String[] nodes;
    final String[] edges;

    public Report(int actionType, String[] nodes, String[] edges) {

        this.actionType = actionType;
        this.nodes = nodes;
        this.edges = edges;

    }

    public int getAction() {

        return actionType;

    }

    public String[] getNodes() {

        return Arrays.copyOf(nodes, nodes.length);

    }

    public String[] getEdges() {

        return Arrays.copyOf(edges, edges.length);

    }

    public Report getInverse() {
        
        int i = actionType;
        
        if (actionType == ACTION_PAINT) i = ACTION_UNPAINT;
        if (actionType == ACTION_UNPAINT) i = ACTION_PAINT;
        
        Report inverse = new Report(i, nodes, edges);
        return inverse;
        
    }
    
}
