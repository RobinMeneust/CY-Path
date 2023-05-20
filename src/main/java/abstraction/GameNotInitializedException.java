package abstraction;

/**
 * Exception used to describe the case where the initialization of the javafx scene game is incorrect
 */

public class GameNotInitializedException extends Exception {
    public GameNotInitializedException() {
        super("ERROR: The game must be initialized");
    }
}
