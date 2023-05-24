package abstraction;

/**
 * Exception used to describe the case where the file name entered by the player is incorrect
 */

public class FileNameException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * Add the error message to the exception: "ERROR: The saved file's name is incorrect"
     */
    public FileNameException() {
        super("ERROR: The saved file's name is incorrect. The save's name file contains only letters, numbers, _ , - and . ");
    }
}
