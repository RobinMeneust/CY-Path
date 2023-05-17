package main.java;

public class InvalidNumberOfPlayersException extends Exception {
	public InvalidNumberOfPlayersException() {
		super("ERROR: Invalid number of players (must be 2 or 4)");
	}
}
