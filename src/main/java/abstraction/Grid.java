package abstraction; /**
 * Importing java classes needed for the Grid class
 * 
 * Importing classes from the java.util package
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Grid using as a data structure an undirected graph using an adjacency list
 * It contains a grid of nbRows x nbCols cells.
 * Each cells is an adjacency list containing all the cells connected to this one.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Grid {

	/**
	 * State the Grid's class attributes
	 */
	
	private HashMap<Point,HashSet<Point>> adjacencyList;
	private int nbRows;
	private int nbCols;
	private int nbNodes;

	/**
	 * Constructor of the Grid
	 * 
	 * @param nbRows Number of rows in the grid
	 * @param nbCols Number of columns in the grid
	 */

	public Grid(int nbRows, int nbCols) {
		this.nbRows = nbRows;
		this.nbCols = nbCols;
		this.nbNodes = nbRows * nbCols;
		this.adjacencyList = new HashMap<Point,HashSet<Point>>();

		Point p = null;
		Point neighbor = null;
		HashSet<Point> neighborsList = null;
		for(int i=0; i<nbRows; i++){
			for(int j=0; j<nbCols; j++){
				p = new Point(i,j);
				neighborsList = new HashSet<Point>(4);
				if(i>0) {
					// Top neighbor
					neighbor = new Point(i-1,j);
					neighborsList.add(neighbor);
				}
				if(i<nbRows-1) {
					// Bottom neighbor
					neighbor = new Point(i+1,j);
					neighborsList.add(neighbor);
				}
				if(j>0) {
					// Left neighbor
					neighbor = new Point(i,j-1);
					neighborsList.add(neighbor);
				}
				if(j<nbCols-1) {
					// Right neighbor
					neighbor = new Point(i,j+1);
					neighborsList.add(neighbor);
				}
				this.adjacencyList.put(p, neighborsList);
			}
		}
	}

	/**
	 * Get the number of rows of the grid
	 * 
	 * @return Number of rows
	 */

	public int getNbRows() {
		return nbRows;
	}

	
	/**
	 * Get the number of columns of the grid
	 * 
	 * @return Number of columns
	 */

	public int getNbCols() {
		return nbCols;
	}

	/**
	 * Get the number of nodes (or cells) of the grid
	 * 
	 * @return Number of nodes
	 */

	public int getNbNodes() {
		return nbNodes;
	}

	/**
	 * Get the set of nodes of the grid
	 * 
	 * @return Set of nodes
	 */

	public Set<Point> getNodesList() {
		return this.adjacencyList.keySet();
	}

	/**
	 * Add an edge between two nodes
	 * 
	 * @param p1 Coordinates of the first node
	 * @param p2 Coordinates of the second node
	 */

	public void addEdge(Point p1, Point p2) {
		this.adjacencyList.get(p1).add(p2);
		this.adjacencyList.get(p2).add(p1);
	}

	/**
	 * Remove an edge between two nodes
	 * 
	 * @param p1 Coordinates of the first node
	 * @param p2 Coordinates of the second node
	 */

	public void removeEdge(Point p1, Point p2) {
		this.adjacencyList.get(p1).remove(p2);
		this.adjacencyList.get(p2).remove(p1);
	}

	/**
	 * Display the grid: display all nodes and for each node it displays all of its neighbors
	 */

	public void displayGrid() {
		for(int i=0; i<this.getNbRows(); i++) {
			for(int j=0; j<this.getNbCols(); j++) {
				System.out.println("Node ("+i+","+j+") : ");
				for(Point p : this.getListNeighbors(new Point(i,j))) {
					System.out.print(p+" ");
				}
			}
		}
	}

	/**
	 * Check if two nodes are connected by an edge (so if they are neighbors)
	 * 
	 * @param p1 Coordinates of the first node
	 * @param p2 Coordinates of the second node
	 * @return Boolean value equals to true if they are connected and false otherwise
	 */

	public boolean areConnected(Point p1, Point p2) {
		if(p1 != null && p2 != null && this.adjacencyList.containsKey(p1))
			return this.adjacencyList.get(p1).contains(p2);
		else
			return false;
	}

	/**
	 * Check if two nodes are connected by an edge (so if they are neighbors)
	 * 
	 * @param x1 X coordinate of the first node
	 * @param y1 Y coordinate of the first node
	 * @param x2 X coordinate of the second node
	 * @param y2 Y coordinate of the second node
	 * @return
	 */

	public boolean areConnected(int x1, int y1, int x2, int y2) {
		Point p1 = new Point(x1,y1);
		Point p2 = new Point(x2,y2);
		return areConnected(p1,p2);
	}

	/**
	 * Get the approximate cost of a path between a start and a destination
	 * 
	 * @param start Node where the path starts
	 * @param destination Node where the path ends
	 * @return Cost of the path
	 */

	public static int costFunctionPath(Point start, Point destination) {
		return Point.getDistance(start,destination);
	}

	/**
	 * Get the Point that has the minimum cost value
	 * 
	 * @param list List of points where the minimum is searched
	 * @param cost Map of the cost of each point
	 * @return Point that has the minimum cost
	 */

	public static Point getMinPoint(LinkedList<Point> list, HashMap<Point,Integer> cost) {
		Point minPoint = list.peek();
		for(Point p : list) {
			if(cost.get(minPoint) > cost.get(p)) {
				minPoint = p;
			}
		}
		return minPoint;
	}

	/**
	 * Get the list of neighbors of a node
	 * 
	 * @param node Node whose neigbors are searched
	 * @return List of neigbors
	 */

	public HashSet<Point> getListNeighbors(Point node) {
		return this.adjacencyList.get(node);
	}

	/**
	 * Get the point at the center of a side
	 * 
	 * @param side Side whose center is searched
	 * @return Center of the side
	 */

	public Point getCenterOfSide(Side side) {
		switch(side){
			case TOP : return new Point(this.getNbCols()/2,0);
			case BOTTOM : return new Point(this.getNbCols()/2,this.getNbRows()-1);
			case LEFT : return new Point(0,this.getNbRows()/2);
			case RIGHT : return new Point(this.getNbCols()-1,this.getNbRows()/2);
			default: return null;
		}
	}

	/**
	 * Get the set of points on the given side
	 * 
	 * @param sideDest Side where points are searched
	 * @return Set of points found on the given side
	 */

	public HashSet<Point> getPointsSetFromSide(Side sideDest) {
		HashSet<Point> pointsSet = new HashSet<Point>(9);
		switch(sideDest) {
			case LEFT:
				for(int i=0; i<this.getNbRows(); i++) {
					pointsSet.add(new Point(0,i));
				}
				break;
			case RIGHT:
				for(int i=0; i<this.getNbRows(); i++) {
					pointsSet.add(new Point(this.getNbCols()-1,i));
				}
				break;
			case TOP:
				for(int i=0; i<this.getNbRows(); i++) {
					pointsSet.add(new Point(i,0));
				}
				break;
			case BOTTOM:
				for(int i=0; i<this.getNbRows(); i++) {
					pointsSet.add(new Point(i,this.getNbRows()-1));
				}
				break;
			default:;
		}
		return pointsSet;
	}

	/**
	 * Function to determine if there is a path that connects a point to the side of the grid it must reach
	 * 
	 * @param start Node from where the path starts
	 * @param sideDest Side where the path must go to
	 * @return Boolean value equals to true if a path exists and false if it doesn't
	 */

	public boolean existPath(Point start, Side sideDest) {
		LinkedList<Point> nodesToBeExpanded = new LinkedList<Point>();
		HashMap<Point,Point> parents = new HashMap<Point,Point>();
		HashMap<Point,Integer> distTo = new HashMap<Point,Integer>();
		HashMap<Point,Integer> cost = new HashMap<Point,Integer>();
		Point currentNode = start;
		Point destinationApprox = null;
		int distToNeighbor = 0;
		HashSet<Point> pointsOnSideDest = null;
		
		pointsOnSideDest = getPointsSetFromSide(sideDest);
		destinationApprox = this.getCenterOfSide(sideDest);

		for(Point p : this.getNodesList()) {
			parents.put(p,null);
			distTo.put(p, Integer.MAX_VALUE);
			cost.put(p, Integer.MAX_VALUE);
		}

		nodesToBeExpanded.add(start);
		distTo.replace(start,0);
		cost.replace(start,costFunctionPath(start, destinationApprox));
		
		while(!nodesToBeExpanded.isEmpty()) {
			currentNode = getMinPoint(nodesToBeExpanded, cost);
			if(pointsOnSideDest.contains(currentNode)) {
				return true;
			}

			nodesToBeExpanded.remove(currentNode);
			for(Point neighbor : getListNeighbors(currentNode)) {
				distToNeighbor = distTo.get(currentNode) + 1; // weight is 1 for all edges
				if(distToNeighbor < distTo.get(neighbor)) {
					// Update distTo and path if the new distance is smaller
					parents.replace(neighbor, currentNode);
					distTo.replace(neighbor, distToNeighbor);
					cost.replace(neighbor, distToNeighbor + costFunctionPath(neighbor, destinationApprox));
					
					if(!nodesToBeExpanded.contains(neighbor)) {
						nodesToBeExpanded.add(neighbor);
					}
				}
			}
		}
		return false;
	}
}