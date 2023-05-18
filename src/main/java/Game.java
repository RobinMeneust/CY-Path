public class Game {
	private GameState state;
	private int winner;
	private int nbFences;
	private Player[] players;
	private Board board;

	public Game(int nbPlayers, Player[] players, int nbFences, int nbRows, int nbCols) throws InvalidNumberOfPlayersException{
		this.state = GameState.READY;
		if(players == null || (players.length != 2 && players.length != 4)){
			throw new InvalidNumberOfPlayersException();
		}
		this.winner = -1;
		this.nbFences = nbFences;
		this.players = players;
		this.board = new Board(nbCols,nbRows, this);
	}

	public void launch() {
		int playerId = 0;
		int winner = 0;
		while(true){
			if(this.getState() == GameState.FINISHED){
				break;
			}
			if(playerId>=this.getNbPlayers()){
				playerId = 0;
			}
			board.displayBoard();
			winner = board.play(playerId);
			playerId++;
		}
		playerId--;
		setWinner(playerId);
	}

	private void setWinner(int idPlayer) {
		this.winner = idPlayer;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state){
		this.state = state;
	}

	public int getNbPlayers() {
		return this.players.length;
	}

	public int getWinner() {
		return winner;
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
