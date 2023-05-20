public class GameNotInitializedException extends Exception {
    public GameNotInitializedException() {
        super("ERROR: The game must be initialized");
    }
}
