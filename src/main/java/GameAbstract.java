public abstract class GameAbstract {
	private GameState state;
	private int nbFences;
	private Player[] players;
	private Board board;
	private int currentPlayerIndex;

	public GameAbstract(int nbPlayers, Player[] players, int nbFences, int nbRows, int nbCols) throws InvalidNumberOfPlayersException{
		this.state = GameState.READY;
		if(players == null || (players.length != 2 && players.length != 4)){
			throw new InvalidNumberOfPlayersException();
		}
		this.nbFences = nbFences;
		this.players = players;
		this.board = new Board(nbCols, nbRows, this);
		this.currentPlayerIndex = 0;
	}

	public int getCurrentPlayerIndex() {
		return this.currentPlayerIndex;
	}

	protected void setCurrentPlayer(int currentPlayer) {
		if(currentPlayer>=0) {	
			this.currentPlayerIndex = currentPlayer % this.getNbPlayers();
		}
	}

	public abstract void launch();

	public GameState getState() {
		return state;
	}

	public void setState(GameState state){
		this.state = state;
	}

	public int getNbPlayers() {
		return this.players.length;
	}


	public Player getPlayer(int index){
		return players[index];
	}

	public int getNbFences() {
		return nbFences;
	}
	
	public Board getBoard() {
		return this.board;
	}
}
