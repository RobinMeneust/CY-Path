import java.util.HashSet;
import java.util.PriorityQueue;

public class UndirectedGraph {
	private boolean[][] adjacencyMatrix;
	private int nbRows;
	private int nbCols;
	private int nbNodes;

// TODO
// Use an adjacency list instead

	public UndirectedGraph(int nbNodes, int nbRows, int nbCols) {
		this.adjacencyMatrix = new boolean[nbNodes][nbNodes];
		this.nbNodes = nbNodes;
		for(int i=0; i<nbNodes; i++){
			for(int j=0; j<nbNodes; j++){
				this.adjacencyMatrix[i][j] = false;
			}
		}
	}

	public void addEdge(int node1, int node2) {
		this.adjacencyMatrix[node1][node2] = true;
		this.adjacencyMatrix[node2][node1] = true;
	}

	public void removeEdge(int node1, int node2) {
		this.adjacencyMatrix[node1][node2] = false;
		this.adjacencyMatrix[node2][node1] = false;
	}

	public boolean areConnected(int node1, int node2) {
		return this.adjacencyMatrix[node1][node2];
	}

	public boolean areConnected(Point p1, Point p2) {
		int node1 = p1.getX() + p1.getY();
		int node2 = p2.getX() + p2.getY();
		return this.areConnected(node1,node2);
	}

	public int getNbNodes() {
		return this.nbNodes;
	}

	// destination = middle of row or column (e.g if the real destination is (0,0),(0,1),(0,2) then (0,1) is the destination point)
	public int costFunctionPath(int node, Point destination) {
		Point p = new Point(node/nbRows, node%nbCols);
		return Point.getDistance(p,destination);
	}

	public int getIndexOfMin(PriorityQueue<Integer> queue, int cost[]) {
		int indexMin = queue.peek();
		for(Integer i : queue) {
			if(cost[indexMin] > cost[i]) {
				indexMin = i;
			}
		}
		return indexMin;
	}

	public HashSet<Integer> getListNeighbors(int node) {
		HashSet<Integer> neighbors = new HashSet<>();

		for(int i=0; i<this.getNbNodes(); i++) {
			if(areConnected(node, i)) {
				neighbors.add(i);
			}
		}

		return neighbors;
	}

	public int getDegree(int node) {
		int degree = 0;
		for(int i=0; i<this.getNbNodes(); i++) {
			if(areConnected(node, i)) {
				degree++;
			}
		}
		return degree;
	}

	public boolean existPath(int start, int end, Point destination){
		PriorityQueue<Integer> nodesToBeExpanded = new PriorityQueue<Integer>();
		int[] parents = new int[this.getNbNodes()];
		int[] distTo = new int[this.getNbNodes()];
		int[] cost = new int[this.getNbNodes()];
		int currentNode = start;
		int distToNeighbor = 0;

		for(int i=0; i<parents.length; i++) {
			parents[i] = -1;
			distTo[i] = Integer.MAX_VALUE;
			cost[i] = Integer.MAX_VALUE;
		}

		nodesToBeExpanded.add(start);
		distTo[start] = 0;
		cost[start] = costFunctionPath(start, destination);

		while(!nodesToBeExpanded.isEmpty()) {
			currentNode = getIndexOfMin(nodesToBeExpanded, cost);
			if(currentNode == end) {
				return true;
			}

			nodesToBeExpanded.remove(currentNode);
			for(Integer neighbor : getListNeighbors(currentNode)) {
				distToNeighbor = distTo[currentNode] + 1; // weight is 1 for all edges
				if(distToNeighbor < distTo[distToNeighbor]) {
					// Update distTo and path if the new distance is smaller
					parents[neighbor] = currentNode;
					distTo[neighbor] = distToNeighbor;
					cost[neighbor] = distToNeighbor + costFunctionPath(neighbor, destination);
					
					if(!nodesToBeExpanded.contains(neighbor)) {
						nodesToBeExpanded.add(neighbor);
					}
				}
			}	
		}
		return false;
	}
}