/**
 * Exception used to describe the case where the number of players is incorrect, e.g if it's equals to 0
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class InvalidNumberOfPlayersException extends Exception {
	/**
     * Constructor of InvalidNumberOfPlayersException.
     * Add the error message to the exception: "ERROR: Invalid number of players (must be 2 or 4)"
     */

	public InvalidNumberOfPlayersException() {
		super("ERROR: Invalid number of players (must be 2 or 4)");
	}
}
