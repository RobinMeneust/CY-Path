package abstraction;

/*
 * Importing java classes needed for the GameConsole class
 * Importing classes from the java.util package
 */

import java.util.HashMap;
import java.util.List;

/*
 * Importing classes from the java.nio package
 * 
 * It provides features for handling non-blocking I/O operations, as well as efficient handling of binary data, such as data transfer between channels and data buffers.
 */

import java.nio.file.FileAlreadyExistsException;

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
     * @param fenceLength Length of the fences
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 * @throws PlayersPawnMapInvalidException If all players aren't associated to a pawn
	 * @throws InvalidBoardSizeException If the board size is incorrect (too small)
	 * @throws InvalidFenceLengthException If the fence length is incorrect (negative, equals to 0 or too large for the board)
	 */

    public GameConsole(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, int fenceLength) throws InvalidNumberOfPlayersException, PlayersPawnMapInvalidException, InvalidBoardSizeException, InvalidFenceLengthException {
        super(players, nbMaxTotalFences, nbRows, nbCols, playersPawnIndex, fenceLength);
    }

    /**
     * Create a GameConsole object by giving all of its attributes
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
    
    public GameConsole(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, Pawn[] pawns, int currentPlayerIndex, int fenceLength) throws InvalidNumberOfPlayersException, PlayersPawnMapInvalidException, InvalidBoardSizeException, InvalidFenceLengthException {
        super(players, nbMaxTotalFences, nbRows, nbCols, playersPawnIndex, pawns, currentPlayerIndex, fenceLength);
    }

    /**
     * Save the current game in a file
     */

    private void saveGame() {
        SaveDataInJSONFile saveDataObject = new SaveDataInJSONFile(this.getBoard().getNbRows(), this.getBoard().getNbCols(), this.getBoard().getFencesArray(), this.getNbMaxTotalFences(), this.getBoard().getPawnsArray(), this.getCurrentPawnIndex());
        System.out.println("What is the name of your save file ? (without extension) :");
        String fileName = CYPath.scanner.nextLine();

        try {
            saveDataObject.save(fileName, false);
            System.out.println("Game successfully saved");
        } catch (FileAlreadyExistsException e) {
            System.err.println("Warning: the name of the file is already used, do you want to overwrite it? ('y' (yes) or 'n' (no))");
            String response = CYPath.scanner.nextLine();

            if(response.toUpperCase().matches("Y(ES)?")) {
                try {
                    saveDataObject.save(fileName, true);
                    System.out.println("Game successfully saved");
                } catch (Exception err) {
                    System.err.println("Error: there was an error while saving the game and overwriting the file\n"+e.getMessage());
                }
            } else {
                System.out.println("Saving process cancelled");
            }
        } catch (Exception e) {
            System.err.println("Error: there was an error while saving the game :" + e.getMessage());
        }
    }

    /**
     * Ask the user if he wants to move, place a fence or save a game
     * 
     * @param canPlaceFence Indicates if the user is allowed to place a fence. If he can, it's equal to true, and if he can't, it's false
     * @return User's choice
     */

    private String getUserActionChoice(boolean canPlaceFence, boolean canMove) {
        String response = "";

        do{
            System.out.println("What is your next action ? ('s' (save) ");
            if(canMove) {
                System.out.print("'m' (move) ");
            } else {
                System.out.print("'n' (next : skip the current player's turn) ");
            }
            if(canPlaceFence) {
                System.out.print(" 'f' (fence))");
            }
            System.out.println();

            response = CYPath.scanner.nextLine();
            response = response.toUpperCase();
            if(response.matches("S(AVE)?")) {
                saveGame();
            }
        } while(!(response.matches("M(OVE)?") && canMove) && !(response.matches("F(ENCE)?") && canPlaceFence) && !(response.matches("N(EXT)?") && !canMove));
        return response;
    }

    /**
     * Ask where the user wants to move its pawn and move it if's a valid position
     */

    private void playerMovePawn() {
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
            } catch (NumberFormatException ignored) {

            } catch (IncorrectPawnIndexException e) {
                System.err.println("ERROR: Pawn index is incorrect. Check the number of players and the number of pawns and see if they are equals");
                System.exit(-1);
            }
        } while(!isPawnPosValid);
    }

    /**
     * Ask where the user what should be the orientation of the given fence and change it to this new value
     * 
     * @param fence Fence whose orientation is changed
     */

    private void playerChangeFenceOrientation(Fence fence) {
        String orientation = null;
        boolean isValidOrientation = false;

        do {
            System.out.println("What is the orientation of your fence ? (H(ORIZONTAL) or V(ERTICAL))");
            orientation = this.getBoard().chooseOrientation();
            isValidOrientation = this.getBoard().isValidOrientation(orientation);

            if(!isValidOrientation) {
                System.out.println(orientation+" is not a valid orientation\nTry again.");
            }
        }while(!isValidOrientation);
        fence.setOrientation(orientation);
    }

    /**
     * Ask where the user wants to place the given fence and place it if's a valid fence
     * 
     * @param fence Fence placed
     * @return True if the player placed the fence and false if he cancelled his move
     */

    private boolean playerPlaceFence(Fence fence) {
        Point point = null;
        boolean isFenceValid = false;

        do {
            System.out.println("Where do you want to put your fence ? (X,Y)");
            try {
                point = Point.choosePoint();
                fence.setStart(point);
                isFenceValid = this.getBoard().placeFence(this.getCurrentPawn().getId(), fence);
            } catch (NumberFormatException ignored) {

            } catch (IncorrectPawnIndexException e) {
                // If this exception is thrown the pawn ids are incorrect
                System.err.println(e.getMessage());
                System.exit(-1);
            }
            if(!isFenceValid) {
                System.out.println("The fence can't be placed here (Starting point:" + fence.getStart() + ").");
                System.out.println("Enter 'c' to cancel and to go back to the action selection or 't' to try again another position: ");
                String response = CYPath.scanner.nextLine();
                if(response.toUpperCase().matches("C(ANCEL)?")) {
                    return false;
                }
            }
        } while(!isFenceValid);
        return true;
    }
    
    /**
     * Launch the current game in console mode
     * 
     * @throws Exception If the game could not be launched or if a round was invalid
     */

    public void launch() throws Exception {
        String response = "";
        Pawn currentPawn = null;
        boolean nextTurn = true;
        try{
            // Play until there is a winner
            while(this.getBoard().getWinner() == -1){
                this.getBoard().displayBoard(DisplayType.NO_COORD);

                System.out.println("Turn of player: " +  this.getCurrentPlayer() + " (pawn id: " + this.getCurrentPawn().getId() + ")");
                currentPawn = this.getCurrentPawn();
                List<Point> listPossibleMoves = this.getBoard().getCurrentPossibleMoves();
                
                do {
                    boolean canPlaceFence = true;
                    boolean canMove = true;
                    System.out.println();
                    if(currentPawn.getAvailableFences() == 0) {
                        System.out.println("You don't have any fence remaining");
                        canPlaceFence = false;
                    } else {
                        System.out.println("You have "+currentPawn.getAvailableFences()+ " fences remaining.\n");
                    }

                    if(listPossibleMoves.isEmpty()) {
                        System.out.println("You can't move");
                        canMove = false;
                    }

                    nextTurn = true;
                    response = getUserActionChoice(canPlaceFence,canMove);

                    if(response.matches("M(OVE)?")){
                        this.getBoard().displayBoard(DisplayType.COORD_CELL);

                    
                        System.out.println("Those are the cells where your pawn can move to:");
                        System.out.println(listPossibleMoves);
                        
                        playerMovePawn();
                        
                    } else if (response.matches("F(ENCE)?")) {
                        this.getBoard().displayBoard(DisplayType.COORD_LINE);
                        Fence fence = new Fence(this.getBoard().getFenceLength());

                        playerChangeFenceOrientation(fence);
                        nextTurn = playerPlaceFence(fence);
                    }
                    else{
                        System.out.println("You can't move or place a fence. Skip your turn");
                    }
                }while(!nextTurn);
                this.endPlayerTurn();
                this.getBoard().clearCurrentPossibleMoves();
            }

            Player playerWinner = this.getBoard().getPawn(this.getBoard().getWinner()).getPlayer();
            System.out.println("The winner is "+playerWinner);            
        } catch (IncorrectPawnIndexException e) {
            // If this exception is thrown the pawn ids are incorrect
            throw e;
        }
	}
}
