import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Set;
import java.util.PriorityQueue;

public class Graph<N,E extends Comparable> implements Iterable<N>, ObservableGraph<N,E> {
    
    // Variables
    
    static int verfication_aux;
    
    Map<N, Node> nodes;
    Map<String,Edge> edges;
    Stack<Integer> ids;
    
    Map<Integer, Node> nodeIds;

    // <editor-fold desc="Observable Graph">
    
    List<GraphObserver> observers;
    
    @Override
    public void addObserver(GraphObserver g) {
        
        if (observers == null) observers = new ArrayList<>();
        observers.add(g);
    
    }

    @Override
    public void removeObserver(GraphObserver g) {
        
        if (observers == null) return;
        observers.remove(g);
    
    }

    @Override
    public List<RunnableAction> getActions() {
    
        List<RunnableAction> actions = new ArrayList<>();
        
        actions.add(new RunnableAction("Dijkstra",
                                       "Finds the shortest path between a nodes and every other one in the graph",
                                        new ParameterType[] { ParameterType.NODE } ));
    
        return actions;
        
    }

    @Override
    public void runAction(RunnableAction r, Object... params) {
    
        // Check parameters
        
        ParameterType[] types = r.getParameters();
        
        for (int i = 0; i < types.length; i++) {
            
            //if (types[i] == ParameterType.NODE && !(params[i] instanceof N)) {}
            
        }
        
        // Run action
        
        switch (r.getName()) {
            
            case "Dijkstra":
                
                // Check parameters
                
                // 
                
                break;
            
        }
    
    }
    
    // </editor-fold>
    
    // <editor-fold desc="Old">
    
    // Node class
    
    class Node {
        
        public N value;
        public int id;
        public int verification_code;
        
        public Node(N value, int id, int verification_code) {
            
            this.value = value;
            this.id = id;
            this.verification_code = verification_code;
            
        }
        
    }
    
    // Edge class
    
    class Edge {
        
        E label;
        int fromVC, toVC;
        
        public Edge(int fromvc, int tovc, E label) {
            
            this.fromVC = fromvc;
            this.toVC = tovc;
            this.label = label;
            
        }
        
    }
    
    // ID Generator
    
    private int getId() {
        
        int i = ids.pop();
        if (ids.isEmpty()) ids.push(i+1);
        
        return i;
        
    }
    
    private void returnId(int id) {
        
        ids.push(id);
        
    }
    
    // Verification Code
    
    private int getVerificationCode() {
        
        return verfication_aux++;
        
    }
    
    // Get node method
    
    private Node getNode(N value) {
        
        if (nodes.containsKey(value)) {
            
            Node n = nodes.get(value);
            return n;
            
        }
        
        return null;
        
    }
    
    // Add node method
    
    public boolean addNode(N value) {
        
        if (nodes.containsKey(value)) return false;
        
        int id = getId();
        int vc = getVerificationCode();
        Node n = new Node(value, id, vc);
        
        System.out.println(n.value.getClass());
        
        nodes.put(value, n);
        nodeIds.put(n.id, n);
        
        return true;
        
    }
    
    // Remove node method
    
    public void removeNode(N value) {
        
        if (!nodes.containsKey(value)) return;
        
        Node n = nodes.get(value);
        returnId(n.id);
        
        nodes.remove(value);
        nodeIds.remove(n.id);
        
    }
    
    // Generate Edge Code
    
    private String getEdgeCode(int fromId, int toId) {
        
        return fromId + "," + toId;
        
    }
    
    // Get edge method
    
    private Edge getEdge(Node from, Node to) {
        
        int idFrom = from.id;
        int idTo   = to.id;
        
        String s = getEdgeCode(idFrom, idTo);
        if (!edges.containsKey(s)) return null;
        
        Edge e = edges.get(s);
        if (!(e.fromVC == from.verification_code) || !(e.toVC == to.verification_code)) {
            
            edges.remove(s);
            return null;
            
        }
        
        return e;
        
    }
    private Double getEdgeValue(Edge e) {
        
        if (e.label instanceof Number) {
            
            Number n = (Number) e.label;
            return n.doubleValue();
            
        }
        
        if (e.label instanceof String) {
            
            String s = (String) e.label;
            return s.length() + 0.0;
            
        }
        
        return 1.0;
        
    }
    
    // Add edge method
    
    public boolean addEdge(N from, N to, E label) {
        
        Node nFrom = nodes.get(from);
        Node nTo   = nodes.get(to);
        
        Edge e = new Edge(nFrom.verification_code, nTo.verification_code, label);
        edges.put(getEdgeCode(nFrom.id, nTo.id), e);
        
        return true;
        
    } 
    
    // Remove edge method
    
    public boolean removeEdge(N from, N to, E label) {
        
        Node nFrom = nodes.get(from);
        Node nTo   = nodes.get(to);
        
        edges.remove(getEdgeCode(nFrom.id, nTo.id));
        
        return true;
        
    }
    
    //Get Neighbours External;
    
    public ArrayList<N> getNeighbours(N from){
    
        Node node = getNode(from);
        if (node == null) return null;
        
        ArrayList<N> list = new ArrayList<>();
        ArrayList<Node> auxList = getNeighboursNode(node);
        
        for (Node n : auxList) list.add(n.value);
        
        return list;
    }
    
    //Get Neighbours Internal
    
    private ArrayList<Node> getNeighboursNode(Node from){
    
        ArrayList<Node> auxList = new ArrayList<>();
        
        for(String key : edges.keySet()) {
            
            if(key.startsWith(from.id + ",")) {
                                
                int index = key.indexOf(",");                
                String aux = key.substring(index+1);
                auxList.add(nodeIds.get(Integer.parseInt(aux)));
                
            }
            
        }
        
        return auxList;
    }
    
    // Dijkstra 
    
    public N[] dijkstraPaths(N origin) throws Exception {
        
        // Get Origin
        
        Node norigin = getNode(origin);
        if (norigin == null) throw new Exception("Invalid Input");
        
        // Declare variables
        
        Set<Node> evaluated;
        PriorityQueue<Node> pqnodes;
        Map<N, N> paths = new HashMap<>(nodes.size());
        Map<Node, Double> path_sizes = new HashMap<>(nodes.size());
        
        // Create set
        
        evaluated = new HashSet<>();
        
        // Create priority queue
        
        pqnodes = new PriorityQueue<>(nodes.size(), new Comparator<Node>() {
            
            @Override
            public int compare(Node o1, Node o2) { 
                
                Double o1_size = path_sizes.get(o1);
                Double o2_size = path_sizes.get(o2);
                
                return o1_size.compareTo(o2_size); 
            
            }
            
        });
        
        // Fill array with Infinity, except for origin
        
        for (Node n : nodes.values()) {
            
            if (n.equals(norigin)) path_sizes.put(n, 0.0);
            else path_sizes.put(n, Double.POSITIVE_INFINITY);
            pqnodes.offer(n);
            
        }
        
        // Dijkstra Loop
        
        while (!pqnodes.isEmpty()) {
            
            Node current = pqnodes.poll();
            evaluated.add(current);
            
            ArrayList<Node> neighbours = getNeighboursNode(current);
            neighbours.removeIf(n -> evaluated.contains(n));
            
            for (Node n : neighbours) {
                
                // Get size from current to n
                
                Edge e = this.getEdge(current, n);
                Double value = getEdgeValue(e);
                
                // Dijkstra check
                
                if ((path_sizes.get(current) + value) < path_sizes.get(n)) {
                    
                    path_sizes.put(n, path_sizes.get(current) + value);
                    
                }
                
            }
            
        }
        
        for (Node n : path_sizes.keySet()) {
            
            System.out.println("Path to " + n.value + " is: " + path_sizes.get(n));
            
        }
        
        return null;
        
    }
    
    // Holds the current iterator class to use
    
    public enum IteratorType {BREADTH, DEPTH}
    
    Node originNode;
    IteratorType currentIterator = IteratorType.DEPTH;
    
    // Iterator
    
    @Override
    public Iterator<N> iterator() {
        
        if (originNode == null) return null;
        if(currentIterator == IteratorType.DEPTH)   return new DepthIterator(originNode);
        if(currentIterator == IteratorType.BREADTH) return new BreadthIterator(originNode);
        
        return null;
        
    }
    public void setIterator (IteratorType a, N origin) throws Exception {
        
        Node n = getNode(origin);
        if (n == null) throw new Exception("Origin not found!");
        
        originNode = n;
        currentIterator = a;
            
    }
    
    // Breadth-First Traversal method (Iterator)
    
    private class BreadthIterator implements Iterator<N>{    

        Queue<Node> iterableQueue = new ArrayDeque<>();
        
        Node originNode;
        Set verified  = new HashSet();
        
        public BreadthIterator(Node origin){
        
            originNode = origin;
            
            verified.add(origin);
            iterableQueue.offer(origin);
            
        }
        
        @Override
        public boolean hasNext() {
         
            return !iterableQueue.isEmpty();
            
        }

        @Override
        public N next() {
           
            Node aux = iterableQueue.poll();
            ArrayList<Node> neighbours = getNeighboursNode(aux);
            
            for(Node node : neighbours) {
                
                if(!verified.contains(node)) {
                    
                    iterableQueue.offer(node);
                    verified.add(node);
                    
                }
                
            }
            
            return aux.value;
            
        }
    
    }  
    
    // Depth-First Traversal method (Iterator)
    
    private class DepthIterator implements Iterator<N>{    

        Stack<Node> iterableStack = new Stack<>();
        
        Node originNode;
        Set verified  = new HashSet();
            
        public DepthIterator(Node origin){
        
            originNode = origin;
            
            verified.add(origin);
            iterableStack.push(origin);
            
        }
        
        @Override
        public boolean hasNext() {
            
            return !iterableStack.isEmpty();
            
        }
        
        @Override
        public N next() {
           
            Node aux = iterableStack.pop();
            ArrayList<Node> neighbours = getNeighboursNode(aux);
            
            for(Node node : neighbours) {
                
                if(!verified.contains(node)) {
                    
                    iterableStack.push(node);
                    verified.add(node);
                    
                }
                
            }
            
            return aux.value;
            
        }

    }

    // Constructor
    
    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder();
        
        for (Node n : nodes.values()) {
            
            builder.append(n.id);
            builder.append(" ");
            builder.append(n.value);
            builder.append(" ");
            builder.append(n.verification_code);
            builder.append("\n");
            
        }
        
        builder.append("\n");
        
        Object[] keyset  = edges.keySet().toArray();
        
        for (Object so : keyset) {
            
            String s = (String)so;
            
            int fromId = Integer.parseInt(s.split(",")[0]);
            int toId = Integer.parseInt(s.split(",")[1]);
            
            Node from = nodeIds.get(fromId);
            Node to   = nodeIds.get(toId);
            
            Edge e = getEdge(from, to);
            if (e == null) continue;
            
            /*Edge e = edges.get(s);
            if (!(e.fromVC == from.verification_code) || !(e.toVC == to.verification_code)) { continue; }*/
            
            builder.append(e.fromVC);
            builder.append(" ");
            builder.append(e.toVC);
            builder.append(" ");
            builder.append(e.label);
            builder.append("\n");
            
        }
        
        return builder.toString();
        
    }
    
    public Graph() {
        
        nodes = new HashMap<>();
        edges = new HashMap<>();
        ids   = new Stack<>();
        
        nodeIds = new HashMap<>();
        
        ids.push(0);
        
    }
    
    // </editor-fold>
    
}
