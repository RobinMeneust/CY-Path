package abstraction;

/**
 * Exception used to describe the case where we encountered an incorrect shape
 */

public class IncorrectShapeException extends Exception {
    /**
     * Constructor of IncorrectShapeException.
     * Add the error message to the exception: "ERROR: The shape is incorrect"
     */
    
    public IncorrectShapeException() {
        super("ERROR: The shape is incorrect");
    }
}
