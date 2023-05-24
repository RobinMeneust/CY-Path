package abstraction;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.util.HashMap;

/*
 * Importing java classes needed for the GameFX class
 * 
 * Importing classes from the java.util package
 */

/*
 * Importing javafx classes needed for the CYPathFX class
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Current game launched in window mode with JavaFX
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

@SuppressWarnings("deprecation")
public class GameFX extends GameAbstract {
    /**
     * The StringProperty representing the current action.
     */
    private StringProperty action;

    /**
     * A boolean indicating if it is the end of the turn.
     */
    private boolean isEndTurn;

    /**
     * The BooleanProperty indicating if it is the end of the game.
     */
    public BooleanProperty isEndGame;

   /**
	 * Create a GameFX object by giving all of its attributes
	 * @param players Array of the players
	 * @param nbMaxTotalFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
     * @param playersPawnIndex Player associated to each pawn index associated
     * @param fenceLength Length of the fences
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 * @throws PlayersPawnMapInvalidException If all players aren't associated to a pawn
	 * @throws InvalidBoardSizeException If the board size is incorrect (too small)
	 * @throws InvalidFenceLengthException If the fence length is incorrect (negative, equals to 0 or too large for the board)
	 */

    public GameFX(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, int fenceLength) throws InvalidNumberOfPlayersException, PlayersPawnMapInvalidException, InvalidBoardSizeException, InvalidFenceLengthException  {
        super(players, nbMaxTotalFences, nbRows, nbCols, playersPawnIndex, fenceLength);
        this.action = new SimpleStringProperty("Place fence");
        this.isEndTurn = false;
        this.isEndGame = new SimpleBooleanProperty(false);
    }

	/**
	 * Create a GameFX object by giving all of its attributes
	 * @param players Array of the players
	 * @param nbMaxTotalFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
	 * @param playersPawnIndex Player associated to each pawn index associated
	 * @param pawns Table of pawns to be assigned for every player
	 * @param currentPlayerIndex Set the player ready to be played
     * @param fenceLength Length of the fences
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 * @throws PlayersPawnMapInvalidException If all players aren't associated to a pawn
	 * @throws InvalidBoardSizeException If the board size is incorrect (too small)
	 * @throws InvalidFenceLengthException If the fence length is incorrect (negative, equals to 0 or too large for the board)
	 */
    public GameFX(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, Pawn[] pawns, int currentPlayerIndex, int fenceLength) throws InvalidNumberOfPlayersException, PlayersPawnMapInvalidException, InvalidBoardSizeException, InvalidFenceLengthException  {
        super(players, nbMaxTotalFences, nbRows, nbCols, playersPawnIndex, pawns, currentPlayerIndex, fenceLength);
        this.action = new SimpleStringProperty("Place fence");
        this.isEndTurn = false;
        this.isEndGame = new SimpleBooleanProperty(false);
    }

    /**
	 * Get a string that corresponds to a type of action to perform
	 *
	 * @return Action : a string property
	 */

    public StringProperty getAction() {
        return this.action;
    }

    /**
	 * Set a type of action to perform
	 * 
	 * @param a String that corresponds to a type of action (move,...)
	 */

	public void setAction(String a){
		this.action.set(a);
	}

    /**
     * Gets the current state indicating if it is the end of the turn.
     * @return True if it is the end of the turn, false otherwise.
     */
    public boolean getIsEndTurn() {
        return this.isEndTurn;
    }

    /**
     * Sets the state indicating if it is the end of the turn.
     * @param isEndTurn True if it is the end of the turn, false otherwise.
     */
    public void setIsEndTurn(boolean isEndTurn) {
        this.isEndTurn = isEndTurn;
    }

    /**
     * Launch the current game for window mode
     */

     public void launch() {
        Pawn currentPawn = null;

        while(true){
            this.getBoard().displayBoard(DisplayType.NO_COORD);

            System.out.println("Turn of player: " + this.getCurrentPlayer());

			this.setChanged();
			this.notifyObservers(this.getCurrentPawnIndex());
			currentPawn = this.getCurrentPawn();

            this.setIsEndTurn(false);
            while (!this.getIsEndTurn()) {
                try {
                    Thread.sleep(100); //Wait 100 milliseconds before checking again
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            //If button set Move
            if("Move".equals(this.getAction().get())){
                System.out.println("Move in : " + currentPawn.getPosition());
            } else if("Place fence".equals(this.getAction().get())) {
                System.out.println("Fence have been placed");
            }
            this.endPlayerTurn();
        }
    }
}
