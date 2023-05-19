
/**
 * Importing java classes needed for the Board class
 * 
 * Importing classes from the java.util package
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Importing javafx classes needed for the Board class
 */
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault,
 *         KUSMIDER David, MENEUST Robin
 * 
 *         This class represents the game board that will be displayed on which
 *         players will play.
 */
public class Board {

	/**
	 * State the Board's class attributes
	 */
	private int nbCols;
	private int nbRows;
	private int size;
	private Grid grid;
	private Game game;
	private ArrayList<Fence> fences;
	private int nbPlacedFences;
	private Pawn[] pawns;
	private int pawnIdTurn;
	private int fenceLength;
	private StringProperty action;

	/**
	 * Create a game board from a number of rows, columns and a Game attribute
	 * 
	 * @param nbCols (int) : number of columns of the game board
	 * @param nbRows (int) : number of rows of the game board
	 * @param game   (Game) : attribut that contains the components of the current
	 *               party
	 */
	public Board(int nbCols, int nbRows, Game game) {
		this.nbCols = nbCols;
		this.nbRows = nbRows;
		this.size = nbCols * nbRows;
		this.grid = new Grid(nbRows, nbCols);
		this.game = game;
		this.fences = null;
		this.pawnIdTurn = 0;
		this.fenceLength = 2;
		this.action = new SimpleStringProperty("Move");

		if (game != null && game.getNbFences() > 0) {
			this.fences = new ArrayList<Fence>(game.getNbFences());
		}

		this.nbPlacedFences = 0;
		this.pawns = new Pawn[game.getNbPlayers()];
		int j = 0;
		for (int i = 0; i < pawns.length; i++) {
			pawns[i] = new Pawn(i, Side.values()[j], ColorPawn.values()[j], this, this.game.getPlayer(i));
			j++;
		}
	}

	/**
	 * Accessor to recover the size of a fence
	 * 
	 * @return (int) size : length of a fence
	 */
	public int getFenceLength() {
		return fenceLength;
	}

	/**
	 * Accessor to assign the length of a barrier under conditions
	 * 
	 * @param fenceLength (int) : size of a fence
	 */
	public void setFenceLength(int fenceLength) {
		if (fenceLength > 0 && fenceLength < nbCols && fenceLength < nbRows) {
			this.fenceLength = fenceLength;
		}
	}

	/**
	 * Accessor to recover the size of the game board
	 * 
	 * @return (int) size : number of cells
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Accessor to recover number of columns of the game board
	 * 
	 * @return (int) nbCols : number of columns
	 */
	public int getNbCols() {
		return nbCols;
	}

	/**
	 * Accessor to recover number of rows of the game board
	 * 
	 * @return (int) nbRows : number of rows
	 */
	public int getNbRows() {
		return nbRows;
	}

	/**
	 * Accessor to recover the number of remaining fences to place on the game board
	 * 
	 * @return (int) number : remaining fences to place
	 */
	public int getAvailableFences() {
		return this.game.getNbFences() - this.nbPlacedFences;
	}

	/**
	 * Accessor to retrieve information about the current game
	 * 
	 * @return (Game) game : attribute that contains elements of the current game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Accessor to recover the game board
	 * 
	 * @return (Grid) grid : the grid of the game board
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Accessor to recover a pawn placed on the game board
	 * 
	 * @param i (int) : index used to mark a pawn
	 * @return (Pawn) pawns[i] : a pawn placed on the game board
	 */
	public Pawn getPawns(int i) {
		return pawns[i];
	}

	/**
	 * Accessor to recover the id of the pawn belonging to the player who will play
	 *
	 * @return (int) pawnIdTurn : the current player’s pawn id
	 */
	public int getPawnIdTurn() {
		return this.pawnIdTurn;
	}

	/**
	 * Accessor to assign the pawn id matches the new player who will play
	 *
	 * @param id (int) : the new player's pawn id
	 */
	public void setPawnIdTurn(int id) {
		this.pawnIdTurn = id;
	}

	/**
	 * Accessor to retrieve a string that corresponds to a type of action to perform
	 *
	 * @return (StringProperty) action : a string property
	 */
	public StringProperty getAction() {
		return this.action;
	}

	/**
	 * Accessor to assign a type of action to perform
	 * 
	 * @param a (StringProperty) : string that corresponds to a type of action
	 */
	public void setAction(String a) {
		this.action.set(a);
	}

	/**
	 * Procedure to recover the position of a square on the game board in which the
	 * player wants to move his pawn
	 * 
	 * @param scanner   (Scanner) : recovers a value entered by the user
	 * @param chosenPos (Point) : recovers the position of a cell in which the
	 *                  player wants to move his pawn
	 */
	public void choosePosition(Scanner scanner, Point chosenPos) {
		System.out.println();

		System.out.print("X : ");
		int x = Integer.parseInt(scanner.next());
		System.out.println();
		System.out.print("Y : ");
		int y = Integer.parseInt(scanner.next());

		chosenPos.setX(x);
		chosenPos.setY(y);
	}

	/**
	 * Function to recover the direction in which the player wants to move his pawn
	 * 
	 * @param scanner (Scanner) : recovers a value entered by the user
	 * @return (String) orientation : user-chosen orientation
	 */
	public String chooseOrientation(Scanner scanner) {
		String orientation = scanner.next();
		return orientation;
	}

	/**
	 * Function to determine if the fence that the player wants to place is.
	 * contained in the game board
	 * 
	 * @param fence (Fence) : fence the player wants to place
	 * @return (boolean) : true if the fence is fully contained in the game board
	 *         else false
	 */
	public boolean isFenceOnTheBoard(Fence fence) {
		if (fence.getOrientation() == Orientation.HORIZONTAL) {
			return ((fence.getStart().getX() < this.getNbCols() && fence.getStart().getX() >= 0)
					&& (fence.getStart().getY() < this.getNbRows() && fence.getStart().getY() > 0)
					&& (fence.getEnd().getX() >= 0 && fence.getEnd().getX() <= this.getNbCols())
					&& (fence.getEnd().getY() > 0 && fence.getEnd().getY() < this.getNbRows()));
		} else {
			return ((fence.getStart().getX() < this.getNbCols() && fence.getStart().getX() > 0)
					&& (fence.getStart().getY() < this.getNbRows() && fence.getStart().getY() >= 0)
					&& (fence.getEnd().getX() > 0 && fence.getEnd().getX() < this.getNbCols())
					&& (fence.getEnd().getY() >= 0 && fence.getEnd().getY() <= this.getNbRows()));
		}
	}

	/**
	 * Function to determine if the coordinates of a cell belong to the game board.
	 * 
	 * @param position (Point) : contains the coordinates of a cell
	 * @return (boolean) : true if the game board contains this cell else false
	 */
	public boolean isOnTheBoard(Point point) {
		return point.getX() < this.getNbRows() && point.getX() >= 0 && point.getY() >= 0
				&& point.getY() < this.getNbCols();
	}

	/**
	 * Function to determine the presence of a pawn in a certain cell
	 * 
	 * @param position (Point) : contains the coordinates of a cell of the game
	 *                 board
	 * @return (boolean) : true if this cell of the game board contains a pawn else
	 *         false
	 */
	public boolean isPawnAtPos(Point position) {
		for (int i = 0; i < this.getGame().getNbPlayers(); i++) {
			if (this.pawns[i].getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Function to test different cells of the game board to see if the player can
	 * move his pawn in
	 * 
	 * @param position        (Point) : pawn coordinates during play
	 * @param positionTested  (Point) : coordinates of a cell to be tested
	 * @param positionTested2 (Point) : coordinates of a cell to be tested
	 * @param positionTested3 (Point) : coordinates of a cell to be tested
	 * @param positionTested4 (Point) : coordinates of a cell to be tested
	 * @return (LinkedList<Point>) : coordinates of cells passed as comments in
	 *         which the player can place his pawn
	 */
	public LinkedList<Point> possibleMove(Point position, Point positionTested, Point positionTested2,
			Point positionTested3, Point positionTested4) {
		LinkedList<Point> listMove = new LinkedList<Point>();

		if (this.isOnTheBoard(positionTested)) {
			// We check if the current position and the tested position are not separated by
			// a fence
			if (this.grid.areConnected(position, positionTested)) {

				if (this.isPawnAtPos(positionTested)) {
					// There is a pawn so we can't this.isPawnAtPos(positionTested)go there, but we
					// can maybe jump above it
					if (this.isOnTheBoard(positionTested2)) {

						if (this.grid.areConnected(positionTested, positionTested2)
								&& !this.isPawnAtPos(positionTested2)) {
							listMove.add(positionTested2);
						} else {
							// If there are a fence behind the pawns we check if we can go leftside or
							// rightside
							if (this.isOnTheBoard(positionTested3)) {
								if (this.grid.areConnected(positionTested, positionTested3)
										&& !this.isPawnAtPos(positionTested3)) {
									listMove.add(positionTested3);
								}
							}
							if (this.isOnTheBoard(positionTested4)) {
								if (this.grid.areConnected(positionTested, positionTested4)
										&& !this.isPawnAtPos(positionTested4)) {
									listMove.add(positionTested4);
								}
							}
						}
					}
				} else {
					listMove.add(positionTested);
				}
			}
		} else {
			listMove.add(position);
		}

		return listMove;
	}

	/**
	 * Function to create a list of the different cells in which the player can
	 * place his pawn
	 * 
	 * @param position (Point) : pawn coordinates during play
	 * @return (LinkedList<Point>) : cells coordinates in which the player can place
	 *         his pawn
	 */
	public LinkedList<Point> listPossibleMoves(Point position) {
		LinkedList<Point> listPossibleMovements = new LinkedList<Point>();

		Point positionTested = null;
		Point positionTested2 = null;
		Point positionTested3 = null;
		Point positionTested4 = null;
		LinkedList<Point> trans = null;

		// We test the bottom position
		positionTested = new Point(position.getX(), position.getY() + 1);
		positionTested2 = new Point(position.getX(), position.getY() + 2);
		positionTested3 = new Point(position.getX() + 1, position.getY() + 1);
		positionTested4 = new Point(position.getX() - 1, position.getY() + 1);

		trans = this.possibleMove(position, positionTested, positionTested2, positionTested3, positionTested4);
		// if we can move to a position other than the initial position in this
		// direction, we add it to the list
		if (!trans.contains(position)) {
			listPossibleMovements.addAll(trans);
		}

		// We test the top position
		positionTested = new Point(position.getX(), position.getY() - 1);
		positionTested2 = new Point(position.getX(), position.getY() - 2);
		positionTested3 = new Point(position.getX() + 1, position.getY() - 1);
		positionTested4 = new Point(position.getX() - 1, position.getY() - 1);

		trans = this.possibleMove(position, positionTested, positionTested2, positionTested3, positionTested4);
		if (!trans.contains(position)) {
			listPossibleMovements.addAll(trans);
		}

		// We test the left position
		positionTested = new Point(position.getX() - 1, position.getY());
		positionTested2 = new Point(position.getX() - 2, position.getY());
		positionTested3 = new Point(position.getX() - 1, position.getY() + 1);
		positionTested4 = new Point(position.getX() - 1, position.getY() - 1);

		trans = this.possibleMove(position, positionTested, positionTested2, positionTested3, positionTested4);
		if (!trans.contains(position)) {
			listPossibleMovements.addAll(trans);
		}

		// We test the right position
		positionTested = new Point(position.getX() + 1, position.getY());
		positionTested2 = new Point(position.getX() + 2, position.getY());
		positionTested3 = new Point(position.getX() + 1, position.getY() + 1);
		positionTested4 = new Point(position.getX() + 1, position.getY() - 1);

		trans = this.possibleMove(position, positionTested, positionTested2, positionTested3, positionTested4);
		if (!trans.contains(position)) {
			listPossibleMovements.addAll(trans);
		}
		return listPossibleMovements;
	}

	/**
	 * Function that returns a pawn if there is one in a certain cell
	 * 
	 * @param pos (Point) : coordinates of a cell
	 * @return (Pawn) : a pawn if the coordinates of a cell contains one,
	 *         otherwise return the null value
	 */
	public Pawn getPawnAtPos(Point pos) {
		for (int i = 0; i < this.pawns.length; i++) {
			if (this.pawns[i].getPosition().equals(pos)) {
				return this.pawns[i];
			}
		}
		return null;
	}

	/**
	 * Function to retrieve the id of a pawn in a cell as a character string
	 * 
	 * @param i : first component of a coordinate of a cell to be tested
	 * @param j : second component of a coordinate of a cell to be tested
	 * @return (String) : the id of a pawn contained in a cell if existing else a
	 *         empty string
	 */
	public String getCellContentText(int i, int j) {
		Pawn pawn = getPawnAtPos(new Point(i, j));
		if (pawn != null) {
			return "" + pawn.getId();
		}
		return " ";
	}

	/**
	 * Procedure to display the game board
	 */
	public void displayBoard(DisplayType type) {
		System.out.println();

		switch (type) {
			case COORD_CELL:
				System.out.print("    ");
				for (int i = 0; i < nbCols; i++) {
					System.out.printf("%3d ", i);
				}
				break;
			case COORD_LINE:
				System.out.print("  ");
				for (int i = 0; i <= nbCols; i++) {
					System.out.printf("%3d ", i);
				}
				break;
			default:
				;
		}
		System.out.println();
		if (type == DisplayType.COORD_LINE) {
			System.out.print("  0 ");
		} else {
			System.out.print("    ");
		}
		for (int x = 0; x < nbCols; x++) {
			System.out.print("|---");
		}
		System.out.print("|");
		System.out.println();

		for (int y = 0; y < nbRows; y++) {
			if (type == DisplayType.COORD_CELL) {
				System.out.printf("%3d | ", y);
			} else {
				System.out.printf("    | ", y);
			}

			for (int x = 0; x < nbCols; x++) {
				System.out.print(getCellContentText(x, y));
				if (x == nbCols - 1 || grid.areConnected(x, y, x + 1, y)) {
					// It's the right border or there is no vertical fence between (x,y) and (x+1,y)
					System.out.print(" | ");
				} else {
					// There is a vertical fence between (x,y) and (x+1,y)
					System.out.print(" @ ");
				}
			}

			// bottom border
			System.out.println();
			if (type == DisplayType.COORD_LINE) {
				System.out.printf("%3d ", y + 1);
			} else {
				System.out.print("    ");
			}
			for (int x = 0; x < nbCols; x++) {
				if (y == nbRows - 1 || grid.areConnected(x, y, x, y + 1)) {
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
	 * Function to add a fence to the current game
	 * 
	 * @param fence (Fence) : the fence to add to the game
	 */
	public void addFenceToData(Fence fence) {
		this.fences.add(fence);
		Point start = fence.getStart();
		Point end = fence.getEnd();

		if (fence.getOrientation() == Orientation.HORIZONTAL) {
			for (int i = start.getX(); i < end.getX(); i++) {
				this.grid.removeEdge(new Point(i, start.getY() - 1), new Point(i, start.getY()));
			}
		} else {
			for (int i = start.getY(); i < end.getY(); i++) {
				this.grid.removeEdge(new Point(start.getX() - 1, i), new Point(start.getX(), i));
			}
		}
	}

	/**
	 * Function to remove a fence to the current game
	 * 
	 * @param fence (Fence) : the fence to remove to the game
	 */
	public void removeFenceFromData(Fence fence) {
		this.fences.remove(fence);
		Point start = fence.getStart();
		Point end = fence.getEnd();

		if (fence.getOrientation() == Orientation.HORIZONTAL) {
			for (int i = start.getX(); i < end.getX(); i++) {
				this.grid.addEdge(new Point(i, start.getY() - 1), new Point(i, start.getY()));
			}
		} else {
			for (int i = start.getY(); i < end.getY(); i++) {
				this.grid.addEdge(new Point(start.getX() - 1, i), new Point(start.getX(), i));
			}
		}
	}

	/**
	 * Function to check if there is still a path for players to win the game
	 * 
	 * @return (boolean) : true if there is a path to win if not false
	 */
	public boolean existPathFromPlayerToWin() {
		if (this.pawns != null) {
			for (Pawn p : pawns) {
				if (!this.getGrid().existPath(p.getPosition(), p.getStartingSide().getOpposite())) {
					return false;
				}
			}
			return true;
		}
		return true;
	}

	/**
	 * Procedure to complete a player’s turn in CY-PATH game
	 * 
	 * @param pawnId (int) : id of the pawn of the current player
	 */
	public int play(int pawnId) {
		Scanner scanner = new Scanner(System.in);
		Point point = new Point();
		String orientation = "";
		this.setPawnIdTurn(pawnId);

		int winner = this.checkWin();
		if (winner != -1) {
			Player playerWinner = null;
			for (int i = 0; i < this.game.getNbPlayers(); i++) {
				if (pawns[winner].getId() == winner) {
					playerWinner = pawns[winner].getPlayer();
				}
			}
			System.out.println("The winner is " + playerWinner);
			this.game.setState(GameState.FINISHED);
			return winner;
		}

		this.displayBoard(DisplayType.NO_COORD);
		Platform.runLater(() -> this.setAction("Move"));

		System.out.println("Turn of player: " + this.pawns[pawnId].getPlayer());
		if (this.pawns[pawnId].getAvailableFences() != 0) {
			while (true) {
				System.out.println(
						"To choose your next action, click on the button so that its text correpsonds to what you want to do.\n Press 'yes' in the terminal to confirm your selection");
				String userInput = scanner.nextLine();

				if (userInput.equals("yes")) {
					break;
				}
			}
		}

		// If button set Move
		if ("Move".equals(this.getAction().get())) {
			this.displayBoard(DisplayType.COORD_CELL);

			if (this.pawns[pawnId].getAvailableFences() == 0) {
				System.out.println(
						"You have " + this.pawns[pawnId].getAvailableFences() + "fence remaining.\nYou can only move.");
			}
			LinkedList<Point> possibleMoves = listPossibleMoves(this.pawns[pawnId].getPosition());
			System.out.println("Those are the possible moves you can do:");
			System.out.println(possibleMoves);

			System.out.println("Where do you want to go ?");

			do {
				this.choosePosition(scanner, point);
			} while (!possibleMoves.contains(point));

			this.pawns[pawnId].setPosition(point);
		}
		// If button set Place Fence
		else if ("Place fence".equals(this.getAction().get())) {
			this.displayBoard(DisplayType.COORD_LINE);

			Fence fence = new Fence(this.getFenceLength());

			do {
				System.out.println("What is the orientation of your fence ? (H(ORIZONTAL) or V(ERTICAL))");
				orientation = this.chooseOrientation(scanner);
				if (!(this.isValidOrientation(orientation))) {
					System.out.println("The fence is in the wrong orientation.\nTry again.");
				} else {
					fence.setOrientation(orientation);
					break;
				}
			} while (true);

			boolean isFenceValid = false;
			do {
				System.out.println("Where do you want to put your fence ? (X,Y)");
				this.choosePosition(scanner, point);
				fence.setStart(point);
				fence.setEnd(fence.getStart());
				isFenceValid = isFencePositionValid(fence);
				if (!isFenceValid) {
					System.out.println(
							"The fence can't be placed here (Starting point:" + fence.getStart() + ").\nTry again.");
				}
			} while (!isFenceValid);

			this.pawns[pawnId].placeFence();
		}
		return winner;
	}

	/**
	 * 
	 * 
	 * @param fence
	 * @return
	 */
	public boolean isFencePositionValid(Fence fence) {
		if (this.isFenceOnTheBoard(fence) && !this.isFenceOverlapping(fence)) {
			this.addFenceToData(fence);
			if (this.existPathFromPlayerToWin()) {
				this.removeFenceFromData(fence);
				return true;
			} else {
				this.removeFenceFromData(fence);
			}
		}
		return false;
	}

	/**
	 * Function to check if the orientation of the fence chosen by the player is
	 * valid
	 * 
	 * @param orientation (String) : the name of the orientation to be tested
	 * @return (boolean) : true if the orientation of the fence is valid else false
	 */
	public boolean isValidOrientation(String orientation) {
		if (orientation.toUpperCase().matches("H(ORIZONTALE)?")) {
			return true;
		} else if (orientation.toUpperCase().matches("V(ERTICAL)?")) {
			return true;
		}
		return false;
	}

	/**
	 * Function to check if a player has won the game
	 * 
	 * @return (int) Pawn ID matches the player who won the game else -1
	 */
	public int checkWin() {
		for (int i = 0; i < this.game.getNbPlayers(); i++) {
			switch (this.pawns[i].getStartingSide()) {
				case BOTTOM:
					if (this.pawns[i].getPosition().getY() == 0) {
						return this.pawns[i].getId();
					}
					break;
				case TOP:
					if (this.pawns[i].getPosition().getY() == this.getNbCols() - 1) {
						return this.pawns[i].getId();
					}
					break;
				case LEFT:
					if (this.pawns[i].getPosition().getX() == this.getNbRows() - 1) {
						return this.pawns[i].getId();
					}
					break;
				case RIGHT:
					if (this.pawns[i].getPosition().getX() == 0) {
						return this.pawns[i].getId();
					}
					break;
				default:
					return -1;
			}
		}
		return -1;
	}

	/**
	 * Accessor to check if the fence to be added overlap others
	 * 
	 * @param fenceToBePlaced (Fence) : the fence we want to test to add
	 * @return (boolean) : true if the fence add overlap with existing ones
	 */
	public boolean isFenceOverlapping(Fence fenceToBePlaced) {
		if (this.fences != null) {
			for (Fence fence : this.fences) {
				// over each other
				if (fenceToBePlaced.getStart().equals(fence.getStart())
						&& fenceToBePlaced.getOrientation().equals(fence.getOrientation())) {
					return true;
				} else {
					switch (fenceToBePlaced.getOrientation()) {
						case HORIZONTAL:
							if (fence.getOrientation() == Orientation.HORIZONTAL
									&& fence.getStart().getY() == fenceToBePlaced.getStart().getY()) {
								for (int i = 0; i < fence.getLength(); i++) {
									if (fenceToBePlaced.getStart().getX() + i == fence.getStart().getX()) {
										return true;
									} else if (fenceToBePlaced.getEnd().getX() - 1 - i == fence.getEnd().getX() - 1) {
										return true;
									}
								}
							} else if (fence.getOrientation() == Orientation.VERTICAL) {
								if (fenceToBePlaced.getStart().getX() < fence.getStart().getX()
										&& fence.getStart().getX() < fenceToBePlaced.getEnd().getX()) {
									if (fence.getStart().getY() < fenceToBePlaced.getStart().getY()
											&& fenceToBePlaced.getStart().getY() < fence.getEnd().getY()) {
										return true;
									}
								}
							}
							break;
						case VERTICAL:
							if (fence.getOrientation() == Orientation.VERTICAL
									&& fence.getStart().getX() == fenceToBePlaced.getStart().getX()) {
								for (int i = 0; i < fence.getLength(); i++) {
									if (fenceToBePlaced.getStart().getY() + i == fence.getStart().getY()) {
										return true;
									} else if (fenceToBePlaced.getEnd().getY() - 1 - i == fence.getEnd().getY() - 1) {
										return true;
									}
								}
							} else if (fence.getOrientation() == Orientation.HORIZONTAL) {
								if (fence.getStart().getX() < fenceToBePlaced.getStart().getX()
										&& fenceToBePlaced.getStart().getX() < fence.getEnd().getX()) {
									if (fenceToBePlaced.getStart().getY() < fence.getStart().getY()
											&& fence.getStart().getY() < fenceToBePlaced.getEnd().getY()) {
										return true;
									}
								}
							}
							break;
						default:
							break;
					}
				}
			}
		}
		return false;
	}
}
