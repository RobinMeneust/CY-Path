package abstraction;

/**
 * Exception used to describe the case where the number of players is incorrect, e.g if it's equals to 0
 */

public class InvalidNumberOfPlayersException extends Exception {
	/**
     * Constructor of abstrac.InvalidNumberOfPlayersException.
     * Add the error message to the exception: "ERROR: Invalid number of players (must be 2 or 4)"
     */

	public InvalidNumberOfPlayersException() {
		super("ERROR: Invalid number of players (must be 2 or 4)");
	}
}
