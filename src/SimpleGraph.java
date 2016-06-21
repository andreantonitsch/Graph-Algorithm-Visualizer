/**
   A simple graph with round nodes and straight edges.
*/
public class SimpleGraph extends Graph
{
   @Override
   public Node[] getNodePrototypes()
   {
      Node[] nodeTypes =
         {
            new TextNode("Sample")
         };
      return nodeTypes;
   }

   @Override
   public Edge[] getEdgePrototypes()
   {
      Edge[] edgeTypes = 
         {
            new LineEdge(),
            new TextEdge("Sample")
         };
      return edgeTypes;
   }
}





