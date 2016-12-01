import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
 * allowed libraries:

import java.lang.Comparable;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Math;
import java.lang.InstantiationException;
*/

public class SubwayGraph {

	private SplayTree tree;
	private Node root; // root of curr tree
	//private int v; // number of vertices

	// construct an empty graph
	public SubwayGraph() {
		tree = new SplayTree();
		root = tree.root;
		//v = 0;
	}

	// optional debug representation of graph
	public String toString() {
		// calls helper method. pass in an empty string and node ROOT
		return inOrder("", root);
	}

	private String inOrder(String string, Node head) {
		// traverse to left-most node on the tree
		if (head.left != null)
			// recursively call the inOrder method
			string = inOrder(string, head.left);

		// reach a node where there is no more LEFT
		// thus adds the satellite data into our string
		string += " " + head.satellite;

		// traverse to the right nodes on the tree
		if (head.right != null)
			// recursively call the inOrder method
			string = inOrder(string, head.right);

		// trims just in case there is an extra spaces
		return string.trim();
	}

	// Add vertex to the graph
	// (Point class is from project 4)
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
		// in the insert method, we just use the insert method in our
		// splay tree to insert. This should check for nullpointerexceptions
		// and other error handlings.
		tree.insert_record(p, s);
		//v++; // increases the number of vertices
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
	// Add edge to the graph
	public void adjacent(Point loc1, Point loc2) {

		// as part of our specs, if either one of the inputs are bad,
		// we ignore all of it, no output is required
		if (tree.search(loc1) == (null) || tree.search(loc2) == (null)) {
			return;
		}

		// now lets say both search produced something that isn't null
		// meaning it can be found, then we add the respective elements to
		// the correct nodes
		else {
			// add the q neighbor to p, thus adding an "edge" between p and q
			// we first search the point P in our splay tree. Using our search
			// method that returns a node, we then access that object's
			// properties,
			// method addElement, to add that new point into their adjacency
			// list
			tree.search(loc1).addElement(tree.search(loc2));

			// add the q neighbor to p, thus adding an "edge" between p and q
			// we first search the point q in our splay tree. Using our search
			// method that returns a node, we then access that object's
			// properties,
			// method addElement, to add that new point into their adjacency
			// list
			// this algorithm is, as you may have noticed, the vice versa of the
			// one
			// above
			tree.search(loc2).addElement(tree.search(loc1));

		}

	}

	// Try to find a shortest path between vertices
	// This command asks for a route, if any, from the station at coordinates
	// (x1, y1) to the station at (x2, y2). If no path exists, return the string
	// "NO PATH"; otherwise, return the coordinates and satellite data for each
	// station (including the first and last), in a string, one station per
	// line.
	// See the sample input and output.
	/*
	 * Breadth First Search (BFS) is a traversal algorithm that starts with a
	 * node, finds all of the adjacent nodes and continues until we find
	 * "finish" node. Throughout the whole traversal, we always try to make sure
	 * to note which node has been visited to prevent cycles from forming.
	 * 
	 * Here is the general algorithm of implementation: 1. Visit the adjacent
	 * unvisited vertex. Mark it as visited. Display it. Insert it in a queue.
	 * 2. If no adjacent vertex is found, remove the first vertex from the
	 * queue. 3. Repeat Rule 1 and Rule 2 until the queue is empty.
	 * 
	 */
	public String route(Point start, Point finish) {
		
		//if one of the points do not exist, we exit out with NO PATH found
		//to prevent a null pointer exception
		if(tree.search(start) == null || tree.search(finish) == null){
			return "NO PATH";
		}
		StringBuilder s = new StringBuilder();

		// checks if start is the same finish
		// this is technically unnecessarily, however I added it to decrease
		// time and space complexity
		if (start.equals(finish)) {
			s.append(start.toString() + " " + tree.lookup(start) + "\n");
			s.append(finish.toString() + " " + tree.lookup(finish));
		}

		// now if we are not given a start and end the same, then we go through
		// this
		// else statement
		else {
			// making a queue object to store the neighbors in an orderly fashio
			// in a FIFO
			// so we do not jump around
			Queue<Node> q = new LinkedList();
			ArrayList<Node> visited = new ArrayList(); // visited arraylist to
														// keep track of which
														// nodes has been
														// visited to already to
														// prevent unecessary
														// cycles
			tree.splay(tree.search(start)); // splays the start root to the top
											// of the tree. this will be at the
											// root
			q.add(tree.search(start)); // adds the first element into the queue
			visited.add(tree.search(start)); // adds visited to the arraylist to
												// prevent cycles while
												// traversing

			// while the queue isn't empty, we contain to pop and find its
			// neighbors
			while (!q.isEmpty()) {
				Node currNode = q.poll(); // pop off the top of the queue

				// if found desired location, we backtrack until the currNode is
				// null. Once
				// the currNode is null, that means we have reached the root of
				// the tree since the
				// parent of a root is null
				if (currNode.satellite.equals(tree.lookup(finish))) {
					while (currNode != null) {
						// inserts into our stringbulider for faster time
						// complexity
						s.insert(0, currNode.toString() + "\n");
						currNode = currNode.parent; // moves currNode pointer to
													// the parent
					}
					return s.toString(); // only case to return a string is if
											// there is a path
				}

				// all other cases if we don't find the finished point is we
				// need to add all of
				// currNode's neighbors. Because BFS goes breadth-y, instead of
				// going from root
				// to the leaves, we always add the neighbors of each "dequeued"
				// node to our queue
				// and then consider it with this while loop
				// But at the same time, we only want to add nodes we haven't
				// visited into our queue
				// because we don't want cycles
				for (int i = 0; i < currNode.adjacencyList.size(); i++) {
					// only non-visited nodes are considered
					if (!visited.contains(currNode.adjacencyList.get(i))) {
						// adds that node to the visited -> flagging it
						// essentially
						visited.add(currNode.adjacencyList.get(i));
						// sets the parent pointer of that
						// currNode.adjacencyList.get(i) to
						// currNode
						currNode.adjacencyList.get(i).parent = currNode;
						// adds to the queue
						q.add(currNode.adjacencyList.get(i));

					}
				}
			}
		}

		// if there is no path, then it will output this message
		return "NO PATH";
	}

	// extra credit -- remove a vertex and all incident edges
	/*
	 * This command is optional, for extra credit, and removes the station at 
	 * location (x, y) and all its connections.  If the station does not exist, 
	 * the command is ignored.  No output is required.
	 * 
	 * We first check if the Point exists. If it does exist, then we can proceed
	 * to the deletion method. However, if it doesn't exist, to prevent a null pointer
	 * exception, we just return and ignore. 
	 * 
	 * But if it does exist, we go to that node with point loc and go through it's
	 * adjacency list. We then traverse to each node, find that respective adjacency 
	 * list and delete that node with point loc. Then we delete the overall node 
	 * from our tree.
	 * 
	 * Again. No output is required, thus a return type of void
	 */
	public void delete(Point loc) {
		
		//if doesn't exist, ignore, to prevent a null pointer exception
		if(tree.search(loc) == null)
			return;

		// splay the point's node to find the respective adjacency list
		ArrayList list = tree.search(loc).adjacencyList; //gets list of node
		Node removeNode = tree.search(loc); //gets node

		//goes through the adj list and remove the removeNode from that list
		for(int i = 0; i < list.size(); i++){
			//go to that node's adjacency list and delete that index
			Node temp = (Node) list.get(i); //get node
			ArrayList tempList = temp.adjacencyList; //get list of that node
			tempList.remove(removeNode); //remove the desired node -> Node with loc Point
		}

		// then delete overall node
		tree.delete(loc);

	}
}
