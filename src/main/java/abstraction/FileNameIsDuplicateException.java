package abstraction;

public class FileNameIsDuplicateException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * Add the error message to the exception: "ERROR: The file already exists"
     */
    public FileNameIsDuplicateException() {
        super("ERROR: The file already exists");
    }
}
