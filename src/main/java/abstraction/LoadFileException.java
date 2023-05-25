package abstraction;

/**
 * Exception thrown when the file loaded has an incorrect name
 */

public class LoadFileException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * Add the error message to the exception: "ERROR: The load file's name isn't correct"
     */
    
    public LoadFileException() {
        super("ERROR: The load file's name isn't correct.");
    }
}
