package abstraction;

public class FileNameIsDuplicate extends Exception {
    public FileNameIsDuplicate() {
        super("ERROR: The file already exists");
    }
}
