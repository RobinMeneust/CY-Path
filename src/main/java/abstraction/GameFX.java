package abstraction; /**
 * Importing java classes needed for the GameFX class
 * 
 * Importing classes from the java.util package
 */

/**
 * Importing javafx classes needed for the CYPathFX class
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Current game launched in window mode with JavaFX
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class GameFX extends GameAbstract {
    private StringProperty action;
    private boolean isEndTurn;

    /**
	 * Create a GameFX object by giving all of its attributes
     * 
	 * @param players Array of the players
	 * @param nbFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 */

    public GameFX(Player[] players, int nbFences, int nbRows, int nbCols) throws InvalidNumberOfPlayersException {
        super(players, nbFences, nbRows, nbCols);
        this.action = new SimpleStringProperty("Move");
        this.isEndTurn = false;
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

    public boolean getIsEndTurn(){
		return this.isEndTurn;
	}
	public void setIsEndTurn(boolean b){
		this.isEndTurn = b;
	}

    /**
     * Launch the current game for window mode
     */

     public void launch() {
		// The game is now in progress
		this.setState(GameState.IN_PROGRESS);

        //String response = "";
        //Scanner scanner = new Scanner(System.in);
        Pawn currentPawn = null;

        try {
            while(this.getBoard().getWinner() == -1){
                this.getBoard().displayBoard(DisplayType.NO_COORD);
                //Platform.runLater(() -> this.setAction("Move"));

                System.out.println("Turn of player: " +  this.getPlayer(this.getCurrentPlayerIndex()));
                currentPawn = this.getBoard().getPawn(this.getCurrentPlayerIndex());

                this.setIsEndTurn(false);
                while (!this.getIsEndTurn()) {
                    try {
                        Thread.sleep(100); //Wait 100 milliseconds before checking again
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //If button set Move
                if("Move".equals(this.getAction().get())){
                    System.out.println("Move in : " + currentPawn.getPosition());
                } else if("Place fence".equals(this.getAction().get())) {
                    System.out.println("Fence have been placed");
                }   
                this.setCurrentPlayerIndex(this.getCurrentPlayerIndex()+1);
                this.getBoard().checkWin();
            }
        
            Pawn pawnWinner = this.getBoard().getPawn(this.getBoard().getWinner());
            System.out.println("The winner is "+pawnWinner.getPlayer());
            this.setState(GameState.FINISHED);
        } catch (IncorrectPawnIndexException e) {
            System.err.println("ERROR: Pawn index is incorrect. Check the number of players and the number of pawns and see if they are equals");
            System.exit(-1);
        }
    }
}
