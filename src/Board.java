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

	public void choosePosition(Scanner scanner, Point chosenPos){
		System.out.print("X : ");
		int x = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Y : ");	
		int y = scanner.nextInt();

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
		int row = 0;

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

		for(int i=0; i<size-nbCols; i+=nbCols) {
			System.out.printf("%3d |",row);
			for(int j=0; j<nbCols-1; j++) {
				if(graph.areConnected(i, j+1)) {
					System.out.print(getCellContentText(row,j)+"||");
				} else {
					System.out.print(getCellContentText(row,j)+" |");
				}
			}
			System.out.println(getCellContentText(row,nbCols-1)+" |");
			System.out.print("    ");
			for(int j=0; j<nbCols; j++)
				System.out.print("___");
			System.out.println();
			row++;
		}
		System.out.printf("%3d |",row);
		for(int j=0; j<nbCols-1; j++) {
			if(graph.areConnected(size-nbCols, j+1)) {
				System.out.print(getCellContentText(row,j)+"||");
			} else {
				System.out.print(getCellContentText(row,j)+" |");
			}
		}
		System.out.print(getCellContentText(row,nbCols-1)+" |");
		System.out.println();
		System.out.print("    ");
		for(int j=0; j<nbCols; j++)
			System.out.print("___");
		System.out.println();
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
			LinkedList<Point> possibleMove = listPossibleMove(this.pawns[pawnId].getPosition());

			System.out.println("Those are the possible moves you can do:");
			System.out.println(possibleMove.toString());
			
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

	private boolean isValidOrientation(String orientation) {
		if (orientation.toUpperCase().matches("H(ORIZONTALE)?")){
			return true;
		}else if(orientation.toUpperCase().matches("V(ERTICAL)?")){
			return true;
		}
		return false;
	}
}
