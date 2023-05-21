package abstraction;

/**
 * Exception used to describe the case where the initialization of the javafx scene game is incorrect
 */

public class GameNotInitializedException extends Exception {
    /**
     * Constructor of GameNotInitializedException.
     * Add the error message to the exception: "ERROR: Pawn index out of bounds"
     */

    public GameNotInitializedException() {
        super("ERROR: The game must be initialized");
    }
}
