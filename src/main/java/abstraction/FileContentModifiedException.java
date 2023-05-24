package abstraction;

/**
 * Exception used to describe the case where the content of the file loaded by the palyer are incorrect
 */

public class FileContentModifiedException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * Add the error message to the exception: "ERROR: The loaded game file cannot succeed due to modifications made."
     */

    public FileContentModifiedException() {
       super("ERROR: The loaded game file cannot succeed due to modifications made.");
    }
}
