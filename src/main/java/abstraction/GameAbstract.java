package abstraction;

import java.util.HashMap;
import java.util.Observable;

/**
 * Abstract class representing the current game with its parameters and rules
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

@SuppressWarnings("deprecation")
public abstract class GameAbstract extends Observable {
	/**
	 * State the Game's class attributes
	 */
	
	private GameState state;
	private int nbMaxTotalFences;
	private Player[] players;
	private Board board;
	private int currentPawnIndex;

	/**
	 * Create a GameAbstract object by giving all of its attributes
	 * 
	 * @param players Array of the players
	 * @param nbFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
	 * @param playersPawnIndex Player associated to each pawn index associated
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 */

	public GameAbstract(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex) throws InvalidNumberOfPlayersException {
		this.state = GameState.READY;
		if(players == null || (players.length != 2 && players.length != 4)){
			throw new InvalidNumberOfPlayersException();
		}
		this.nbMaxTotalFences = nbMaxTotalFences;
		this.players = players;

		Pawn[] pawns = new Pawn[players.length];
		for(int i=0; i<players.length;i++) {
			pawns[i] = new Pawn(i, Side.values()[i], ColorPawn.values()[i], playersPawnIndex.get(i), nbMaxTotalFences, players.length);
		}
		
		this.board = new Board(nbCols, nbRows, this, pawns);
		for(int i=0; i<players.length;i++) {
			pawns[i].setBoard(this.getBoard());
		}
		this.currentPawnIndex = 0;
	}

	public GameAbstract(Player[] players, int nbMaxTotalFences, int nbRows, int nbCols, HashMap<Integer,Player> playersPawnIndex, Pawn[] pawns, int currentPawnIndex) throws InvalidNumberOfPlayersException {
		this.state = GameState.READY;
		if(players == null || (players.length != 2 && players.length != 4)){
			throw new InvalidNumberOfPlayersException();
		}
		this.nbMaxTotalFences = nbMaxTotalFences;
		this.players = players;
	
		this.board = new Board(nbCols, nbRows, this, pawns);
		this.currentPawnIndex = currentPawnIndex;
	}	


	public int getCurrentPawnIndex() {
		return this.currentPawnIndex;
	}

	public Pawn getCurrentPawn() {
		Pawn current = null;
		try {
			current = this.getBoard().getPawn(getCurrentPawnIndex());
			return current;
		} catch (IncorrectPawnIndexException e) {
			// It should not happen since the current pawn index needs to be positive and lesser than the number of pawns
			// So if this is catched it's link to other errors
			System.err.println(e);
			System.exit(-1);
		}
		return current;
	}

	/**
	 * Set the current player index to the one with the givenIndex
	 * 
	 * @param currentPlayer New current player index
	 */

	private void setCurrentPawnIndex(int currentPawn) {
		if(currentPawn>=0) {	
			this.currentPawnIndex = currentPawn % this.getNbPlayers();
		}
	}

	protected void endPlayerTurn() {
		int newPawnIndex = (this.getCurrentPawnIndex()+1) % this.getBoard().getNbPawns();
		this.setCurrentPawnIndex(newPawnIndex);
	}

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
	 * Get the current state of the game
	 * 
	 * @return State of the game
	 */

	public GameState getState() {
		return state;
	}

	/**
	 * Set the current state of the game to a new state
	 * 
	 * @param state New game state
	 */

	public void setState(GameState state){
		this.state = state;
	}

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
