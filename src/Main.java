public class Main {
    
    public static void main(String[] args) {
        
        Graph<String, Integer> g = new Graph<>();
        
        for (ObservableGraph.RunnableAction a : g.getActions()) {
            
            System.out.println(a.parameters[0]);
            
        }
        
    }
    
}
