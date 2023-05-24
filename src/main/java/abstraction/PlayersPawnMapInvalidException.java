package abstraction;

/**
 * Exception used to describe the case where the map associating pawns to players, is incorrect
 */

public class PlayersPawnMapInvalidException extends Exception {
    /**
     * Constructor of PlayersPawnMapInvalidException.
     * Add the error message to the exception: "ERROR: Invalid association between players with pawns"
     */

    public PlayersPawnMapInvalidException() {
		super("ERROR: Invalid association between players with pawns");
	}
}
