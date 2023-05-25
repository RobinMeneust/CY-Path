package abstraction;

/**
 * Exception used to describe the case where the format of the save file is incorrect
 */

public class SaveFileFormatInvalidException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * Add the error message to the exception: "ERROR: The saved file's format is incorrect."
     */
    public SaveFileFormatInvalidException() {
        super("ERROR: The saved file's format is incorrect.");
    }
}