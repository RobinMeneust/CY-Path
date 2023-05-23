package abstraction;

public class IncorrectShapeException extends Exception {
    public IncorrectShapeException() {
        super("ERROR: The shape is incorrect");
    }
}
