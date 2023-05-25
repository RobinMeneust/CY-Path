package abstraction;

/*
 * Importing java classes needed for the Grid class
 * 
 * Importing classes from the java.util package
 */

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;


/**
 * Grid using as a data structure an undirected graph using an adjacency list
 * It contains a grid of nbRows x nbCols cells.
 * Each cell is an adjacency list containing all the cells connected to this one.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Grid {
	/**
	 * Hashmap of a position associated with its adjacent position
	 */
	private HashMap<Point,HashSet<Point>> adjacencyList;
	/**
	 * Number of rows
	 */
	private int nbRows;
	/**
	 * Number of columns
	 */
	private int nbCols;
	/**
	 * Constructor of the Grid
	 * 
	 * @param nbRows Number of rows in the grid
	 * @param nbCols Number of columns in the grid
	 */

	public Grid(int nbRows, int nbCols) {
		this.nbRows = nbRows;
		this.nbCols = nbCols;
		this.adjacencyList = new HashMap<Point,HashSet<Point>>();

		Point p = null;
		Point neighbor = null;
		HashSet<Point> neighborsList = null;
		for(int i=0; i<nbRows; i++){
			for(int j=0; j<nbCols; j++){
				p = new Point(j,i);
				neighborsList = new HashSet<Point>(4);
				if(i>0) {
					// Top neighbor
					neighbor = new Point(j,i-1);
					neighborsList.add(neighbor);
				}
				if(i<nbRows-1) {
					// Bottom neighbor
					neighbor = new Point(j,i+1);
					neighborsList.add(neighbor);
				}
				if(j>0) {
					// Left neighbor
					neighbor = new Point(j-1,i);
					neighborsList.add(neighbor);
				}
				if(j<nbCols-1) {
					// Right neighbor
					neighbor = new Point(j+1,i);
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
		if(p1 != null && p2 != null && this.adjacencyList != null) {
			this.adjacencyList.get(p1).add(p2);
			this.adjacencyList.get(p2).add(p1);
		}
	}

	/**
	 * Remove an edge between two nodes
	 * 
	 * @param p1 Coordinates of the first node
	 * @param p2 Coordinates of the second node
	 */

	public void removeEdge(Point p1, Point p2) {
		if(p1 != null && p2 != null && this.adjacencyList != null) {
			this.adjacencyList.get(p1).remove(p2);
			this.adjacencyList.get(p2).remove(p1);
		}
	}

	/**
	 * Display the grid: display all nodes and for each node it displays all of its neighbors
	 */

	public void displayGrid() {
		for(int i=0; i<this.getNbRows(); i++) {
			for(int j=0; j<this.getNbCols(); j++) {
				System.out.println("Node ("+j+","+i+") : ");
				for(Point p : this.getListNeighbors(new Point(j,i))) {
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
	 * @return True if two node are connected, false otherwise
	 */

	public boolean areConnected(int x1, int y1, int x2, int y2) {
		Point p1 = new Point(x1,y1);
		Point p2 = new Point(x2,y2);
		return areConnected(p1,p2);
	}

	/**
	 * Get the distance between a point and a Side
	 * 
	 * @param start Point whose distance is calculated
	 * @param destination Targeted side
	 * @return Distance
	 */

	public int getDistanceToSide(Point start, Side destination) {
		switch(destination) {
			case TOP: return start.getY();
			case BOTTOM: return this.getNbRows() - 1 - start.getY();
			case LEFT: return start.getX();
			case RIGHT: return this.getNbRows() - 1 - start.getX();
			default: return Integer.MAX_VALUE;
		}
	}

	/**
	 * Get the approximate cost of a path between a start and a destination
	 * 
	 * @param start Node where the path starts
	 * @param destination Side where the path ends
	 * @return Cost of the path
	 */

	public int costFunctionPath(Point start, Side destination) {
		return getDistanceToSide(start,destination);
	}

	/**
	 * Get the list of neighbors of a node
	 * 
	 * @param node Node whose neighbors are searched
	 * @return Set of neighbors from a node
	 */

	public HashSet<Point> getListNeighbors(Point node) {
		return this.adjacencyList.get(node);
	}

	/**
	 * Display the path in contained in the parameter 'parents' starting from the point 'end'
	 * 
	 * @param end The last point of the path
	 * @param parents The map containing the path. Each point is associated to its predecessor in the path
	 */

	public void displayPath(Point end, HashMap<Point,Point> parents) {
		if(parents == null) {
			return;
		}
		System.out.println();
		
		System.out.print("    ");
		for(int i=0; i<nbCols; i++) {
			System.out.printf("%3d ",i);
		}

		System.out.println();
		System.out.print("    ");
		for(int x=0; x<nbCols; x++){
			System.out.print("|---");
		}
		System.out.print("|");
		System.out.println();

		HashMap<Point,Integer> cellContent = new HashMap<Point,Integer>();

		Point p = end;
		int id = 0;
		while(p != null) {
			cellContent.put(p, Integer.valueOf(id));
			p = parents.get(p);
			id++;
		}


		for(int y=0; y<nbRows; y++) {
			System.out.printf("%3d |",y);

			for(int x=0; x<nbCols; x++) {
				Integer content = cellContent.get(new Point(x,y));
				if(content != null) {
					System.out.printf("%3d", content);
				} else {
					System.out.print("   ");
				}
				
				if(x==nbCols-1 || this.areConnected(x, y, x+1, y)) {
					// It's the right border or there is no vertical fence between (x,y) and (x+1,y)
					System.out.print("|");
				} else {
					// There is a vertical fence between (x,y) and (x+1,y)
					System.out.print("@");
				}
			}

			// bottom border
			System.out.println();
			System.out.print("    ");

			for(int x=0; x<nbCols; x++){
				if(y == nbRows-1 || this.areConnected(x, y, x, y+1)) {
					System.out.print("|---");
				} else {
					System.out.print("|@@@");
				}
			}
			System.out.print("|");
			System.out.println();
		}
	}

	/**
	 * Function to determine if there is a path that connects a point to the side of the grid it must reach
	 * 
	 * @param start Node from where the path starts
	 * @param sideDest Side where the path must go to
	 * @return Boolean value equals to true if a path exists and false if it doesn't
	 */

	public boolean existPath(Point start, Side sideDest) {
		PriorityQueue<PointWithCost> nodesToBeExpanded = new PriorityQueue<PointWithCost>();
		HashMap<Point,Point> parents = new HashMap<Point,Point>();
		HashMap<Point,Integer> distTo = new HashMap<Point,Integer>();
		Point currentNode = start;
		int distToNeighbor = 0;

		for(Point p : this.getNodesList()) {
			parents.put(p,null);
			distTo.put(p, Integer.MAX_VALUE);
		}

		nodesToBeExpanded.add(new PointWithCost(start, costFunctionPath(start, sideDest)));
		distTo.replace(start,0);
		
		while(!nodesToBeExpanded.isEmpty()) {
			currentNode = nodesToBeExpanded.poll();
			if(getDistanceToSide(currentNode, sideDest) == 0) {
				return true;
			}

			nodesToBeExpanded.remove(currentNode);
			for(Point neighbor : getListNeighbors(currentNode)) {
				distToNeighbor = distTo.get(currentNode) + 1; // weight is 1 for all edges so we just add 1
				if(distToNeighbor < distTo.get(neighbor)) {
					// Update distTo and path if the new distance is smaller
					parents.replace(neighbor, currentNode);
					distTo.replace(neighbor, distToNeighbor);
					
					PointWithCost newPointWithCost = new PointWithCost(neighbor, distToNeighbor + costFunctionPath(neighbor, sideDest));
					if(!nodesToBeExpanded.contains(newPointWithCost)) {
						nodesToBeExpanded.add(newPointWithCost);
					}
				}
			}
		}
		return false;
	}
}