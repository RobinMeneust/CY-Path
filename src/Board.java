public class Board {
	private int nbCols;
	private int nbRows;
	private int size;
	private UndirectedGraph graph;
	private Game game;
	private Fence[] fences;
	private int nbPlacedFences;
	
	public Board(int nbCols, int nbRows, Game game) {
		this.nbCols = nbCols;
		this.nbRows = nbRows;
		this.size = nbCols * nbRows;
		this.graph = new UndirectedGraph(size);
		this.game = game;
		if(game != null){
			this.fences = new Fence[game.getNbFences()];
		}
		this.nbPlacedFences = 0;
	}

	public int getSize() {
		return this.size;
	}

	public int getNbCols() {
		return nbCols;
	}

	public int getNbRows() {
		return nbRows;
	}

	public int getAvailableFences() {
		return this.game.getNbFences() - this.nbPlacedFences;
	}	
}
