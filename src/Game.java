public class Game {
	private GameState state;
	private int nbPlayers;
	private int winner;
	private int nbFences;
	private Player[] players;

	public Game(int nbPlayers, int nbFences) throws InvalidNumberOfPlayersException{
		this.state = GameState.READY;
		if(nbPlayers != 2 && nbPlayers != 4){
			throw new InvalidNumberOfPlayersException();
		}
		this.nbPlayers = nbPlayers;
		this.winner = -1;
		this.nbFences = nbFences;
		// TO-DO
		
		this.players = new Player[]
	}

	public GameState getState() {
		return state;
	}

	public int getNbPlayers() {
		return nbPlayers;
	}

	public int getWinner() {
		return winner;
	}

	public int getNbFences() {
		return nbFences;
	}
	
}
