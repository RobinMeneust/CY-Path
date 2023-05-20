package abstraction; /**
 * Abstract class representing the current game with its parameters and rules
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public abstract class GameAbstract {
	/**
	 * State the Game's class attributes
	 */
	
	private GameState state;
	private int nbFences;
	private Player[] players;
	private Board board;
	private int currentPlayerIndex;

	/**
	 * Create a abstrac.GameAbstract object by giving all of its attributes
	 * 
	 * @param players Array of the players
	 * @param nbFences Maximum number of fences that can be placed in total
	 * @param nbRows Number of rows of the board
	 * @param nbCols Number of columns of the board
	 * @throws InvalidNumberOfPlayersException If the number of players is incorrect
	 */

	public GameAbstract(Player[] players, int nbFences, int nbRows, int nbCols) throws InvalidNumberOfPlayersException {
		this.state = GameState.READY;
		if(players == null || (players.length != 2 && players.length != 4)){
			throw new InvalidNumberOfPlayersException();
		}
		this.nbFences = nbFences;
		this.players = players;
		this.board = new Board(nbCols, nbRows, this);
		this.currentPlayerIndex = 0;
	}

	/**
	 * Get the index of the current player
	 * 
	 * @return Index of the curretn player
	 */

	public int getCurrentPlayerIndex() {
		return this.currentPlayerIndex;
	}

	/**
	 * Set the current player index to the one with the givenIndex
	 * 
	 * @param currentPlayer New current player index
	 */

	protected void setCurrentPlayerIndex(int currentPlayer) {
		if(currentPlayer>=0) {	
			this.currentPlayerIndex = currentPlayer % this.getNbPlayers();
		}
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
	 * @return abstrac.Player corresponding to the given index
	 */

	public Player getPlayer(int index){
		return players[index];
	}

	/**
	 * Get the maximum number of fences
	 * 
	 * @return Maximum number of fences
	 */

	public int getNbFences() {
		return nbFences;
	}

	/**
	 * Get the board on which the current game is played
	 * 
	 * @return abstrac.Board used for this game
	 */
	
	public Board getBoard() {
		return this.board;
	}
}
