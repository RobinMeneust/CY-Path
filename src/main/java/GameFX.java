import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Current game launched in window mode with JavaFX
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class GameFX extends GameAbstract {
    private StringProperty action;

    /**
	 * Create a GameFX object by giving all of its attributes
	 * @param players Array of the players
	 * @param nbFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 */

    public GameFX(Player[] players, int nbFences, int nbRows, int nbCols) throws InvalidNumberOfPlayersException {
        super(players, nbFences, nbRows, nbCols);
        this.action = new SimpleStringProperty("Move");
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
     * Launch the current game for window mode
     */

    public void launch() {
		// The game is now in progress
		this.setState(GameState.IN_PROGRESS);

        String response = "";
        Scanner scanner = new Scanner(System.in);
        Pawn currentPawn = null;

        try {
            while(this.getBoard().getWinner() == -1){
                this.getBoard().displayBoard(DisplayType.NO_COORD);
                Platform.runLater(() -> this.setAction("Move"));

                System.out.println("Turn of player: " +  this.getPlayer(this.getCurrentPlayerIndex()));
                currentPawn = this.getBoard().getPawn(this.getCurrentPlayerIndex());
                
                if(currentPawn.getAvailableFences() != 0){
                    do {
                        System.out.println("To choose your next action, click on the button so that its text correpsonds to what you want to do.\n Press 'yes' in the terminal to confirm your selection");
                        response = scanner.nextLine();
                    }while(!response.equals("yes"));
                }

                //If button set Move
                if("Move".equals(this.getAction().get())){
                    this.getBoard().displayBoard(DisplayType.COORD_CELL);

                    if (currentPawn.getAvailableFences() == 0) {
                        System.out.println("You have "+currentPawn.getAvailableFences()+ "fences remaining.\nYou can only move.");
                    }

                    PossibleMoves possibleMoves = this.getBoard().updateAndGetPossibleMoves(this.getCurrentPlayerIndex());
                    List<Point> listPossibleMoves = possibleMoves.getPossibleMovesList();

                    System.out.println("Those are the cells where your pawn can move to:");
                    System.out.println(listPossibleMoves);
                    
                    Point point = null;
                    boolean isPawnPosValid = false;
                    System.out.println("Where do you want to go ?");

                    do {
                        point = Board.choosePosition();
                        isPawnPosValid = this.getBoard().movePawn(this.getCurrentPlayerIndex(), point);
                        if(!isPawnPosValid) {
                            System.out.println("The pawn can't move here\nTry again.");
                        }
                    } while(!isPawnPosValid);
                } else if("Place fence".equals(this.getAction().get())) {
                    //If button set Place Fence

                    this.getBoard().displayBoard(DisplayType.COORD_LINE);
                    boolean isFenceValid = false;

                    String orientation = "";
                    Fence fence = new Fence(this.getBoard().getFenceLength());
                    Point point = null;
                    do {
                        System.out.println("What is the orientation of your fence ? (H(ORIZONTAL) or V(ERTICAL))");
                        orientation = this.getBoard().chooseOrientation();
                        isFenceValid = this.getBoard().isValidOrientation(orientation);

                        if(!isFenceValid) {
                            System.out.println(orientation+" is not a valid orientation\nTry again.");
                        }
                    }while(!isFenceValid);
                    fence.setOrientation(orientation);

                    do {
                        System.out.println("Where do you want to put your fence ? (X,Y)");
                        point = Board.choosePosition();
                        fence.setStart(point);
                        isFenceValid = this.getBoard().placeFence(this.getCurrentPlayerIndex(), fence);
                        if(!isFenceValid) {
                            System.out.println("The fence can't be placed here (Starting point:" + fence.getStart() + ").\nTry again.");
                        }
                    } while(!isFenceValid);
                }   
                this.setCurrentPlayerIndex(this.getCurrentPlayerIndex()+1);
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
