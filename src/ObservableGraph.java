import java.util.List;

public interface ObservableGraph<N,E extends Comparable> {
    
    // Observer/Observable
    
    public void addObserver(GraphObserver g);
    
    public void removeObserver(GraphObserver g);
    
    // Runnable Actions
    
    public List<RunnableAction> getActions();
    
    public void runAction(RunnableAction r, Object... params);
    
    // Communication
    
    public N stringToNode(String node);
    
    public String nodeToString(N node);
    
    public void addNode(String node);
    
}