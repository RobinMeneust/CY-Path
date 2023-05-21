package abstraction; /**
 * Importing java classes needed for the GameConsole class
 * 
 * Importing classes from the java.util package
 */

import java.util.Scanner;
import java.util.List;

/**
 * Current game launched in console mode
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class GameConsole extends GameAbstract {
    /**
	 * Create a GameConsole object by giving all of its attributes
	 * @param players Array of the players
	 * @param nbFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 */

    public GameConsole(Player[] players, int nbFences, int nbRows, int nbCols) throws InvalidNumberOfPlayersException {
        super(players, nbFences, nbRows, nbCols);
    }
    
    /**
     * Launch the current game in console mode
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

                System.out.println("Turn of player: " +  this.getPlayer(this.getCurrentPlayerIndex()));
                currentPawn = this.getBoard().getPawn(this.getCurrentPlayerIndex());
                System.out.println("You have "+currentPawn.getAvailableFences()+ " fences remaining.\n");
                if(currentPawn.getAvailableFences() == 0){
                    response = "move";
                    System.out.println("You don't have any fence remaining. You can only move.");
                } else{
                    do {
                        System.out.println("What is your next action ? ('m' (move) or 'f' (place fence))");
                        response = scanner.nextLine();
                        response = response.toUpperCase();
                    }while(!response.equals("M") && !response.equals("F"));
                }
        
                if(response.equals("M")){
                    this.getBoard().displayBoard(DisplayType.COORD_CELL);

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
                } else if (response.equals("F")) {
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
                this.getBoard().clearLastCheckedFence();
                this.setCurrentPlayerIndex(this.getCurrentPlayerIndex()+1);
            }

            Player playerWinner = this.getBoard().getPawn(this.getBoard().getWinner()).getPlayer();
            System.out.println("The winner is "+playerWinner);
            this.setState(GameState.FINISHED);
        } catch (IncorrectPawnIndexException e) {
            System.err.println("ERROR: Pawn index is incorrect. Check the number of players and the number of pawns and see if they are equals");
            System.exit(-1);
        }
	}
}
