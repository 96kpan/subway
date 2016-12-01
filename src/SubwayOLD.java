import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SubwayOLD {

	private SplayTree tree;
	private Node root; // root of curr tree
	private int v; //number of vertices

	// constructor
	public SubwayOLD() {
		tree = new SplayTree();
		root = tree.root;
		v = 0;
	}

	// The insert command accepts station info (key x and y coordinates and
	// satellite string data s, just like project 4), which the data structure
	// should store. Like project 4, a later insert command could update the
	// satellite data. No output is required.
	/*
	 * In the insert, we want to insert a subway stop into our splay tree. We
	 * are using a splay tree to store our subway stops because of a faster
	 * runtime to lookup where everything is plus its (respective) adjacency
	 * list. Utilizing Prog 4 in this current assignment will save us time both
	 * in runtime, as well as implementation.
	 * 
	 * Essentially, we are inserting subway stops into our splay tree
	 */
	public void insert(Point p, String s) {
		//in the insert method, we just use the insert method in our
		//splay tree to insert. This should check for nullpointerexceptions
		//and other error handlings.
		tree.insert_record(p, s);
		v++; //increases the number of vertices
	}

	// The adjacent command says that the station at coordinates (x1, y1) is
	// directly connected to the station at (x2, y2). They are connected by
	// a subway track, without any intervening stops. In case of bad input
	// (e.g., if (x1, y1) or (x2, y2) is not a valid station) the input should
	// be ignored. Expect this to be tested. No output is required.
	/*
	 * In addAdjacency, we are essentially adding an edge into our graph. How we
	 * will be doing this is splaying the tree (moving the desired stop, p, to
	 * the root of the tree) and then inserting this new "q" point into it's
	 * adjacency list. Algorithm will be explained more in detail in the code
	 * below
	 * 
	 * In essence, we are adding another element (q) to our p's adjacency list
	 * and also adding another element (p) to our q's adjacency list
	 */
	public void addAdjacency(Point p, Point q) {
		
		//as part of our specs, if either one of the inputs are bad, 
		//we ignore all of it, no output is required
		if(tree.search(p).equals(null) || tree.search(q).equals(null)){
			return;
		}
		
		//now lets say both search produced something that isn't null
		//meaning it can be found, then we add the respective elements to
		//the correct nodes
		else{
			// add the q neighbor to p, thus adding an "edge" between p and q
			// we first search the point P in our splay tree. Using our search
			// method that returns a node, we then access that object's properties,
			// method addElement, to add that new point into their adjacency list
			tree.search(p).addElement(tree.search(q));

			// add the q neighbor to p, thus adding an "edge" between p and q
			// we first search the point q in our splay tree. Using our search
			// method that returns a node, we then access that object's properties,
			// method addElement, to add that new point into their adjacency list
			// this algorithm is, as you may have noticed, the vice versa of the one
			// above
			tree.search(q).addElement(tree.search(q));
			

		}
		
	}

	// This command asks for a route, if any, from the station at coordinates 
	// (x1, y1) to the station at (x2, y2).  If no path exists, return the string 
	// "NO PATH"; otherwise, return the coordinates and satellite data for each 
	// station (including the first and last), in a string, one station per line.  
	// See the sample input and output.
	/*
	 * Breadth First Search (BFS) is a traversal algorithm that starts with a node,
	 * finds all of the adjacent nodes and continues until we find "finish" node.
	 * Throughout the whole traversal, we always try to make sure to note which node
	 * has been visited to prevent cycles from forming.
	 * 
	 * Here is the general algorithm of implementation:
	 * 1. Visit the adjacent unvisited vertex. Mark it as visited. Display it. Insert it in a queue.
	 * 2. If no adjacent vertex is found, remove the first vertex from the queue.
	 * 3. Repeat Rule 1 and Rule 2 until the queue is empty.
	 * 
	 * SAMPLE OUTPUT: 
	 * 1. NO PATH
	 * 	-OUTPUT: "NO PATH"
	 * 2. THERE IS A PATH
	 * 	-OUTPUT: "50 60 New York City\n"
	 */
	public String getRoute(Point start, Point finish) {
		
		StringBuilder s = new StringBuilder();
		
		//checks if start is the same finish
		if(start.equals(finish)){
			s.append(start.toString() + " " + tree.lookup(start) + "\n");
			s.append(finish.toString() + " " + tree.lookup(finish));
		}
		
		else{
			Queue<Node> q = new LinkedList();
			ArrayList<Node> visited = new ArrayList();
			q.add(tree.search(start));
			visited.add(tree.search(start));
			
			//while the queue isn't empty, we contain to pop and find its neighbors
			while(!q.isEmpty()){
				Node currNode = q.poll(); //pop off the queue
				
				//found desired location
				if(currNode.equals(tree.lookup(finish))){
					while(currNode != null){
						s.append(currNode.toString() + " " + currNode.satellite + "\n");
						currNode = currNode.parent;
					}
					//s.append(currNode.toString() + " " + currNode.satellite + "\n");
				}
				
				//node is already visited -> ignore and move onto next node
				if(visited.contains(currNode)){
					continue;
				}
				
				//we have now visited the currNode
				visited.add(currNode);
				
				for(int i = 0; i < currNode.adjacencyList.size(); i++){
					if(!visited.contains(currNode.adjacencyList.get(i))){
						q.add(currNode.adjacencyList.get(i));
						currNode.adjacencyList.get(i).parent = currNode;
					}
				}
			}
		}
		
		return s.toString(); //returns string representation
	}

	// (optional) void delete(Point p)
}
