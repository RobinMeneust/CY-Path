package abstraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

/**
 * Abstract class representing the current game with its parameters and rules
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

@SuppressWarnings("deprecation")
public abstract class GameAbstract extends Observable {
	/**
	 * Maximal total number of fence available
	 */
	private int nbMaxTotalFences;
	/**
	 * Table of the players
	 */
	private Player[] players;
	/**
	 * The board playing on
	 */
	private Board board;
	/**
	 * Index of the current pawn
	 */
	private int currentPawnIndex;

	/**
	 * Create a GameAbstract object by giving all of its attributes
	 * 
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

	public GameAbstract(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, int fenceLength) throws PlayersPawnMapInvalidException, InvalidNumberOfPlayersException, InvalidBoardSizeException, InvalidFenceLengthException {
		if(players == null || (players.length != 2 && players.length != 4)){
			throw new InvalidNumberOfPlayersException();
		}

		if(playersPawnIndex == null){
			throw new PlayersPawnMapInvalidException();
		}
		
		// Check if all the players have a pawn
		List<Player> listPlayers = Arrays.asList(players);
		for(int i=0; i<players.length; i++) {
			if(!listPlayers.contains(playersPawnIndex.get(i))) {
				throw new PlayersPawnMapInvalidException();
			}
		}

		this.nbMaxTotalFences = Math.max(0,nbMaxTotalFences);
		this.players = players;

		Pawn[] pawns = new Pawn[players.length];
		for(int i=0; i<pawns.length;i++) {
			pawns[i] = new Pawn(i, Side.values()[i], ColorPawn.values()[i], playersPawnIndex.get(i), nbMaxTotalFences, players.length);
		}

		try {
			this.board = new Board(nbCols, nbRows, this, pawns, fenceLength);
		} catch(Exception e) {
			throw e;
		}
		this.currentPawnIndex = 0;
	}

	/**
	 * Create a GameAbstract object by giving all of its attributes
	 *
	 * @param players Array of the players
	 * @param nbMaxTotalFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
	 * @param playersPawnIndex Player associated to each pawn index associated
	 * @param pawns Table of pawns to be assigned for every player
	 * @param currentPawnIndex Set the pawn ready to be played
	 * @param fenceLength Length of the fences
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 * @throws InvalidBoardSizeException If the board size is incorrect (too small)
	 * @throws InvalidFenceLengthException If the fence length is incorrect (negative, equals to 0 or too large for the board)
	 */
	public GameAbstract(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, Pawn[] pawns, int currentPawnIndex, int fenceLength) throws InvalidNumberOfPlayersException, InvalidBoardSizeException, InvalidFenceLengthException {
		if(players == null || (players.length != 2 && players.length != 4)){
			throw new InvalidNumberOfPlayersException();
		}
		this.nbMaxTotalFences = nbMaxTotalFences;
		this.players = players;
		
		try {
			this.board = new Board(nbCols, nbRows, this, pawns, fenceLength);
		} catch(Exception e) {
			throw e;
		}
		
		for(int i=0; i<pawns.length;i++) {
			pawns[i].setPlayer(playersPawnIndex.get(i));
		}
		this.currentPawnIndex = currentPawnIndex;
	}


	/**
	 * Get index of the pawn currently played
	 * @return Index of current pawn played
	 */
	public int getCurrentPawnIndex() {
		return this.currentPawnIndex;
	}


	/**
	 * Get the pawn currently played
	 * @return Current pawn being played
	 */
	public Pawn getCurrentPawn() {
		Pawn current = null;
		try {
			current = this.getBoard().getPawn(this.getCurrentPawnIndex());
		} catch (IncorrectPawnIndexException e) {
			// Since the current pawn index is lesser than the number of pawns it shouldn't happen, so the index is incorrect, so we exit the program
			e.printStackTrace();
			System.exit(-1);
		}
		return current;
	}

	/**
	 * Set the current player index to the one with the givenIndex
	 * 
	 * @param currentPawn New current player index
	 */

	private void setCurrentPawnIndex(int currentPawn) {
		if(currentPawn>=0) {
			this.currentPawnIndex = currentPawn % this.getNbPlayers();
		}
	}

	/**
	 * End the turn of the current pawn by changing the current pawn index to the next one.
	 */
	protected void endPlayerTurn() {
		int newPawnIndex = (this.getCurrentPawnIndex()+1) % this.getBoard().getNbPawns();
		this.setCurrentPawnIndex(newPawnIndex);
	}


	/**
	 * Get the player currently playing from the index of the pawn currently playing.
	 * @return Player currently playing
	 */
	public Player getCurrentPlayer() {
		try {
			int currentPawnIndex = getCurrentPawnIndex();
			return this.getBoard().getPawn(currentPawnIndex).getPlayer();
		} catch (IncorrectPawnIndexException e) {
			// It should not happen since the currentPlayerIndex is positive and lesser than the size of the players array
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	/**
	 * Abstract method used to launch a Game on a board in either console or window mode
	 */

	public abstract void launch();

	/**
	 * Get the number of players
	 * 
	 * @return Number of players
	 */

	public int getNbPlayers() {
		return this.players.length;
	}


	/**
	 * Get a player from its index
	 * 
	 * @param index Index of the player
	 * @return Player corresponding to the given index
	 * @throws IncorrectPlayerIndexException if the index is invalid
	 */

	public Player getPlayer(int index) throws IncorrectPlayerIndexException {
		if(index>=0 && index<players.length) {
			return players[index];
		} else {
			throw new IncorrectPlayerIndexException();
		}
	}

	/**
	 * Get the maximum number of fences
	 * 
	 * @return Maximum number of fences
	 */

	public int getNbMaxTotalFences() {
		return this.nbMaxTotalFences;
	}

	/**
	 * Get the board on which the current game is played
	 * 
	 * @return Board used for this game
	 */
	
	public Board getBoard() {
		return this.board;
	}
}
