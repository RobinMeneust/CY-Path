package abstraction; 

/*
 * Importing java classes needed for the Board class
 * 
 * Importing classes from the java.util package
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import presentation.CYPath;

/**
 * This class represents the game board on which players will play.
 * It contains functions used to check if the player action is allowed or not (e.g. move out of the grid, ...).
 * It also helps displaying the board in the terminal.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Board {
	/**
	 * Number of columns in the board
	 */	
	private int nbCols;
	/**
	 * Number of rows in the board
	 */	
	private int nbRows;
	/**
	 * Undirected graph used to check if a fence is between two cell
	 */	
	private Grid grid;
	/**
	 * Game playing on this board
	 */	
	private GameAbstract game;
	/**
	 * Fences placed on the board
	 */
	private ArrayList<Fence> fences;
	/**
	 * Pawns placed on the board
	 */
	private Pawn[] pawns;
	/**
	 * Length of the fences
	 */
	private int fenceLength;
	/**
	 * ID of the pawn that won. If there is no winner it's equal to -1
	 */
	private int winner;
	/**
	 * List of the possible moves for the current round (used to avoid rechecking the same position twice)
	 */
	private List<Point> currentPossibleMoves;
	
	/**
	 * Create a game board from a number of rows, columns and a Game
	 * 
	 * @param nbCols Number of columns of the game board
	 * @param nbRows Number of rows of the game board
	 * @param game Object extending GameAbstract, that contains the components of the current game (list of players, rules...)
	 * @param pawns Array of the pawns that are placed on the board
	 * @param fenceLength Length of the fences
	 * @throws InvalidBoardSizeException If the board size is incorrect (too small)
	 * @throws InvalidFenceLengthException If the fence length is incorrect (negative, equals to 0 or too large for the board)
	 */

	public Board(int nbCols, int nbRows, GameAbstract game, Pawn[] pawns, int fenceLength) throws InvalidBoardSizeException, InvalidFenceLengthException {
		if((pawns.length < 3 && (nbCols < 2 || nbRows < 2))
				|| (pawns.length >= 3 && (nbCols < 3 || nbRows < 3))
				|| nbCols>15 || nbRows>15) {
			throw new InvalidBoardSizeException();
		}

		if(fenceLength < 1 || fenceLength >= nbCols || fenceLength >= nbRows) {
			throw new InvalidFenceLengthException();
		}
		this.nbCols = nbCols;
		this.nbRows = nbRows;
		this.grid = new Grid(nbRows, nbCols);
		this.game = game;
		this.fences = null;
		this.fenceLength = fenceLength;
		this.winner = -1;

		this.fences = new ArrayList<Fence>(Math.max(0,game.getNbMaxTotalFences()));
		
		this.pawns = pawns;

		for(int i=0; i<pawns.length;i++) {
			pawns[i].setBoard(this);
		}
	}

	/**
	 * Get the number of pawns in this board
	 * 
	 * @return Number of pawns
	 */

	public int getNbPawns() {
		return this.pawns.length;
	}

	/**
	 * Get the size of a fence in this board
	 * 
	 * @return Length of a fence
	 */

	public int getFenceLength() {
		return fenceLength;
	}

	/**
	 * Get an array of the fences placed on this board
	 * 
	 * @return Array of fences
	 */

	public Fence[] getFencesArray() {
		if(this.fences != null){
			Fence[] fencesArray = new Fence[this.fences.size()];
			this.fences.toArray(fencesArray);
			return fencesArray;
		}
		return null;
	}

	/**
	 * Get an array of the pawns on this board
	 * 
	 * @return Array of pawns
	 */

	public Pawn[] getPawnsArray() {
		Pawn[] clone = new Pawn[this.pawns.length];
		try {
			for(int i=0; i<clone.length; i++) {
				clone[i] = (Pawn) pawns[i].clone();
			}
		} catch(CloneNotSupportedException e) {};
		return clone;
	}

	/**
	 * Get the number of columns of the game board
	 * 
	 * @return Number of columns
	 */

	public int getNbCols() {
		return nbCols;
	}

	/**
	 * Get the number of rows of the game board
	 * 
	 * @return Number of rows
	 */

	public int getNbRows() {
		return nbRows;
	}

	/**
	 * Get the Game element that is using this instance of Board. It contains information about the current game
	 * 
	 * @return Object extending GameAbstract, that contains elements about the current game
	 */

	public GameAbstract getGame() {
		return game;
	}

	/**
	 * Get the game grid
	 * 
	 * @return The grid of the game board (undirected graph)
	 */

	public Grid getGrid(){
		return grid;
	}

	/**
	 * Get a pawn from its index
	 * 
	 * @param i Index of the pawn that we are getting
	 * @return The pawn at the given index
	 * @throws IncorrectPawnIndexException If the pawn index is out of bounds
	 */

	public Pawn getPawn(int i) throws IncorrectPawnIndexException {
		if(i>=0 && i<this.getNbPawns()) {
			return pawns[i];
		} else {
			throw new IncorrectPawnIndexException();
		}
	}

	/**
	 * Get a string from the user that corresponds to the position where the player wants to move his pawn
	 * 
	 * @return User-chosen orientation
	 */

	public String chooseOrientation(){
		String orientation = CYPath.scanner.nextLine();
		return orientation;
	}

	/**
	 * Check if the given fence is on the board
	 * A fence that is on the board borders is considered out of the board
	 * 
	 * @param fence Fence that is checked
	 * @return Boolean value equals to true if the fence is in the game board, and false otherwise
	 */

	public boolean isFenceOnTheBoard(Fence fence){
		if(fence.getOrientation() == Orientation.HORIZONTAL){
			return ((fence.getStart().getX() < this.getNbCols() && fence.getStart().getX() >= 0) && (fence.getStart().getY() < this.getNbRows() && fence.getStart().getY() > 0) && (fence.getEnd().getX() >= 0 && fence.getEnd().getX() <= this.getNbCols()) && (fence.getEnd().getY() > 0 && fence.getEnd().getY() < this.getNbRows()));
		} else {
			return ((fence.getStart().getX() < this.getNbCols() && fence.getStart().getX() > 0) && (fence.getStart().getY() < this.getNbRows() && fence.getStart().getY() >= 0) && (fence.getEnd().getX() > 0 && fence.getEnd().getX() < this.getNbCols()) && (fence.getEnd().getY() >= 0 && fence.getEnd().getY() <= this.getNbRows()));
		}
    }

	/**
	 * Check if the given Point is on the board
	 * 
	 * @param point Point containing the coordinates of a cell
	 * @return Boolean value equals to true if the game board contains this cell and false otherwise
	 */

	public boolean isCellOnTheBoard(Point point){
		return point.getX() < this.getNbRows() && point.getX() >= 0 && point.getY() >= 0 && point.getY() < this.getNbCols();
	}

	/**
	 * Check if a pawn is at the given position
	 * 
	 * @param position Cell position checked
	 * @return Boolean value equals to true if the cell at the given position has a pawn on it, and false otherwise
	 */

	public boolean isPawnAtPos(Point position){
		try {
			for (int i = 0; i < this.getGame().getNbPlayers(); i++) {
				if (this.getPawn(i).getPosition().equals(position)) {
					return true;
				}
			}
		}catch (IncorrectPawnIndexException err){
			System.err.println(err);
		}
		return false;
	}
	
	/**
	 * Check all the cells where a pawn can move to, from its starting position in one round and return it as a list.
	 * Here the parameters are points arranged in a "T" shape
	 * 
	 * @param position Current pawn position
	 * @param posTested Position tested
	 * @param posBehindTested Position just after posTested (posTested is between position and posLeftTested)
	 * @param posLeftTested Position on the left of posTested
	 * @param posRightTested Position on the right of posTested
	 * @return List of cells coordinates where the pawn can move to but only in one direction (only one 'posTested')
	 * 
	 */

	public LinkedList<Point> possibleMove(Point position, Point posTested, Point posBehindTested, Point posLeftTested, Point posRightTested){
		LinkedList<Point> listMove = new LinkedList<Point>();
		/* 
		The given points are defined by considering the position and looking at one of the 4 directions (top,left,right,bottom).
		It's shaped like a "T"
		*/
		// We check if the tested position is on the board and if the current position and the tested position are not separated by a fence
		if(this.isCellOnTheBoard(posTested) && this.getGrid().areConnected(position,posTested)){
			if(this.isPawnAtPos(posTested)) {
				// There is a pawn, so we can't go there, but we can maybe jump above it
				if(this.isCellOnTheBoard(posBehindTested) && this.getGrid().areConnected(posTested,posBehindTested) && !this.isPawnAtPos(posBehindTested)){
					// The cell just behind is free
					listMove.add(posBehindTested);
				}
				else{
					// If there is a fence behind the pawn we check if we can go left side or right side
					if(this.isCellOnTheBoard(posLeftTested) && this.getGrid().areConnected(posTested,posLeftTested) && !this.isPawnAtPos(posLeftTested)){
						listMove.add(posLeftTested);
					}
					if(this.isCellOnTheBoard(posRightTested) && this.getGrid().areConnected(posTested,posRightTested) && !this.isPawnAtPos(posRightTested)){
						listMove.add(posRightTested);
					}
				}
			}
			else {
				// There is no pawn, so we can go there, and we can't go behind it
				listMove.add(posTested);
			}
		}

		return listMove;
	}

	/**
	 * Create a list of all the cells a pawn can move to from its current position
	 * 
	 * @param position Current pawn position
	 * @return List of cells coordinates where the pawn can move to
	 */

	public LinkedList<Point> listPossibleMoves(Point position){
		LinkedList<Point> listPossibleMovements = new LinkedList<Point>();
		/* 
		The following points are defined by considering the position and looking at one of the 4 directions (top,left,right,bottom).
		It's shaped like a "T"
		*/

		Point posTested = null;
		Point posBehindTested = null;
		Point posLeftTested = null;
		Point posRightTested = null;
		LinkedList<Point> allPossibleMoves = null; // List of all the possibles moves considering the 4 directions
		
		//We test the bottom position
		posTested = new Point(position.getX(), position.getY() + 1);
		posBehindTested = new Point(position.getX(), position.getY() + 2);
		posLeftTested = new Point(position.getX() + 1, position.getY() + 1);
		posRightTested = new Point(position.getX() - 1, position.getY() + 1);
		
		allPossibleMoves = this.possibleMove(position,posTested,posBehindTested,posLeftTested,posRightTested);
		//if we can move to a position other than the initial position in this direction, we add it to the list
		if(!allPossibleMoves.contains(position)){
			listPossibleMovements.addAll(allPossibleMoves);
		}

		// Right rotation
		for(int i=0; i<3; i++) {
			posTested = Point.rightRotation(posTested, position);
			posBehindTested = Point.rightRotation(posBehindTested, position);
			posLeftTested = Point.rightRotation(posLeftTested, position);
			posRightTested = Point.rightRotation(posRightTested, position);

			allPossibleMoves = this.possibleMove(position,posTested,posBehindTested,posLeftTested,posRightTested);
			if(!allPossibleMoves.contains(position)){
				listPossibleMovements.addAll(allPossibleMoves);
			}
		}
		return listPossibleMovements;
    }

	/**
	 * Get a pawn at the given coordinates on the board, if there is one
	 * 
	 * @param pos Coordinates of a cell
	 * @return Pawn in the given cell. It returns 0 if there is no pawn at the given position
	 */

	public Pawn getPawnAtPos(Point pos) {
		try {
			for (int i = 0; i < this.getNbPawns(); i++) {
				if (this.getPawn(i).getPosition().equals(pos)) {
					return this.getPawn(i);
				}
			}
		}catch (IncorrectPawnIndexException err){
			System.err.println(err);
		}
		return null;
	}

	/**
	 * Get the possible moves from the current pawn being played.
	 * @return List of possible moves from the current pawn.
	 * @throws IncorrectPawnIndexException If the pawn index is out of bounds
	 */
	public List<Point> getCurrentPossibleMoves() throws IncorrectPawnIndexException {
		if(this.currentPossibleMoves == null) {
			Pawn pawn = this.game.getCurrentPawn();
			this.setCurrentPossibleMoves(this.listPossibleMoves(pawn.getPosition()));
		}
		return currentPossibleMoves;
	}

	/**
	 * Check if the position wanted is a position possible for the pawn to move to.
	 * @param pos Position to test
	 * @return True if the position tested is valid for the pawn to move, false otherwise
	 * @throws IncorrectPawnIndexException If the pawn index is out of bounds
	 */
	public boolean isPawnPosValid(Point pos) throws IncorrectPawnIndexException {
		try {
			return this.getCurrentPossibleMoves().contains(pos);
		} catch (IncorrectPawnIndexException e) {
			throw e;
		}
	}

	/**
	 * Change the list of possible moves for the current pawn
	 * @param currentPossibleMoves List of possible move to set to the pawn
	 */
	private void setCurrentPossibleMoves(List<Point> currentPossibleMoves) {
		this.currentPossibleMoves = currentPossibleMoves;
	}

	/**
	 * Remove the list of possible moves for the current pawn
	 */
	public void clearCurrentPossibleMoves() {
		this.setCurrentPossibleMoves(null);
	}

	/**
	 * Get the id of the pawn in the given cell coordinates as a String, if there is any.
	 * 
	 * @param x X coordinate of the cell to be checked
	 * @param y Y coordinate of the cell to be checked
	 * @return ID of the pawn in the given cell. If there is none then it returns a single space ' '
	 */

	public String getCellContentText(int x, int y) {
		Pawn pawn = getPawnAtPos(new Point(x,y));
		if(pawn != null) {
			return ""+pawn.getId();
		}
		return " ";
	}

	/** 
	 * Display the current board with its pawns, fences and cells
	 * 
	 * @param type Type of display for the coordinates (coordinates aligned with the lines, cells or no coordinates for instance)
	 */

	public void displayBoard(DisplayType type) {
		System.out.println();
		
		switch(type){
			case COORD_CELL:
				System.out.print("    ");
				for(int i=0; i<this.getNbCols(); i++) {
					System.out.printf("%3d ",i);
				}
				break;
			case COORD_LINE:
				System.out.print("  ");
				for(int i=0; i<=this.getNbCols(); i++) {
					System.out.printf("%3d ",i);
				}
				break;
			default:;
		}
		System.out.println();
		if(type == DisplayType.COORD_LINE){
			System.out.print("  0 ");
		} else {
			System.out.print("    ");
		}
		for(int x=0; x<this.getNbCols(); x++){
			System.out.print("|---");
		}
		System.out.print("|");
		System.out.println();

		for(int y=0; y<this.getNbRows(); y++) {
			if(type == DisplayType.COORD_CELL){
				System.out.printf("%3d | ",y);
			} else {
				System.out.print("    | ");
			}

			for(int x=0; x<this.getNbCols(); x++) {
				System.out.print(getCellContentText(x,y));
				if(x==this.getNbCols()-1 || this.getGrid().areConnected(x, y, x+1, y)) {
					// It's the right border or there is no vertical fence between (x,y) and (x+1,y)
					System.out.print(" | ");
				} else {
					// There is a vertical fence between (x,y) and (x+1,y)
					System.out.print(" @ ");
				}
			}

			// bottom border
			System.out.println();
			if(type == DisplayType.COORD_LINE){
				System.out.printf("%3d ", y+1);
			} else {
				System.out.print("    ");
			}
			for(int x=0; x<this.getNbCols(); x++){
				if(y == this.getNbRows()-1 || this.getGrid().areConnected(x, y, x, y+1)) {
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
	 * Get the list of fences
	 * @return ArrayList of fences
	 */
	public ArrayList<Fence> getFences() {
		return fences;
	}

	/**
	 * Add a fence to the board
	 * 
	 * @param fence Fence added
	 */

	public void addFenceToData(Fence fence) {
		this.getFences().add(fence);
		Point start = fence.getStart();
		Point end = fence.getEnd();

		if(fence.getOrientation() == Orientation.HORIZONTAL) {
			for(int i=start.getX(); i<end.getX(); i++) {
				this.getGrid().removeEdge(new Point(i,start.getY()-1), new Point(i,start.getY()));
			}
		} else {
			for(int i=start.getY(); i<end.getY(); i++) {
				this.getGrid().removeEdge(new Point(start.getX()-1, i), new Point(start.getX(), i));
			}
		}
	}

	/**
	 * Remove a fence from the current game
	 * 
	 * @param fence Fence removed
	 */

	public void removeFenceFromData(Fence fence) {
		this.getFences().remove(fence);
		Point start = fence.getStart();
		Point end = fence.getEnd();

		if(fence.getOrientation() == Orientation.HORIZONTAL) {
			for(int i=start.getX(); i<end.getX(); i++) {
				this.getGrid().addEdge(new Point(i,start.getY()-1), new Point(i,start.getY()));
			}
		} else {
			for(int i=start.getY(); i<end.getY(); i++) {
				this.getGrid().addEdge(new Point(start.getX()-1, i), new Point(start.getX(), i));
			}
		}
	}

	/**
	 * Check for all pawns if there is a path between its position and the Side that makes it wins (opposite side of its starting side)
	 * 
	 * @return Boolean value equals to true if there is such a path and false if there isn't
	 */

	public boolean existPathFromPlayerToWin() {
		if(this.getPawnsArray() != null) {
			for (Pawn p : this.getPawnsArray()) {
				if (!this.getGrid().existPath(p.getPosition(), p.getStartingSide().getOpposite())) {
					return false;
				}
			}
			return true;
		}
		return true;
	}
	
	/** 
	 * Move the given pawn to the given position if it can be moved there
	 * 
	 * @param pawnId Pawn id of the pawn moved
	 * @param newPawnPos New position of the pawn
	 * @return Boolean value equals to true if the pawn can be moved there and false if it can't
	 * @throws IncorrectPawnIndexException If the pawn index is out of bounds
	 */

	public boolean movePawn(int pawnId, Point newPawnPos) throws IncorrectPawnIndexException {
		// If the game is already won then we can't play
		if(this.getWinner() != -1) {
			return false;
		}

		try {
			// Check if the new position is valid
			if(this.isPawnPosValid(newPawnPos)) {
				this.getPawn(pawnId).setPosition(newPawnPos);
				this.checkWin();
				this.clearCurrentPossibleMoves();
				return true;
			}

		} catch (IncorrectPawnIndexException e) {
			throw e;
		}

		return false;
	}

	/** 
	 * Place a fence at the given position for the given pawn if it can be placed
	 * 
	 * @param pawnId Pawn id of the pawn that places a fence
	 * @param fence Fence placed
	 * @return Boolean value equals to true if the fence can be placed there and false if it can't
	 * @throws IncorrectPawnIndexException If the pawn index is out of bounds
	 */

	public boolean placeFence(int pawnId, Fence fence) throws IncorrectPawnIndexException {
		// If the game is already won then we can't play
		if(this.getWinner() != -1) {
			return false;
		}

		if(this.isFencePositionValid(fence)) {
			addFenceToData(fence);
			try {
				Pawn pawn = this.getPawn(pawnId);
				pawn.decreaseAvailableFences();
				this.clearCurrentPossibleMoves();
			} catch (IncorrectPawnIndexException e) {
				throw e;
			}
			return true;
		}
		return false;
	}

	/** 
	 * Check if the given Fence can be placed
	 * 
	 * @param fence Fence to be checked
	 * @return Boolean value equals to true if the fence can be placed there and false if it can't
	 */

	public boolean isFencePositionValid(Fence fence) {
		if(this.isFenceOnTheBoard(fence) && !this.isFenceOverlapping(fence)) {
			this.addFenceToData(fence);
			if(this.existPathFromPlayerToWin()) {
				this.removeFenceFromData(fence);
				return true;
			} else {
				this.removeFenceFromData(fence);
			}
		}
		return false;
	}

	/**
	 * Check if the orientation of the fence chosen by the player is valid
	 * 
	 * @param orientation Name of the orientation to be tested
	 * @return Boolean value equals to true if the orientation is valid and false if it isn't
	 */

	public boolean isValidOrientation(String orientation) {
		if (orientation.toUpperCase().matches("H(ORIZONTAL)?")){
			return true;
		}else if(orientation.toUpperCase().matches("V(ERTICAL)?")){
			return true;
		}
		return false;
	}

	/** 
	 * Set the winner to the given pawnId
	 * 
	 * @param pawnId Pawn that won
	 */

	public void setWinner(int pawnId) {
		this.winner = pawnId;
	}

	
	/**
	 * Get the id of the winner
	 * 
	 * @return Winner's id
	 */

	public int getWinner() {
		return this.winner;
	}

	/**
	 * Check if a player has won the game and set the winner accordingly
	 */

	public void checkWin(){
		try {
			for (int i = 0; i < this.getGame().getNbPlayers(); i++) {
				switch (this.getPawn(i).getStartingSide()) {
					case BOTTOM:
						if (this.getPawn(i).getPosition().getY() == 0) {
							this.setWinner(this.getPawn(i).getId());
						}
						break;
					case TOP:
						if (this.getPawn(i).getPosition().getY() == this.getNbCols() - 1) {
							this.setWinner(this.getPawn(i).getId());
						}
						break;
					case LEFT:
						if (this.getPawn(i).getPosition().getX() == this.getNbRows() - 1) {
							this.setWinner(this.getPawn(i).getId());
						}
						break;
					case RIGHT:
						if (this.getPawn(i).getPosition().getX() == 0) {
							this.setWinner(this.getPawn(i).getId());
						}
						break;
					default:
						;
				}
			}
		}catch (IncorrectPawnIndexException err){
			System.err.println(err);
		}
	}

	/**
	 * Check if two fences are intersecting
	 * 
	 * @param f1 Fence 1
	 * @param f2 Fence 2
	 * @return True if the fences are intersecting and false otherwise
	 */

	public boolean areIntersecting(Fence f1, Fence f2) {
		if(f1.getOrientation() == Orientation.HORIZONTAL){
			if(f2.getOrientation() == Orientation.VERTICAL && isStrictlyBetween(Orientation.VERTICAL, f1.getStart(), f2.getStart(),f2.getEnd()) && isStrictlyBetween(Orientation.HORIZONTAL, f2.getStart(), f1.getStart(),f1.getEnd())) {
				return true;
			}
		} else {
			if (f2.getOrientation() == Orientation.HORIZONTAL){
				if(f2.getOrientation() == Orientation.HORIZONTAL && isStrictlyBetween(Orientation.HORIZONTAL, f1.getStart(), f2.getStart(),f2.getEnd()) && isStrictlyBetween(Orientation.VERTICAL, f2.getStart(), f1.getStart(),f1.getEnd())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if a point is strictly between 2 others for one coordinate (x or y)
	 * 
	 * @param orientation Orientation giving the coordinates compared (x if horizontal and y if vertical)
	 * @param pTested Point tested
	 * @param inf Lower bound
	 * @param sup Upper bound
	 * @return True if the point is between the two others for the orientation considered
	 */

	 public boolean isStrictlyBetween(Orientation orientation, Point pTested, Point inf, Point sup) {
		if(orientation == Orientation.HORIZONTAL) {
			return (inf.getX() < pTested.getX() && pTested.getX() < sup.getX());
		} else {
			return (inf.getY() < pTested.getY() && pTested.getY() < sup.getY());
		}
	}

	/**
	 * Check if 2 fences are parallel, one the same line and share some points
	 * 
	 * @param f1 Fence 1
	 * @param f2 Fence 2
	 * @return True if the 2 fences are coincident
	 */

	public boolean areCoincident(Fence f1, Fence f2) {
		if(f1.getOrientation() == f2.getOrientation()) {
			// Same orientation
			if(f1.getStart().equals(f2.getStart()) || f1.getEnd().equals(f2.getEnd())){
				// End or start at the same point
				return true;
			}else if(f1.getOrientation() == Orientation.HORIZONTAL && f2.getStart().getY() == f1.getStart().getY()) {
				// Same row
				if(isStrictlyBetween(Orientation.HORIZONTAL, f1.getEnd(), f2.getStart(), f2.getEnd()) || isStrictlyBetween(Orientation.HORIZONTAL, f2.getEnd(), f1.getStart(), f1.getEnd())) {
					return true;
				}
			} else if(f1.getOrientation() == Orientation.VERTICAL && f2.getStart().getX() == f1.getStart().getX()) {
				// Same column
				if(isStrictlyBetween(Orientation.VERTICAL, f1.getEnd(), f2.getStart(), f2.getEnd()) || isStrictlyBetween(Orientation.VERTICAL, f2.getEnd(), f1.getStart(), f1.getEnd())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if the fence to be added overlaps with others
	 * 
	 * @param fenceChecked The fence checked
	 * @return Boolean value equals to true if the fence is overlapping with others, and false otherwise
	 */

    public boolean isFenceOverlapping(Fence fenceChecked) {
        if (this.fences != null) {
            for (Fence fence : this.fences) {
				if(areIntersecting(fence,fenceChecked) || areCoincident(fence,fenceChecked)) {
					return true;
				}
            }
        }
		return false;
    }
}
