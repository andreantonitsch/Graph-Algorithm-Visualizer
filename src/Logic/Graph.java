package Logic;

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

public class Graph<N extends Comparable,E extends Comparable> implements Iterable<N>, ObservableGraph<N,E> {
    
    // Variables
    
    static int verfication_aux;
    
    Map<N, Node> nodes;
    Map<String,Edge> edges;
    Stack<Integer> ids;
    
    Map<Integer, Node> nodeIds;

    // <editor-fold desc="Observable Graph">
    
    List<GraphObserver> observers;
    
    private String getStringFromNode(Node node) {
        
        return node.id + node.value.toString() + node.verification_code;
        
    }
    
    private N getNodeFromString(String node) {
        
        try {
            
            N value = (N) node;
            return value;
            
        }
        catch (ClassCastException e) {
            
            // String is not the type of the N
            
        }
        
        try {
            
            Integer i = Integer.parseInt(node);
            N value = (N) i;
            return value;
            
        }
        catch (ClassCastException e) {
            
            // Integer is not the type of the N
            
        }
        
        return null;
        
    }
    
    private String getStringFromEdge(Edge edge) {
        
        return edge.fromVC + edge.label.toString() + edge.toVC;
        
    }
    
    private E getEdgeFromString(String edge) {
        
        try {
            
            E value = (E) edge;
            return value;
            
        }
        catch (ClassCastException e) {
            
            // String is not the type of the N
            
        }
        
        try {
            
            Integer i = Integer.parseInt(edge);
            E value = (E) i;
            return value;
            
        }
        catch (ClassCastException e) {
            
            // Integer is not the type of the N
            
        }
        
        return null;
        
    }
    
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

    private void action(Report r) {
        
        for (GraphObserver go : observers) {
            
            go.update(r);
            
        }
        
    }
    
    @Override
    public List<RunnableAction> getActions() {
    
        List<RunnableAction> actions = new ArrayList<>();
        
        actions.add(new RunnableAction("Dijkstra",
                                       "Finds the shortest path between a nodes and every other one in the graph",
                                        new ParameterType[] { ParameterType.NODE, ParameterType.NODE } ));
    
        actions.add(new RunnableAction("Prim",
                                       "Finds the minimum spanning tree for the current graph",
                                       new ParameterType[] {}));
        
        actions.add(new RunnableAction("Critical Path",
                                       "Finds the critical path",
                                       new ParameterType[] { ParameterType.NODE }));
        
        actions.add(new RunnableAction("Topological Order",
                                       "Order the nodes",
                                       new ParameterType[] {}));
        
        return actions;
        
    }

    @Override
    public void runAction(RunnableAction r, String... params) throws Exception {
    
        if (params.length != r.getParameters().length) { throw new IllegalArgumentException("Invalid number of parameters!"); }
        
        switch (r.getName()) {
            
            // <editor-fold desc="Dijkstra">
            
            case "Dijkstra":
                
                N n1 = getNodeFromString(params[0]);
                N n2 = getNodeFromString(params[1]);
                
                if (n1 == null || n2 == null) throw new IllegalArgumentException();
                
                try {
                    
                    dijkstraPaths(n1, n2);
                    
                }
                catch (Exception e) {
                    
                    System.out.println(e.getClass() + " " + e.getMessage());
                    throw new Exception("Error processing algorithm!" + e.getMessage());
                    
                }
                
                break;
                
            // </editor-fold>
                
            // <editor-fold desc="Prim">
            
            case "Prim":
                
                try {
                    
                    PrimMST();
                    
                }
                catch (Exception e) {
                    
                    System.out.println(e.getClass() + " " + e.getMessage());
                    throw new Exception("Error processing algorithm!" + e.getMessage());
                    
                }
                
                break;
                
            // </editor-fold>
            
            // <editor-fold desc="Critical Path">
            
            case "Critical Path":
                
                N n = getNodeFromString(params[0]);
                
                if (n == null) throw new IllegalArgumentException();
                
                try {
                    
                    CriticalPath(n);
                    
                }
                catch (Exception e) {
                    
                    System.out.println(e.getClass() + " " + e.getMessage());
                    throw new Exception("Error processing algorithm!" + e.getMessage());
                    
                }
                
                break;
                
            // </editor-fold>
            
            // <editor-fold desc="Topological Order">
            
            case "Topological Order":
                
                try {
                    
                    TopologicOrder();
                    
                }
                catch (Exception e) {
                    
                    System.out.println(e.getClass() + " " + e.getMessage());
                    throw new Exception("Error processing algorithm!" + e.getMessage());
                    
                }
                
                break;
                
            // </editor-fold>
                
        }
    
    }

    @Override
    public String addNode(String node) {
    
        N n = getNodeFromString(node);
        
        if (n != null) { 
            Node nNode = addNode2(n); 
            
            if (nNode != null) 
                return getStringFromNode(nNode); 
        
        }
        
        return null;
        
    }

    @Override
    public boolean removeNode(String node) {
    
        N n = getNodeFromString(node);
        if (n != null) { removeNode(n); return true; }
        return false;
        
    }

    @Override
    public String addEdge(String from, String to, String label) {
    
        N n1 = getNodeFromString(from);
        N n2 = getNodeFromString(to);
        E e1 = getEdgeFromString(label);

        if (n1 == null || n2 == null || e1 == null) return null;
        
        Edge e = addEdge2(n1, n2, e1);
        
        return getStringFromEdge(e);
    
    }

    @Override
    public boolean removeEdge(String from, String to, String label) {
    
        N n1 = getNodeFromString(from);
        N n2 = getNodeFromString(to);
        E e1 = getEdgeFromString(label);
        
        if (n1 == null || n2 == null || e1 == null) return false;
        
        removeEdge(n1, n2, e1);
        
        return true;
        
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
        
        @Override
        public String toString() {
            
            return value.toString();
            
        }
        
    }
    
    // Edge class
    
    class Edge {
        
        E label;
        int fromId, toId, fromVC, toVC;
        
        public Edge(Node from, Node to, E label) {
            
            this.fromId = from.id;
            this.fromVC = from.verification_code;
            
            this.toId = to.id;
            this.toVC = to.verification_code;

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
        
        Node n = addNode2(value);
        return n != null;
        
    }
    
    private Node addNode2(N value) {
        
        if (nodes.containsKey(value)) return null;
        
        int id = getId();
        int vc = getVerificationCode();
        Node n = new Node(value, id, vc);
        
        nodes.put(value, n);
        nodeIds.put(n.id, n);
        
        return n;
        
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
        
        return getEdgeValue(e.label);
        
    }
    private Double getEdgeValue(E e) {
        
        if (e instanceof Number) {
            
            Number n = (Number) e;
            return n.doubleValue();
            
        }
        
        if (e instanceof String) {
            
            try {
                
                int i = Integer.parseInt((String)e);
                return i + 0.0;
                
            }
            catch (Exception ex) {
                             
                String s = (String) e;
                return s.length() + 0.0;
                
            }
            
        }
        
        return 1.0;
        
    }
    
    // Add edge method
    
    public boolean addEdge(N from, N to, E label) {
        
        Edge e = addEdge2(from, to, label);
        return e != null;
        
    }
    private Edge addEdge2(N from, N to, E label) {
        
        Node nFrom = nodes.get(from);
        Node nTo   = nodes.get(to);
        
        String edgeCode = getEdgeCode(nFrom.id, nTo.id);
        if (edges.containsKey(edgeCode)) return null;
        
        Edge e = new Edge(nFrom, nTo, label);
        edges.put(getEdgeCode(nFrom.id, nTo.id), e);
        
        return e;
        
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
        
        auxList.sort((Node o1, Node o2) -> {
            
            Edge e1 = getEdge(from, o1);
            Edge e2 = getEdge(from, o2);
            
            return getEdgeValue(e2).compareTo(getEdgeValue(e1));
            
        });
        
        return auxList;
    }
    
    // Dijkstra 
    
    public Map<N, Double> dijkstraPaths(N origin, N destination) throws Exception {
        
        // Get Origin
        
        Node norigin = getNode(origin);
        Node ndest = getNode(destination);
        if (norigin == null || ndest == null) throw new Exception("Invalid Input");
        
        // Declare variables
        
        Set<Node> evaluated = new HashSet<>();
        Map<Node, Node> paths = new HashMap<>(nodes.size());
        Map<Node, Double> path_sizes = new HashMap<>(nodes.size());

        // Create priority queue
        
        PriorityQueue<Node> pqnodes = new PriorityQueue<>(nodes.size(), (Node o1, Node o2) -> {
            
            Double o1_size = path_sizes.get(o1);
            Double o2_size = path_sizes.get(o2);
            
            int result = o1_size.compareTo(o2_size);

            return result;
            
        });
        
        // Fill array with Infinity, except for origin
        
        for (Node n : nodes.values()) {
            
            if (n == norigin) path_sizes.put(n, 0.0);
            else path_sizes.put(n, Double.POSITIVE_INFINITY);
            pqnodes.offer(n);
            paths.put(n, n);
            
        }
        
        // Dijkstra Loop
        
        while (!pqnodes.isEmpty()) {
            
            Node current = pqnodes.peek();
            evaluated.add(current);
            
            action(new Report(Report.ACTION_PAINT,
                          new String[] { getStringFromNode(current) },
                          new String[] {}));
            
            ArrayList<Node> neighbours = getNeighboursNode(current);
            
            for (int i = neighbours.size() - 1; i >= 0; i--) {
                
                Node n = neighbours.get(i);
                if (evaluated.contains(n)) { neighbours.remove(i); }
                
            }

            for (Node n : neighbours) {
                
                // Get size from current to n
                
                Edge e = this.getEdge(current, n);
                Double value = getEdgeValue(e);
                
                // Dijkstra check
                
                if ((path_sizes.get(current) + value) < path_sizes.get(n)) {
                    
                    path_sizes.put(n, path_sizes.get(current) + value);
                    paths.put(n, current);
                    
                }
                
            }
            
            pqnodes.poll();
            if (current == ndest) break;
            
        }
        
        // Paint path
        
        Node aux = ndest;
        ArrayList<String> path = new ArrayList<>();
        
        while (paths.get(aux) != aux) {
            
            Node aux2 = paths.get(aux);
            Edge edge = getEdge(aux2, aux);
   
            path.add(getStringFromEdge(edge));
            aux = aux2;
            
        }
        
        action(new Report(Report.ACTION_PAINT,
                          new String[] {},
                          path.toArray(new String[path.size()])));
        
        // Copy map to return
        
        Map<N, Double> pathsResult = new HashMap<>();
        
        for (Node n : path_sizes.keySet()) {
            
            pathsResult.put(n.value, path_sizes.get(n));

        }
        
        return pathsResult;
        
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

    // Breadth-First Traversal Method (Atomic)
    
    public List<N> BreadthTraversal(N origin) {
        
        ArrayList<N> traversal = new ArrayList<>();
        
        try {
            
            setIterator(IteratorType.BREADTH, origin);
            for (N data : this) traversal.add(data);
            
        }
        catch (Exception e) {
            
            return null;
            
        }
        
        return traversal;
        
    }
    
    // Depth-First Traversal Method (Atomic)
    
    public List<N> DepthTraversal(N origin) {
        
        ArrayList<N> traversal = new ArrayList<>();
        
        try {
            
            setIterator(IteratorType.DEPTH, origin);
            for (N data : this) traversal.add(data);
            
        }
        catch (Exception e) {
            
            return null;
            
        }
        
        return traversal;
        
    }
    
    // Prim's Algorithm for MSTs
    // Requires an undirected graph
    
    public Graph<N,E> PrimMST(){

        HashMap<Node, E> cheapestCost = new HashMap<>();
        HashMap<Node, Edge> father = new HashMap<>();
        
        HashSet<Node> newNodes = new  HashSet<>();
        HashSet<Edge> newEdges = new  HashSet<>();
        
        PriorityQueue<Node> vertices = new PriorityQueue((Object o1, Object o2) -> {
            
            Node n1 = (Node)o1;
            Node n2 = (Node)o2;
            
            E e1 = cheapestCost.get(n1);
            E e2 = cheapestCost.get(n2);
            
            if (e1 == null && e2 == null) return 0;
            if (e1 == null) return 1;
            if (e2 == null) return -1;
            
            return cheapestCost.get(n1).compareTo(cheapestCost.get(n2));
            
        });
        
        for(Node n : nodes.values()){
            
            father.put(n, null);
            cheapestCost.put(n, null);
            vertices.offer(n);

        }

        while(!vertices.isEmpty()){
        
            Node n = vertices.peek();
            
            action(new Report(Report.ACTION_PAINT,
                          new String[] { getStringFromNode(n) },
                          new String[] {}));
            
            newNodes.add(n);

            for(Node neighbour : getNeighboursNode(n)){
            
                if(vertices.contains(neighbour)){
                    
                    if((cheapestCost.get(neighbour)) == null || getEdgeValue(getEdge(n, neighbour)).compareTo(getEdgeValue(cheapestCost.get(neighbour))) < 0 ){

                        cheapestCost.put(neighbour , getEdge(n, neighbour).label);
                        father.put(neighbour, getEdge(n, neighbour));
                    
                    }  
                        
                }
            
            }
            
            if(father.containsKey(n)) {
                
                Edge e = father.get(n);
                
                if (e != null) {
                    
                    newEdges.add(e);
                
                    action(new Report(Report.ACTION_PAINT,
                              new String[] {},
                              new String[] { getStringFromEdge(e) }));

                }
                
            }
            
            vertices.poll();
        
        }

        Graph<N,E> return_graph = new Graph<>();
        
        for(Node n : newNodes) return_graph.addNode(n.value);
        
        for(Edge e : newEdges){
            
            Node toNode = nodeIds.get(e.toId);
            Node fromNode = nodeIds.get(e.fromId);
        
            return_graph.addEdge(fromNode.value, toNode.value, e.label);
        
        }
        
        return return_graph;
        
    }
    
    // Topological Ordering
    // Requires Acyclic directed graph
    
    public List<N> TopologicOrder(){
    
       ArrayList<N> return_list = new ArrayList<>();
       ArrayList<Edge> aux_edges = new ArrayList(edges.values());
       ArrayList<Node> aux_nodes = new ArrayList(nodes.values());
       
       while(!aux_nodes.isEmpty()){
           
           for(int i = 0; i < aux_nodes.size(); i++){
           
               Node n = aux_nodes.get(i);
               
                boolean node_with_no_parents_left = true;

                for(Edge e : aux_edges){

                    Node destination = nodeIds.get(e.toId);
                    Node departure   = nodeIds.get(e.fromId);

                    if(destination.value.equals(n.value) && aux_nodes.contains(departure))
                        node_with_no_parents_left = false;

                }

                if(node_with_no_parents_left){
                    
                    return_list.add(n.value);
                    aux_nodes.remove(n);
                     
                    action(new Report(Report.ACTION_PAINT,
                          new String[] { getStringFromNode(n) },
                          new String[] {}));
                     
                }
                
           }
       
       }
       
       return return_list;

    }
    
    // Topological Ordering
    // Requires Acyclic directed graph
    
    public Map<Integer, List<N>> TopologicOrderMap(){
    
       HashMap<Integer, List<N>> return_map = new HashMap<>();
    
       ArrayList<Edge> aux_edges = new ArrayList(edges.values());
       
       ArrayList<N> aux_nodes = new ArrayList(nodes.values());
       
       int classCount = 0;
       
       while(!aux_nodes.isEmpty()){
           
           ArrayList<N> class_list = new ArrayList<>();
               
           for(int i = 0; i < aux_nodes.size(); i++){
           
               N n = aux_nodes.get(i);
               
                boolean node_with_no_parents_left = true;

                for(Edge e : aux_edges){

                    Node destination = nodeIds.get(e.toVC);
                    Node departure   = nodeIds.get(e.fromVC);

                    if(destination.value.equals(n) && aux_nodes.contains(departure.value))
                        node_with_no_parents_left = false;

                }

                if(node_with_no_parents_left){
                     class_list.add(n);
                     aux_nodes.remove(n);
                }
                
           }
           
           return_map.put(classCount, class_list);
           classCount++;
       
       }
       
       return return_map;

    }
       
    //Critical Path
    //Requires Acyclic directed graph
    
    public List<N> CriticalPath(N startingNode){
    
        Node currentNode = getNode(startingNode);
        if (currentNode == null) return null;
        
        List<N> return_list = new ArrayList<>();
        ArrayList<N> neighbours = getNeighbours(startingNode);
        
        while(neighbours.size() > 0){
        
            Node nn = currentNode;
            
            action(new Report(Report.ACTION_PAINT,
                   new String[] { getStringFromNode(nn) },
                   new String[] {}));
            
            N biggestNeighbour = neighbours.get(0);
            for(N n : neighbours) if( n.compareTo(biggestNeighbour) > 0 ) biggestNeighbour = n;
            
            return_list.add(currentNode.value);
            currentNode = getNode(biggestNeighbour);
            
            neighbours = getNeighbours(currentNode.value);
       
        }
        
        Node nn = currentNode;
        
        action(new Report(Report.ACTION_PAINT,
                   new String[] { getStringFromNode(nn) },
                   new String[] {}));
        
        return_list.add(currentNode.value);
        
        for (int i = 0; i < return_list.size() - 1; i++) {
            
            Node from = nodes.get(return_list.get(i));
            Node to = nodes.get(return_list.get(i+1));
            
            Edge e = getEdge(from, to);
            
            action(new Report(Report.ACTION_PAINT,
                   new String[] {},
                   new String[] { getStringFromEdge(e) }));
            
        }

        return return_list;
        
    }
    
    // Returns a possible Biggest value in the list.
    // Assures nothing in the case of partial ordering.
    
    private N biggestN (ArrayList<N> nodes){
    
        N biggest = nodes.get(0);
    
        for(N n : nodes) if( n.compareTo(biggest) > 0 ) biggest = n;
    
        return biggest;
        
    }
     
    // </editor-fold>
    
}
