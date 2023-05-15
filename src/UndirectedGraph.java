public class UndirectedGraph {
	private boolean[][] adjacencyMatrix;

	public UndirectedGraph(int nbNodes) {
		this.adjacencyMatrix = new boolean[nbNodes][nbNodes];
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

	public boolean existPath(int start, int end){
		// TO-DO
		return false;
	}
}