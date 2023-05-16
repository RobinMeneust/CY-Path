import java.util.LinkedList;
import java.util.Scanner;

public class Board {
	private int nbCols;
	private int nbRows;
	private int size;
	private Grid grid;
	private Game game;
	private Fence[] fences;
	private int nbPlacedFences;
	private Pawn[] pawns;
	
	public Board(int nbCols, int nbRows, Game game) {
		this.nbCols = nbCols;
		this.nbRows = nbRows;
		this.size = nbCols * nbRows;
		this.grid = new Grid(nbRows, nbCols);
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

	public Grid getGrid(){
		return grid;
	}

	public void choosePosition(Scanner scanner, Point chosenPos){
		System.out.println();
		System.out.print("X : ");
		int x = Integer.parseInt(scanner.nextLine());
		System.out.println();
		System.out.print("Y : ");	
		int y = Integer.parseInt(scanner.nextLine());

		chosenPos.setX(x);
		chosenPos.setY(y);
	}

	public String chooseOrientation(Scanner scanner){
		String orientation = scanner.next();
		return orientation;
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
		System.out.println("_____________");
		System.out.println(position);
		System.out.println(positionTested);
		System.out.println(positionTested2);

		if(this.isOnTheBoard(positionTested)){
			System.out.println("pos1 in board");
			//We check if the current position and the tested position are not separated by a fence
			if(this.grid.areConnected(position,positionTested)){
				System.out.println("no fence");
				
				if(this.isPawnAtPos(positionTested)){
					// There is a pawn so we can't go there, but we can maybe jump above it
					System.out.println("there is a pawn here");
					if(this.isOnTheBoard(positionTested2)){
						System.out.println("pos2 in board");
						if(this.grid.areConnected(position,positionTested2)){
							System.out.println("no fence for pos2");
							return positionTested2;
						}
					}
				}
				else{
					System.out.println("no pawn");
					return positionTested;
				}
			}
		}

		return position;
	}

	public LinkedList<Point> listPossibleMoves(Point position){
		LinkedList<Point> listPossibleMovements = new LinkedList<Point>();
		
		Point positionTested = null;
		Point positionTested2 = null;
		Point trans = null;
		
		//We test the bottom position
		positionTested = new Point(position.getX(), position.getY() + 1);
		positionTested2 = new Point(position.getX(), position.getY() + 2);
		
		trans = this.possibleMove(position,positionTested,positionTested2);
		//if we can move to a position other than the initial position in this direction, we add it to the list
		if(!trans.equals(position)){
			listPossibleMovements.add(trans);
		}
		
		//We test the top position
		positionTested = new Point(position.getX(), position.getY() - 1);
		positionTested2 = new Point(position.getX(), position.getY() - 2);

		trans = this.possibleMove(position,positionTested,positionTested2);
		if(!trans.equals(position)){
			listPossibleMovements.add(trans);
		}

		//We test the left position
		positionTested = new Point(position.getX() - 1, position.getY());
		positionTested2 = new Point(position.getX() - 2, position.getY());
		trans = this.possibleMove(position,positionTested,positionTested2);
		if(!trans.equals(position)){
			listPossibleMovements.add(trans);
		}

		//We test the right position
		positionTested = new Point(position.getX() + 1, position.getY());
		positionTested2 = new Point(position.getX() + 2, position.getY());

		trans = this.possibleMove(position,positionTested,positionTested2);
		if(!trans.equals(position)){
			listPossibleMovements.add(trans);
		}
		return listPossibleMovements;
    }

	public Pawn getPawnAtPos(Point pos) {
		for(int i=0; i<this.pawns.length; i++) {
			if(this.pawns[i].getPosition().equals(pos)) {
				return this.pawns[i];
			}
		}
		return null;
	}

	public String getCellContentText(int i, int j) {
		Pawn pawn = getPawnAtPos(new Point(i,j));
		if(pawn != null) {
			return ""+pawn.getId();
		}
		return " ";
	}

	public void displayBoard() {
		System.out.println();
		System.out.print("    ");
		for(int i=0; i<nbCols; i++) {
			System.out.printf("%3d",i);
		}
		System.out.println();
		System.out.print("    ");
		for(int j=0; j<nbCols; j++)
			System.out.print("___");
		System.out.println();

		for(int i=0; i<nbRows; i++) {
			System.out.printf("%3d |",i);
			for(int j=0; j<nbCols; j++) {
				System.out.print(getCellContentText(i,j));
				if(grid.areConnected(i, j, i, j+1)) {
					System.out.print("||");
				} else {
					System.out.print(" |");
				}
			}
			System.out.println();
			System.out.print("    ");
			for(int j=0; j<nbCols; j++)
				System.out.print("___");
			System.out.println();
		}
	}

	public void play(int pawnId) {
		Scanner scanner = new Scanner(System.in);
		Point point = new Point();
		String response;
		String orientation = "";

		if(this.pawns[pawnId].getAvailableFences() == 0){
			response = "move";
		} else{
			do {
				System.out.println("What is your next action ? ('m' (move) or 'f' (place fence))");
				response = scanner.nextLine();
				response = response.toUpperCase();
			}while(!response.equals("M") && !response.equals("F"));
		}

		if(response.equals("M")){
			LinkedList<Point> possibleMoves = listPossibleMoves(this.pawns[pawnId].getPosition());
			System.out.println("Those are the possible moves you can do:");
			System.out.println(possibleMoves);
			
			System.out.println("Where do you want to go ?");
			this.choosePosition(scanner, point);
			
			this.pawns[pawnId].setPosition(point);
		} else if(response.equals("F")) {
			Fence fence = new Fence(2);
			do {
				System.out.println("Where do you want to put your fence ? (X,Y)");
				this.choosePosition(scanner, point);
				if(!(this.isOnTheBoard(point))){
					System.out.println("The fence is out of the board.\nTry again.");
				}
			} while(!(this.isOnTheBoard(point)));

			fence.setStart(point);

			do {
				System.out.println("What is the orientaion of your fence ? (H(ORIZONTAL) or V(ERTICAL))");
				orientation = this.chooseOrientation(scanner);
				if(!(this.isValidOrientation(orientation))){
					System.out.println("The fence is in the wrong orientation.\nTry again.");
				}
			}while(!(this.isValidOrientation(orientation)));

			fence.setOrientation(orientation);
			fence.setEnd(fence.getStart());

			System.out.println(fence);


			this.pawns[pawnId].setAvailableFences(this.getAvailableFences() - 1);
			// methode placer fence 
		}
	}

	public boolean isValidOrientation(String orientation) {
		if (orientation.toUpperCase().matches("H(ORIZONTALE)?")){
			return true;
		}else if(orientation.toUpperCase().matches("V(ERTICAL)?")){
			return true;
		}
		return false;
	}
}
