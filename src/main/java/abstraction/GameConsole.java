package abstraction; /**
 * Importing java classes needed for the GameConsole class
 * 
 * Importing classes from the java.util package
 */


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import presentation.CYPath;

/**
 * Current game launched in console mode
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class GameConsole extends GameAbstract {
    /**
	 * Create a GameConsole object by giving all of its attributes
	 * @param players Array of the players
	 * @param nbMaxTotalFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
     * @param playersPawnIndex Player associated to each pawn index associated
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 */

    public GameConsole(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex) throws InvalidNumberOfPlayersException {
        super(players, nbMaxTotalFences, nbRows, nbCols, playersPawnIndex);
    }

    public GameConsole(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, Pawn[] pawns, int currentPlayerIndex) throws InvalidNumberOfPlayersException {
        super(players, nbMaxTotalFences, nbRows, nbCols, playersPawnIndex, pawns, currentPlayerIndex);
    }
    
    /**
     * Launch the current game in console mode
     */

    public void launch() {
		// The game is now in progress
		this.setState(GameState.IN_PROGRESS);

        String response = "";
        Pawn currentPawn = null;

        try {
            while(this.getBoard().getWinner() == -1){
                this.getBoard().displayBoard(DisplayType.NO_COORD);

                System.out.println("Turn of player: " +  this.getCurrentPlayer());
                currentPawn = this.getCurrentPawn();
                System.out.println("You have "+currentPawn.getAvailableFences()+ " fences remaining.\n");
                if(currentPawn.getAvailableFences() == 0){
                    response = "move";
                    System.out.println("You don't have any fence remaining. You can only move.");
                } else{
                    do {
                        System.out.println("What is your next action ? ('m' (move) or 'f' (fence) or 's' (save))");
                        response = CYPath.scanner.nextLine();
                        response = response.toUpperCase();
                        
                        if(response.matches("S(AVE)?")) {
                            boolean isValidSave = false;
                            do {
                                try {
                                    System.out.println("What is the name of your save file ? (without extension) :");
                                    String fileName = CYPath.scanner.nextLine();

                                    SaveDataInJSONFile saveDataObject = new SaveDataInJSONFile(this.getBoard().getNbRows(), this.getBoard().getNbCols(), this.getBoard().getFencesArray(), this.getNbMaxTotalFences(), this.getBoard().getPawnsArray(), this.getCurrentPawnIndex());
                                    saveDataObject.save(fileName);
                                    isValidSave = true;
                                } catch (FileNameIsDuplicateException e) {
                                    System.err.println("Error: the name of the file is already used, write a new name save file :");
                                } catch (IOException e) {
                                    System.err.println("Error: there is an error during saving game, write a new name save file :");
                                }
                            } while (!isValidSave);
                        }
                    }while(!response.matches("M(OVE)?") && !response.matches("F(ENCE)?"));
                }
        
                if(response.equals("M")){
                    this.getBoard().displayBoard(DisplayType.COORD_CELL);

                    List<Point> listPossibleMoves = this.getBoard().getCurrentPossibleMoves();

                    System.out.println("Those are the cells where your pawn can move to:");
                    System.out.println(listPossibleMoves);
                    
                    Point point = null;
                    boolean isPawnPosValid = false;
                    System.out.println("Where do you want to go ?");

                    do {
                        try {
                            point = Point.choosePoint();
                            isPawnPosValid = this.getBoard().movePawn(this.getCurrentPawn().getId(), point);
                            if(!isPawnPosValid) {
                                System.out.println("The pawn can't move here\nTry again.");
                            }
                        } catch (NumberFormatException e) {}
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
                        point = Point.choosePoint();
                        fence.setStart(point);
                        isFenceValid = this.getBoard().placeFence(this.getCurrentPawn().getId(), fence);
                        if(!isFenceValid) {
                            System.out.println("The fence can't be placed here (Starting point:" + fence.getStart() + ").\nTry again.");
                        }
                    } while(!isFenceValid);
                }
                this.endPlayerTurn();
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
