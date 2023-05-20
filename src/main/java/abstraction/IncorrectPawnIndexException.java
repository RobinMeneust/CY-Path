package abstraction;

/**
 * Exception used to describe the case where a pawn index is incorrect (out of bounds), e.g if it's negative
 */

public class IncorrectPawnIndexException extends Exception {
    /**
     * Constructor of abstrac.IncorrectPawnIndexException.
     * Add the error message to the exception: "ERROR: abstrac.Pawn index out of bounds"
     */
    
    public IncorrectPawnIndexException() {
        super("ERROR: abstrac.Pawn index out of bounds");
    }
}
