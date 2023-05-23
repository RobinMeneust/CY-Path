package abstraction;

public class FileNameNotExistException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * Add the error message to the exception: "ERROR: The file doesn't exist"
     */
    public FileNameNotExistException() {
        super("ERROR: The file doesn't exist");
    }
}