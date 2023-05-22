package abstraction;

public class FileNameIsDuplicateException extends Exception {
    public FileNameIsDuplicateException() {
        super("ERROR: The file already exists");
    }
}
