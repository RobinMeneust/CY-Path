package abstraction;

public class IncorrectShapeException extends Exception {
    /**
     * Constructor of IncorrectShapeException.
     * Add the error message to the exception: "ERROR: The shape is incorrect"
     */
    public IncorrectShapeException() {
        super("ERROR: The shape is incorrect");
    }
}
