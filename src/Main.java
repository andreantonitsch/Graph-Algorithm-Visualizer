public class Main {
    
    public static void main(String[] args) {
        
        Graph<String, Integer> g = new Graph<>();
        
        g.addNode("Teste");
        
        for (RunnableAction a : g.getActions()) {
            
            System.out.println(a.parameters[0]);
            
        }
        
    }
    
}
