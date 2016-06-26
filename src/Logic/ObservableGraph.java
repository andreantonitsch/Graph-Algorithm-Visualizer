package Logic;

import java.util.List;

public interface ObservableGraph<N,E> {
    
    // Observer/Observable
    
    public void addObserver(GraphObserver g);
    
    public void removeObserver(GraphObserver g);
    
    // Runnable Actions
    
    public List<RunnableAction> getActions();
    
    public void runAction(RunnableAction r, String... params) throws Exception;
    
    // Communication
    
    public String addNode(String node);
    
    public boolean removeNode(String node);
    
    public String addEdge(String from, String to, String label);
    
    public boolean removeEdge(String from, String to, String label);
            
}