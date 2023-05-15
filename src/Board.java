import java.util.LinkedList;
import java.util.Scanner;

public class Board {
	private int nbCols;
	private int nbRows;
	private int size;
	private UndirectedGraph graph;
	private Game game;
	private Fence[] fences;
	private int nbPlacedFences;
	private Pawn[] pawns;
	
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
		this.pawns = new Pawn[game.getNbPlayers()];
		int j = 0;
		for(int i = 0; i < pawns.length; i++){
			pawns[i] = new Pawn(i, Side.values()[j], Color.values()[j], this);
			j++;
			if(game.getNbPlayers() == 2){
				j++;
			}
		}
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

	public Game getGame() {
		return game;
	}

	public boolean isOverlapping(int node){
		return graph.getDegree(node) < 3;
	}

	public boolean isOutOfBoard(Point position){
        return position.getX() <= this.getNbCols() && position.getX() >= 0 && position.getY() >= 0 && position.getY() <= this.getNbRows();
    }

	public void choosePosition(Point positionChose){
		Scanner scanner = new Scanner(System.in);

		System.out.println("X : ");
		positionChose.setX(scanner.nextInt());

		System.out.println("Y : ");
		positionChose.setY(scanner.nextInt());
	}

	public void play(int pawnId) {
		Scanner scanner = new Scanner(System.in);
		Point point = new Point();
		String response;

		if(this.pawns[pawnId].getAvailableFences() == 0){
			response = "move";
		} else{
			System.out.println("What is your next action ? (move or put corridor)");
			response = scanner.nextLine();
		}

		if(response.toUpperCase().equals("MOVE")){
			LinkedList<Point> possibleMove = listPossibleMove(this.pawns[pawnId].getPositition());

			System.out.println("This is your possibilities to move :");
			System.out.println(possibleMove.toString());
			
			System.out.println("Where do you want to go ?");
			this.choosePosition(point);
			
			this.pawns[pawnId].setPositition(point);
		} else {

			do {
				System.out.println("Where do you want to put your fence ? (X,Y)");
				this.choosePosition(point);

				if(this.isOutOfBoard(point)){
					System.out.println("Error : fence out of board");
				}

				if(this.isOverlapped(point.getX() && this.isOverlapped(point.getX() + 1))){
					System.out.println("Error : fence is overlapping another fence");
				}

			} while(this.isOutOfBoard(point) && this.isOverlapped(point.getX() && this.isOverlapped(point.getX() + 1)));
			
			this.pawns[pawnId].setAvailableFences(this.getAvailableFences() - 1);
			// methode placer fence 
		}
	}
}
