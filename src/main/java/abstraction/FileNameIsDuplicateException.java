package abstraction;

public class FileNameIsDuplicateException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     */
    public FileNameIsDuplicateException() {
        super("ERROR: The file already exists");
    }
}
