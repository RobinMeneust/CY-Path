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
		this.graph = new UndirectedGraph(size, nbRows, nbCols);
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

	public void choosePosition(Point positionChose){
		Scanner scanner = new Scanner(System.in);

		System.out.println("X : ");
		positionChose.setX(scanner.nextInt());

		System.out.println("Y : ");
		positionChose.setY(scanner.nextInt());
		scanner.close();
	}

	public boolean isOnTheBoard(Point position){
        return position.getX() <= this.getNbCols() && position.getX() >= 0 && position.getY() >= 0 && position.getY() <= this.getNbRows();
    }

	public boolean isPawnAtPos(Point position){

		for(int i = 0; i < this.getGame().getNbPlayers(); i++){
			if(this.pawns[i].getPosition().equals(position)){
				return true;
			}
		}
		return false;
	}

	public Point possibleMove(Point position, Point positionTested, Point positionTested2){

		//we check if the position is on the board
		if(this.isOnTheBoard(positionTested)){
			//We check if the current position and the tested position are not separated by a fence
			if(this.graph.areConnected(position,positionTested)){

				
				if(this.isPawnAtPos(positionTested)){

					if(this.isOnTheBoard(positionTested2)){
						if(this.graph.areConnected(position,positionTested2) && !this.isOnTheBoard(positionTested2)){
							
							return positionTested2;
						}
					}
				}
				else{
					return positionTested;
				}
			}
		}

		return position;
	}

	public LinkedList<Point> listPossibleMove(Point position){

		LinkedList<Point> listPossibleMovement = new LinkedList<Point>();
	
		Point positionTested;
		Point positionTested2;
		Point trans;

		//We test the top position
		positionTested = new Point(position.getX(), position.getY() + 1);
		positionTested2 = new Point(position.getX(), position.getY() + 2);

		trans = this.possibleMove(position,positionTested,positionTested2);
		//if we can move to a position other than the initial position in this direction, we add it to the list
		if(!trans.equals(position)){
			listPossibleMovement.add(trans);
		}

		//We test the down position
		positionTested = new Point(position.getX(), position.getY() - 1);
		positionTested2 = new Point(position.getX(), position.getY() - 2);

		trans = this.possibleMove(position,positionTested,positionTested2);
		if(!trans.equals(position)){
			listPossibleMovement.add(trans);
		}

		//We test the right position
		positionTested = new Point(position.getX() - 1, position.getY());
		positionTested2 = new Point(position.getX() - 2, position.getY());
		trans = this.possibleMove(position,positionTested,positionTested2);
		if(!trans.equals(position)){
			listPossibleMovement.add(trans);
		}

		//We test the left position
		positionTested = new Point(position.getX() + 1, position.getY());
		positionTested2 = new Point(position.getX() + 2, position.getY());

		trans = this.possibleMove(position,positionTested,positionTested2);
		if(!trans.equals(position)){
			listPossibleMovement.add(trans);
		}

		return listPossibleMovement;
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
			LinkedList<Point> possibleMove = listPossibleMove(this.pawns[pawnId].getPosition());

			System.out.println("This is your possibilities to move :");
			System.out.println(possibleMove.toString());
			
			System.out.println("Where do you want to go ?");
			this.choosePosition(point);
			
			this.pawns[pawnId].setPosition(point);
		} else {
			do {
				System.out.println("Where do you want to put your fence ? (X,Y)");
				this.choosePosition(point);

				if(this.isOnTheBoard(point)){
					System.out.println("Error : fence out of board");
				}

				if(this.isOverlapped(point.getX()) && this.isOverlapped(point.getX() + 1)){
					System.out.println("Error : fence is overlapping another fence");
				}

			} while(this.isOnTheBoard(point) && this.isOverlapped(point.getX()) && this.isOverlapped(point.getX() + 1));
			
			this.pawns[pawnId].setAvailableFences(this.getAvailableFences() - 1);
			// methode placer fence 
		}
		scanner.close();
	}

	public boolean isOverlapped(int node){
		return graph.getDegree(node) < 3;
	}
}
