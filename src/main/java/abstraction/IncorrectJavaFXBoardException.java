package abstraction;

/**
 * Exception used to describe the case where the board created in the JavaFX window mode is incorrect
 */

public class IncorrectJavaFXBoardException extends Exception {
    /**
     * Constructor of IncorrectJavaFXBoardException.
     * Add the error message to the exception: "The game board wasn't properly initialized"
     */

    public IncorrectJavaFXBoardException() {
		super("ERROR: The game board wasn't properly initialized");
	}
}
