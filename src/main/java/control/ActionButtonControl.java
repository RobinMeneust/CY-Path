package control;

import abstraction.GameFX;
import abstraction.IncorrectPawnIndexException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import presentation.CYPathFX;
import javafx.scene.control.Button;

/**
 * Event that determined the player's choice between "Place a fence" and "Move".
 *
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */
public class ActionButtonControl implements EventHandler<ActionEvent> {
	private Button actionButton;
	private GameFX game;
	private CYPathFX cyPathFX;

	/**
	 * Default constructor.
	 * @param cyPathFX	Reference to the JavaFX interface
	 * @param button	Action button for the action of the player
	 * @param game		Reference to the game for the JavaFX interface
	 */
	public ActionButtonControl(CYPathFX cyPathFX, Button button, GameFX game){
		this.cyPathFX = cyPathFX;
		this.actionButton = button;
		this.game = game;
	}


	/**
	 * Handler when the button of action is pressed.
	 * This handler helps us to know what is desired action of the player when it is his turn.
	 * @param event	Event of the button when it's pressed
	 */
	@Override
	public void handle(ActionEvent event) {
		// Change the color of the cells to the default
		this.cyPathFX.resetPossibleCells(this.game.getCurrentPawnIndex());
		// Change the text inside the button depending on the action
		if (this.actionButton.getText().equals("Move") && this.game.getCurrentPawn().getAvailableFences() > 0) {
			this.game.setAction("Place fence");
			this.cyPathFX.setMoveMode(false);
		} else {
			this.cyPathFX.showPossibleCells(this.game.getCurrentPawnIndex());
			this.game.setAction("Move");
			this.cyPathFX.setMoveMode(true);
		}

		//Update fenceCounter
		this.cyPathFX.fenceCounter.setText(this.cyPathFX.game.getCurrentPawn().getAvailableFences()+" fence(s) remaining.");
	}
}