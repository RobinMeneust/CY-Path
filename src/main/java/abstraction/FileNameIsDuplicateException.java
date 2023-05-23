package abstraction;

/**
 * Exception used to describe the case where there is already a file with the same name created
 */

public class FileNameIsDuplicateException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * Add the error message to the exception: "ERROR: The file already exists"
     */
    public FileNameIsDuplicateException() {
        super("ERROR: The file already exists");
    }
}
