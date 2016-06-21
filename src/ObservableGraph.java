import java.util.List;

public interface ObservableGraph {
    
    // Observer/Observable
    
    public void addObserver(GraphObserver g);
    
    public void removeObserver(GraphObserver g);
    
    public interface GraphObserver {
        
        public void update();
        
    }
    
    // Runnable Actions
    
    public List<RunnableAction> getActions();
    
    public void runAction(RunnableAction r, Object... params);
    
    public class RunnableAction {
        
        String name, description;
        Class<?>[] parameters;
        
        // Getters
        
        public String getName() {
            
            return name;
            
        }
        
        public String getDescription() {
            
            return description;
            
        }
        
        public Class<?>[] getParameters() {
            
            return parameters;
            
        }
        
        // Constructor
        
        public RunnableAction(String name, String description, Class<?>[] parameters) {
            
            this.name = name;
            this.description = description;
            this.parameters = parameters;
            
        }
        
    }
    
}