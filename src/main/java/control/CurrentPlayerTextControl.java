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
	private Text currentPlayer;
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
	 * @param observable
	 * @param o
	 */
	@Override
	public void update(Observable observable, Object o) {
		currentPlayer.setText(this.game.getCurrentPlayer().getUsername()+"'s turn");
	}
}
