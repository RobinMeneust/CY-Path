package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Grid {
	private HashMap<Point,HashSet<Point>> adjacencyList;
	private int nbRows;
	private int nbCols;
	private int nbNodes;

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

	public int getNbRows() {
		return nbRows;
	}

	public int getNbCols() {
		return nbCols;
	}

	public int getNbNodes() {
		return nbNodes;
	}

	public Set<Point> getNodesList() {
		return this.adjacencyList.keySet();
	}

	public void addEdge(Point p1, Point p2) {
		this.adjacencyList.get(p1).add(p2);
		this.adjacencyList.get(p2).add(p1);
	}

	public void removeEdge(Point p1, Point p2) {
		this.adjacencyList.get(p1).remove(p2);
		this.adjacencyList.get(p2).remove(p1);
	}


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

	public boolean areConnected(Point p1, Point p2) {
		if(p1 != null && p2 != null && this.adjacencyList.containsKey(p1))
			return this.adjacencyList.get(p1).contains(p2);
		else
			return false;
	}

	public boolean areConnected(int x1, int y1, int x2, int y2) {
		Point p1 = new Point(x1,y1);
		Point p2 = new Point(x2,y2);
		return areConnected(p1,p2);
	}

	// destination = middle of row or column (e.g if the real destination is (0,0),(0,1),(0,2) then (0,1) is the destination point)
	public static int costFunctionPath(Point node, Point destination) {
		return Point.getDistance(node,destination);
	}

	public static Point getMinPoint(PriorityQueue<Point> queue, HashMap<Point,Integer> cost) {
		Point minPoint = queue.peek();
		for(Point p : queue) {
			if(cost.get(minPoint) > cost.get(p)) {
				minPoint = p;
			}
		}
		return minPoint;
	}

	public HashSet<Point> getListNeighbors(Point node) {
		return this.adjacencyList.get(node);
	}

	public int getDegree(Point node) {
		return this.getListNeighbors(node).size();
	}

	public Point getCenterOfSide(Side side) throws UnknownSideException {
		switch(side){
			case TOP : return new Point(this.getNbCols()/2,0);
			case BOTTOM : return new Point(this.getNbCols()/2,this.getNbRows()-1);
			case LEFT : return new Point(0,this.getNbRows()/2);
			case RIGHT : return new Point(this.getNbCols()-1,this.getNbRows()/2);
			default: throw new UnknownSideException();
		}
	}

	public boolean existPath(Point start, Point end, Side sideDest) throws UnknownSideException{
		PriorityQueue<Point> nodesToBeExpanded = new PriorityQueue<Point>();
		HashMap<Point,Point> parents = new HashMap<Point,Point>();
		HashMap<Point,Integer> distTo = new HashMap<Point,Integer>();
		HashMap<Point,Integer> cost = new HashMap<Point,Integer>();
		Point currentNode = start;
		Point destination = null;
		int distToNeighbor = 0;

		try {
			destination = this.getCenterOfSide(sideDest);
		} catch(UnknownSideException e) {
			throw e;
		}

		for(Point p : this.getNodesList()) {
			parents.put(p,null);
			distTo.put(p, Integer.MAX_VALUE);
			cost.put(p, Integer.MAX_VALUE);
		}

		nodesToBeExpanded.add(start);
		distTo.replace(start,0);
		cost.replace(start,costFunctionPath(start, destination));

		while(!nodesToBeExpanded.isEmpty()) {
			currentNode = getMinPoint(nodesToBeExpanded, cost);
			if(currentNode == end) {
				return true;
			}

			nodesToBeExpanded.remove(currentNode);
			for(Point neighbor : getListNeighbors(currentNode)) {
				distToNeighbor = distTo.get(currentNode) + 1; // weight is 1 for all edges
				if(distToNeighbor < distTo.get(neighbor)) {
					// Update distTo and path if the new distance is smaller
					parents.replace(neighbor, currentNode);
					distTo.replace(neighbor, distToNeighbor);
					cost.replace(neighbor, distToNeighbor + costFunctionPath(neighbor, destination));
					
					if(!nodesToBeExpanded.contains(neighbor)) {
						nodesToBeExpanded.add(neighbor);
					}
				}
			}
			// TODO : Check if there is an infinite loop here
		}
		return false;
	}
}