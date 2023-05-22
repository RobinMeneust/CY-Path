package abstraction; 

/**
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
 * It contains functions used to check if the player action is allowed or not (e.g mvoe out of the grid, ...)
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class Board {
	/**
	 * State the Board's class attributes
	 */
	
	private int nbCols;
	private int nbRows;
	private int size;
	private Grid grid;
	private GameAbstract game;
	private ArrayList<Fence> fences;
	private Pawn[] pawns;
	private int fenceLength;
	private int winner;
	private List<Point> currentPossibleMoves;
	
	/**
	 * Create a game board from a number of rows, columns and a Game
	 * 
	 * @param nbCols Number of columns of the game board
	 * @param nbRows Number of rows of the game board
	 * @param game Object extending GameAbstract, that contains the components of the current game (list of players, rules...)
	 */

	public Board(int nbCols, int nbRows, GameAbstract game, Pawn[] pawns) {
		this.nbCols = nbCols;
		this.nbRows = nbRows;
		this.size = nbCols * nbRows;
		this.grid = new Grid(nbRows, nbCols);
		this.game = game;
		this.fences = null;
		this.fenceLength = 2;
		this.winner = -1;
		

		if(game != null && game.getNbMaxTotalFences() > 0){
			this.fences = new ArrayList<Fence>(game.getNbMaxTotalFences());
		}
		
		this.pawns = pawns;
	}

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

	public Fence[] getFencesArray() {
		Fence[] fencesArray = new Fence[this.fences.size()];
		this.fences.toArray(fencesArray);
		return fencesArray;
	}

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
	 * Get the size of the game board
	 * 
	 * @return Number of cells in the board
	 */

	public int getSize() {
		return this.size;
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
		if(i>=0 && i<pawns.length) {
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
		String orientation = CYPath.scanner.next();
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

		for(int i = 0; i < this.getGame().getNbPlayers(); i++){
			if(this.pawns[i].getPosition().equals(position)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check all the cells where a pawn can move to, from its starting position in one round and return it as a list.
	 * Here the parameters are points arranged in a 'T' shape
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
		It's shaped like a 'T' 
		*/

		// We check if the tested position is on the board and if the current position and the tested position are not separated by a fence
		if(this.isCellOnTheBoard(posTested) && this.grid.areConnected(position,posTested)){
			if(this.isPawnAtPos(posTested)) {
				// There is a pawn so we can't go there, but we can maybe jump above it
				if(this.isCellOnTheBoard(posBehindTested) && this.grid.areConnected(posTested,posBehindTested) && !this.isPawnAtPos(posBehindTested)){
					// The cell just behind is free
					listMove.add(posBehindTested);
				}
				else{
					// If there is a fence behind the pawn we check if we can go leftside or rightside
					if(this.isCellOnTheBoard(posLeftTested) && this.grid.areConnected(posTested,posLeftTested) && !this.isPawnAtPos(posLeftTested)){
						listMove.add(posLeftTested);
					}
					if(this.isCellOnTheBoard(posRightTested) && this.grid.areConnected(posTested,posRightTested) && !this.isPawnAtPos(posRightTested)){
						listMove.add(posRightTested);
					}
				}
			}
			else {
				// There is no pawn so we can go there and we can't go behind it
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
		It's shaped like a 'T' 
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

		//We test the top position
		posTested = new Point(position.getX(), position.getY() - 1);
		posBehindTested = new Point(position.getX(), position.getY() - 2);
		posLeftTested = new Point(position.getX() - 1, position.getY() - 1);
		posRightTested = new Point(position.getX() + 1, position.getY() - 1);

		allPossibleMoves = this.possibleMove(position,posTested,posBehindTested,posLeftTested,posRightTested);
		if(!allPossibleMoves.contains(position)){
			listPossibleMovements.addAll(allPossibleMoves);
		}

		//We test the left position
		posTested = new Point(position.getX() - 1, position.getY());
		posBehindTested = new Point(position.getX() - 2, position.getY());
		posLeftTested = new Point(position.getX() - 1, position.getY() + 1);
		posRightTested = new Point(position.getX() - 1, position.getY() - 1);

		allPossibleMoves = this.possibleMove(position,posTested,posBehindTested,posLeftTested,posRightTested);
		if(!allPossibleMoves.contains(position)){
			listPossibleMovements.addAll(allPossibleMoves);
		}

		//We test the right position
		posTested = new Point(position.getX() + 1, position.getY());
		posBehindTested = new Point(position.getX() + 2, position.getY());
		posLeftTested = new Point(position.getX() + 1, position.getY() - 1);
		posRightTested = new Point(position.getX() + 1, position.getY() + 1);

		allPossibleMoves = this.possibleMove(position,posTested,posBehindTested,posLeftTested,posRightTested);
		if(!allPossibleMoves.contains(position)){
			listPossibleMovements.addAll(allPossibleMoves);
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
		for(int i=0; i<this.pawns.length; i++) {
			if(this.pawns[i].getPosition().equals(pos)) {
				return this.pawns[i];
			}
		}
		return null;
	}

	public List<Point> getCurrentPossibleMoves() throws IncorrectPawnIndexException {
		if(this.currentPossibleMoves == null) {
			try {
				Pawn pawn = this.getPawn(this.getGame().getCurrentPawnIndex());
				this.setCurrentPossibleMoves(this.listPossibleMoves(pawn.getPosition()));
			} catch (IncorrectPawnIndexException e) {
				throw e;
			}
		}
		return currentPossibleMoves;
	}

	public boolean isPawnPosValid(Point pos) throws IncorrectPawnIndexException {		
		try {
			return this.getCurrentPossibleMoves().contains(pos);
		} catch (IncorrectPawnIndexException e) {
			throw e;
		}
	}

	private void setCurrentPossibleMoves(List<Point> currentPossibleMoves) {
		this.currentPossibleMoves = currentPossibleMoves;
	}

	private void clearCurrentPossibleMoves() {
		this.currentPossibleMoves = null;
	}

	/**
	 * Get the id of the pawn in the given cell coordinates as a String, if there is any.
	 * 
	 * @param x X coordinate of the cell to be checked
	 * @param y Y coordinate of the cell to be checked
	 * @return Id of the pawn in the given cell. If there is none then it returns a single space ' '
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
				for(int i=0; i<nbCols; i++) {
					System.out.printf("%3d ",i);
				}
				break;
			case COORD_LINE:
				System.out.print("  ");
				for(int i=0; i<=nbCols; i++) {
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
		for(int x=0; x<nbCols; x++){
			System.out.print("|---");
		}
		System.out.print("|");
		System.out.println();

		for(int y=0; y<nbRows; y++) {
			if(type == DisplayType.COORD_CELL){
				System.out.printf("%3d | ",y);
			} else {
				System.out.printf("    | ",y);
			}

			for(int x=0; x<nbCols; x++) {
				System.out.print(getCellContentText(x,y));
				if(x==nbCols-1 || grid.areConnected(x, y, x+1, y)) {
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
			for(int x=0; x<nbCols; x++){
				if(y == nbRows-1 || grid.areConnected(x, y, x, y+1)) {
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
	 * Add a fence to the board
	 * 
	 * @param fence Fence added
	 */

	public void addFenceToData(Fence fence) {
		this.fences.add(fence);
		Point start = fence.getStart();
		Point end = fence.getEnd();

		if(fence.getOrientation() == Orientation.HORIZONTAL) {
			for(int i=start.getX(); i<end.getX(); i++) {
				this.grid.removeEdge(new Point(i,start.getY()-1), new Point(i,start.getY()));
			}
		} else {
			for(int i=start.getY(); i<end.getY(); i++) {
				this.grid.removeEdge(new Point(start.getX()-1, i), new Point(start.getX(), i));
			}
		}
	}

	/**
	 * Remove a fence from the current game
	 * 
	 * @param fence Fence removed
	 */

	public void removeFenceFromData(Fence fence) {
		this.fences.remove(fence);
		Point start = fence.getStart();
		Point end = fence.getEnd();

		if(fence.getOrientation() == Orientation.HORIZONTAL) {
			for(int i=start.getX(); i<end.getX(); i++) {
				this.grid.addEdge(new Point(i,start.getY()-1), new Point(i,start.getY()));
			}
		} else {
			for(int i=start.getY(); i<end.getY(); i++) {
				this.grid.addEdge(new Point(start.getX()-1, i), new Point(start.getX(), i));
			}
		}
	}

	/**
	 * Check for all pawns if there is a path between its position and the Side that makes it wins (opposite side of its starting side)
	 * 
	 * @return Boolean value equals to true if there is such a path and false if there isn't
	 */

	public boolean existPathFromPlayerToWin() {
		if(this.pawns != null) {
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
		for(int i = 0; i < this.game.getNbPlayers(); i++){
			switch (this.pawns[i].getStartingSide()){
				case BOTTOM:
					if(this.pawns[i].getPosition().getY() == 0){
						this.setWinner(this.pawns[i].getId());
					}
					break;
				case TOP:
					if(this.pawns[i].getPosition().getY() == this.getNbCols()-1){
						this.setWinner(this.pawns[i].getId());
					}
					break;
				case LEFT:
					if(this.pawns[i].getPosition().getX() == this.getNbRows()-1){
						this.setWinner(this.pawns[i].getId());
					}
					break;
				case RIGHT:
					if(this.pawns[i].getPosition().getX() == 0){
						this.setWinner(this.pawns[i].getId());
					}
					break;
				default:;
			}
		}
	}

	/**
	 * Check if the fence to be added overlaps with others
	 * 
	 * @param fenceChecked The fence checked
	 * @return Boolean value equals to true if the fence is overlapping with others, and false otherwise
	 */

    public boolean isFenceOverlapping(Fence fenceChecked) {
        if (this.fences != null) {
			/* 
			Array of the number of fences that have the same y and this y is between the y of the starting point and the ending point of fencedChecked (if fenceChecked is vertical, and x if it's horizontal)
			And the x of the starting point or ending point of those fences must be equals to the x of the new fence (if fenceChecked is vertical, and y if it's horizontal)
			In this array there is for each index (the y (if vertical) or x (if horizontal) coordinate of the new fence) the number of fences that correspond to the statements above
			*/

            for (Fence fence : this.fences) {
                // over each other
                if (fenceChecked.getStart().equals(fence.getStart()) && fenceChecked.getOrientation().equals(fence.getOrientation())) {
                    return true;
                } else {
                    switch (fenceChecked.getOrientation()) {
                        case HORIZONTAL:
                            if (fence.getOrientation() == Orientation.HORIZONTAL && fence.getStart().getY() == fenceChecked.getStart().getY()) {
                                for (int i = 0; i < fence.getLength(); i++) {
                                    if (fenceChecked.getStart().getX() + i == fence.getStart().getX()) {
                                        return true;
                                    } else if (fenceChecked.getEnd().getX() - 1 - i == fence.getEnd().getX() - 1) {
                                        return true;
                                    }
                                }
                            } else if (fence.getOrientation() == Orientation.VERTICAL) {
                                if(fenceChecked.getStart().getX() < fence.getStart().getX() && fence.getStart().getX() < fenceChecked.getEnd().getX()){
                                    if(fence.getStart().getY() < fenceChecked.getStart().getY() && fenceChecked.getStart().getY() < fence.getEnd().getY()){
                                        return true;
                                    }
                                }
                            }
                            break;
                        case VERTICAL:
                            if (fence.getOrientation() == Orientation.VERTICAL && fence.getStart().getX() == fenceChecked.getStart().getX()) {
								for (int i = 0; i < fence.getLength(); i++) {
									if (fenceChecked.getStart().getY() + i == fence.getStart().getY()) {
                                        return true;
                                    } else if (fenceChecked.getEnd().getY() - 1 - i == fence.getEnd().getY() - 1) {
										return true;
                                    }
                                }
                            } else if (fence.getOrientation() == Orientation.HORIZONTAL) {
                                if(fence.getStart().getX() < fenceChecked.getStart().getX() && fenceChecked.getStart().getX() < fence.getEnd().getX()){
                                    if(fenceChecked.getStart().getY() < fence.getStart().getY() && fence.getStart().getY() < fenceChecked.getEnd().getY()){
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
