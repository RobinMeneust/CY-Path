package abstraction;

/**
 * Exception used to describe the case where the fence length is incorrect
 */

public class InvalidFenceLengthException extends Exception {
	/**
	 * Constructor of InvalidFenceLengthException.
	 * Add the error message to the exception: "ERROR: The length of the fences for this game is incorrect.\nA positive integer, lesser than the number of rows and columns is expected"
	 */

	public InvalidFenceLengthException() {
		super("ERROR: The length of the fences for this game is incorrect.\nA positive integer, lesser than the number of rows and columns is expected");
	}
}