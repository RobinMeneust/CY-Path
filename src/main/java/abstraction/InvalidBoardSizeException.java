package abstraction;

/**
 * Exception used to describe the case where the board size is incorrect
 */

public class InvalidBoardSizeException extends Exception {
	/**
	 * Constructor of InvalidBoardSizeException.
	 * Add the error message to the exception: "ERROR: The game board size is incorrect.\nSize must be at least 3x3 for 4 players and 2x2 for 2 players, and the maximum size is 15x15"
	 */

	public InvalidBoardSizeException() {
		super("ERROR: The game board size is incorrect.\nSize must be at least 3x3 for 4 players and 2x2 for 2 players, and the maximum size is 15x15");
	}
}
