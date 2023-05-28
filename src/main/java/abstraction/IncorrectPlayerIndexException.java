package abstraction;

/**
 * Exception used to describe the case where a player index is incorrect (out of bounds), e.g. if it's negative
 */

public class IncorrectPlayerIndexException extends Exception {
    /**
     * Constructor of IncorrectPlayerIndexException.
     * Add the error message to the exception: "ERROR: Player index out of bounds"
     */
    
    public IncorrectPlayerIndexException() {
        super("ERROR: Player index out of bounds");
    }
}
