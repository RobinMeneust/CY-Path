import java.util.LinkedList;

public class PossibleMoves {
    private Point currentPosition;
    private LinkedList<Point> possibleMovesList;

    public PossibleMoves(Point currentPosition, LinkedList<Point> possibleMovesList) {
        this.currentPosition = currentPosition;
        this.possibleMovesList = possibleMovesList;
    }

    public PossibleMoves(Point currentPosition) {
        this(currentPosition, null);
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Point currentPosition) {
        this.currentPosition = currentPosition;
    }

    public LinkedList<Point> getPossibleMovesList() {
        return this.possibleMovesList;
    }

    public void setPossibleMovesList(LinkedList<Point> possibleMovesList) {
        this.possibleMovesList = possibleMovesList;
    }    
}
