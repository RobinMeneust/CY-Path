/**
 * Exception used to describe the case where a pawn index is incorrect (out of bounds), e.g if it's negative
 */

public class IncorrectPawnIndexException extends Exception {
    /**
     * Constructor of IncorrectPawnIndexException.
     * Add the error message to the exception: "ERROR: Pawn index out of bounds"
     */
    
    public IncorrectPawnIndexException() {
        super("ERROR: Pawn index out of bounds");
    }
}
