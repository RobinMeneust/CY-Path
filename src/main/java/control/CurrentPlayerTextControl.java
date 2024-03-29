package control;

import abstraction.GameAbstract;
import javafx.scene.text.Text;

import java.util.Observable;
import java.util.Observer;

/**
 * Control of the text which shows the player's current turn.
 *
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

@SuppressWarnings("deprecation")
public class CurrentPlayerTextControl implements Observer {
	/**
	 * Name of the current player
	 */
	private Text currentPlayer;
	/**
	 * Game currently playing to get current player's username
	 */
	private GameAbstract game;


	/**
	 * Default constructor.
	 * @param game Reference to the game. It can be the game in the console or with the JavaFX interface.
	 * @param text Reference to the text that we want to change
	 */
	public CurrentPlayerTextControl(GameAbstract game, Text text){
		this.game = game;
		this.currentPlayer = text;
	}

	/**
	 * We use the state of the game to know which player's turn it is.
	 * Default method of the Observer interface
	 * @param observable The Observable object that triggered the update.
 	 * @param o The optional argument passed to the notifyObservers method.
	 */
	@Override
	public void update(Observable observable, Object o) {
		this.getCurrentPlayer().setText(this.getGame().getCurrentPlayer().getUsername()+"'s turn");
	}

	/**
	 * Get the text of the current player's turn
	 * @return Text of the current player's turn
	 */
	public Text getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Set the text of the current player, shown to the player(s)
	 * @param currentPlayer Text of the current player
	 */
	public void setCurrentPlayer(Text currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * Get the current game
	 * @return Game currently playing
	 */
	public GameAbstract getGame() {
		return game;
	}
	/**
	 * Set the game
	 * @param game Game wanting to be changed
	 */
	public void setGame(GameAbstract game) {
		this.game = game;
	}
}
