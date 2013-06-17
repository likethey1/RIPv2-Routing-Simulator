import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * NetworkGraph.java
 * 
 * This class represents a network graph. Each node is a router.
 * The network is generated by first generating pairs of nodes
 * and constructing an edge between each pair. Then, new edges 
 * are constructed between each pair of nodes, thus connecting the graph.
 * 
 * @author Steven Shaw
 * @date 1/2/12
 *
 */
public class NetworkGraph{
	// Network graph data storage structures
	private static Set<Node> networkGraph = new HashSet<Node>();
	private Set<String> uniqueAddresses = new HashSet<String>();
	
	private Random generator = new Random();
	private String[] nodeType = {"core", "edge"}; // Node types
	
	/**
	 * NetworkGraph: Creates a network graph with a specific
	 * number of pairs of nodes (routers)
	 * 
	 * @param numNodePairs
	 */
	public NetworkGraph(int numNodePairs){
		Set<Edge> initialEdges = new HashSet<Edge>();
		String nodeType1;
    	String nodeType2;
		
		// Generates initial edges
    	for (int i = 0; i < numNodePairs; i++) {
           	String address1 = generateIPAddress();
           	String address2 = generateIPAddress();
           	
           	// Duplicate address check
           	if( uniqueAddresses.contains(address1) || uniqueAddresses.contains(address2) ){
           		continue;
           	}
               
           	// Generate valid node connection Type
           	do{
               	nodeType1 = generateNodeType();
               	nodeType2 = generateNodeType();
           	}while(!validateNodeConnection(nodeType1, nodeType2)); 
           	
           	// Creates 2 nodes
           	Node x = new Node(nodeType1, address1);
           	Node y = new Node(nodeType2, address2);
           	
           	// Constructs an edge between previously created nodes
            Edge edge = new Edge(x, y, generateWeight());
            
            // Add edge to each Node
            x.addEdge(edge);
            y.addEdge(edge);
            
            // Add edge to graph
            initialEdges.add(edge);
            networkGraph.add(x);
            networkGraph.add(y);
                
            // Updates address structure
            uniqueAddresses.add(address1);
            uniqueAddresses.add(address2);
        }
    	
        // Generates new edges to ensure connected graph
        Iterator<Edge> it = initialEdges.iterator();
        Edge curEdge = it.next();
        while(it.hasNext()){
        	Edge nextEdge = it.next();
            
            Node curX = curEdge.getX();
            Node curY = curEdge.getY();
            
            Node nextX = nextEdge.getX();
            Node nextY = nextEdge.getY();
            
            // Add valid new edges to graph set
            if(validateNodeConnection(curX.getType(), nextX.getType())){
            	Edge newXX = new Edge(curX, nextX, generateWeight());
            	
           		curX.addEdge(newXX);
           		nextX.addEdge(newXX);
           	}else if(validateNodeConnection(curX.getType(), nextY.getType())){
           		Edge newXY = new Edge(curX, nextY, generateWeight());
           		
           		curX.addEdge(newXY);
           		nextY.addEdge(newXY);
           	}else if(validateNodeConnection(curY.getType(), nextX.getType())){
           		Edge newYX = new Edge(curY, nextX, generateWeight());
           		
           		curY.addEdge(newYX);
           		nextX.addEdge(newYX);
           	}else if(validateNodeConnection(curY.getType(), nextY.getType())){
           		Edge newYY = new Edge(curY, nextY, generateWeight());
           		
           		curY.addEdge(newYY);
           		nextY.addEdge(newYY);
           	}  	
           	curEdge = nextEdge;
       	} 
	}
	
	/**
	 * generateWeight: Randomly generates an edge weight
	 * that is between 1 and 100
	 * 
	 * @return A randomly generated edge weight
	 */
	private int generateWeight(){
		int weight;
		do{
			weight = generator.nextInt(100);
		}while(weight == 0); // Ensures a non zero weight
		
		return weight;
	}
	
	/**
	 * generateNodeType: Randomly generates a node type
	 * based on the defined node types
	 * 
	 * @return A randomly generated node type
	 */
	private String generateNodeType(){
		return nodeType[generator.nextInt(nodeType.length)];
	}
	
	/**
	 * 
	 * @param nodeType1 The type of the first node
	 * @param nodeType2 The type of the second node
	 * @return If the connection is valid or not
	 */
	private boolean validateNodeConnection(String nodeType1, String nodeType2){
		/* Always true because hosts are not implemented,
		so no need to valid connection type */
		return true;
	}
	
	/**
	 * generateIPAddress: Randomly generates an IP address
	 * 
	 * @return A random IP address
	 */
	private String generateIPAddress(){
		StringBuilder addressBuilder = new StringBuilder();
		
    	addressBuilder.append( Integer.toString(generator.nextInt(255)) );
    	addressBuilder.append(".");
    	
    	addressBuilder.append( Integer.toString(generator.nextInt(255)) );
    	addressBuilder.append(".");
    	
    	addressBuilder.append( Integer.toString(generator.nextInt(255)) );
    	addressBuilder.append(".");
    	
    	addressBuilder.append( Integer.toString(generator.nextInt(255)) );
	
    	return addressBuilder.toString();
	}
	
	/**
	 * getNetworkGraph: Gets the set of nodes that represents the network
	 * 
	 * @return A set of Nodes that represents the network
	 */
	public static Set<Node> getNetworkGraph(){
		return networkGraph;
	}
	
	/**
	 * getAddresses: Gets the unique IP address data structure
	 * 
	 * @return the uniquely generated IP addresses
	 */
	public Set<String> getAddresses(){
		return uniqueAddresses;
	}
	
	/**
	 * toString: Prints the network
	 * 
	 * @return a String representation of the network
	 */
	public String toString(){
		StringBuilder graphBuilder = new StringBuilder();
		
		// For each node, print its edges
		for(Node node : networkGraph){
			Set<Edge> edges = node.getEdges();
			
			// For each edge, print its nodes and the weight
			int counter = 0;
			for(Edge edge : edges){
				graphBuilder.append("\t" + edge.getX().getAddress() + " to " 
						+ edge.getY().getAddress() + " weight: " + edge.getWeight());
				
				// Appends a new line for every line but the last
				if(counter < edges.size() ){
					graphBuilder.append("\n");
				}
				counter++;
			}
		}
		
		// Removes last occurrence of \n
		graphBuilder.delete(graphBuilder.length() - 1, graphBuilder.length());
		
		return graphBuilder.toString();
	}
}