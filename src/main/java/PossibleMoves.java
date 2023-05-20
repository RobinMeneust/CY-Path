/**
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
	 * Create possibles moves from the current position of the pawn of the playern and the list of all possible moves
	 * 
	 * @param currentPosition (Point)
     * @param possibleMovesList (LinkedList<Point>)
	 */

    public PossibleMoves(Point currentPosition, LinkedList<Point> possibleMovesList) {
        this.currentPosition = currentPosition;
        this.possibleMovesList = possibleMovesList;
    }

    /**
	 * Create possibles moves from the current position of the pawn of the playern
	 * 
	 * @param currentPosition (Point)
	 */

    public PossibleMoves(Point currentPosition) {
        this(currentPosition, null);
    }

    /**
     * Accessor to recover the current position of a pawn
     * 
     * @return currentPosition (Point)
     */
    public Point getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Accessor to assign the current position of a pawn
     * 
     * @param currentPosition (Point)
     */

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * Accessor to recover the list of all possible moves of a pawn
     * 
     * @return possibleMovesList (LinkedList<Point>)
     */

    public LinkedList<Point> getPossibleMovesList() {
        return this.possibleMovesList;
    }

    /**
     * Accessor to assign the list of all possible moves for a pawn
     * 
     * @param possibleMovesList (LinkedList<Point>)
     */

    public void setPossibleMovesList(LinkedList<Point> possibleMovesList) {
        this.possibleMovesList = possibleMovesList;
    }    
}
