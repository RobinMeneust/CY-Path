package abstraction;

public class FileNameNotExistException extends Exception {
    public FileNameNotExistException() {
        super("ERROR: The file doesn't exist");
    }
}