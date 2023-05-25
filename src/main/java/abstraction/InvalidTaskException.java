package abstraction;

/**
 * Exception used to describe the case where task associated to a game is invalid (null for instance)
 */

public class InvalidTaskException extends Exception {
	/**
	 * Constructor of InvalidTaskException.
	 * Add the error message to the exception: "ERROR: Task wasn't properly created or associated to the game"
	 */

	public InvalidTaskException() {
		super("ERROR: Task wasn't properly created or associated to the game");
	}
}
