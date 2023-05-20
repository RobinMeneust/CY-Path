package abstraction; /**
 * Importing java classes needed for the PossibleMoves class
 * 
 * Importing classes from the java.util package
 */

import java.util.LinkedList;

/**
 * This class represents all possible moves a player can make from the current position of his pawn.
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class PossibleMoves {
    /**
	 * State the Board's class attributes
	 */

    private Point currentPosition;
    private LinkedList<Point> possibleMovesList;

    /**
	 * Create possibles moves from the current position of the pawn of the player and the list of all possible moves
	 * 
	 * @param currentPosition Position of the pawn
     * @param possibleMovesList List of possible moves
	 */

    public PossibleMoves(Point currentPosition, LinkedList<Point> possibleMovesList) {
        this.currentPosition = currentPosition;
        this.possibleMovesList = possibleMovesList;
    }

    /**
	 * Create possibles moves from the current position of the pawn of the playern
	 * 
	 * @param currentPosition Current pawn position
	 */

    public PossibleMoves(Point currentPosition) {
        this(currentPosition, null);
    }

    /**
     * Get the position of a pawn
     * 
     * @return position of the pawn
     */
    public Point getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Set the current position of a pawn
     * 
     * @param currentPosition New position
     */

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * Get the list of all possible moves of a pawn
     * 
     * @return possibleMovesList List of possible moves
     */

    public LinkedList<Point> getPossibleMovesList() {
        return this.possibleMovesList;
    }

    /**
     * Set possible moves list to the given one
     * 
     * @param possibleMovesList New list of possible moves
     */

    public void setPossibleMovesList(LinkedList<Point> possibleMovesList) {
        this.possibleMovesList = possibleMovesList;
    }    
}
