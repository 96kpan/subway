import java.util.ArrayList;

public class Node {
	public Point record;
	public String satellite;
	public Node left, right, parent;
	public ArrayList<Node> adjacencyList;
	public boolean visited;

	//just another constructor I added if there is no information about left, right, and parent nodes
	//calls the constructor that contains left, right, and parent parameters
	public Node(Point rec, String sat)
	{
		this(rec, sat, null, null, null, null);
	}

	//just another constructor I added if there is information about left, right, and parent nodes
	//sets values
	public Node(Point rec, String sat, Node left, Node right, Node parent, ArrayList<Node> list)
	{
		record = rec;
		satellite = sat;
		this.left = left;
		this.right = right;
		this.parent = parent;
		list = new ArrayList();
		adjacencyList = list;
	}

	public String toString()
	{
		return "Station " + record + " " + satellite;
	}
	
	//inserts element into the arraylist of our adjacencyList
	public void addElement(Node p){
		this.adjacencyList.add(p); //adds the Point p to the front of the list
	}
	
	public String toStringAdjacencyList(){
		String s = "\n";
		for(int i = 0; i < this.adjacencyList.size(); i++){
			s += this.adjacencyList.get(i).toString() + "\n";
		}
		
		return s;
	}
}
