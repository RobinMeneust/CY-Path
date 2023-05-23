package abstraction;

public class FileNameNotExistException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     */
    public FileNameNotExistException() {
        super("ERROR: The file doesn't exist");
    }
}