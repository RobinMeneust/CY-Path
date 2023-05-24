package control;

import abstraction.GameFX;
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
	/**
	 * Button to choose the action
	 */
	private Button actionButton;
	/**
	 * Game currently playing to know the current action
	 */
	private GameFX game;
	/**
	 * Graphical interface
	 */
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
		if (this.actionButton.getText().equals("Place fence") && this.game.getCurrentPawn().getAvailableFences() > 0) {
			this.game.setAction("Place fence");
			this.cyPathFX.setMoveMode(false);
			this.actionButton.setText("Move");
		//} else if(this.actionButton.getText().equals("Move")){
		}else{
			this.cyPathFX.showPossibleCells(this.game.getCurrentPawnIndex());
			this.game.setAction("Move");
			this.cyPathFX.setMoveMode(true);
			this.actionButton.setText("Place fence");
		}

		//Update fenceCounter
		this.cyPathFX.fenceCounter.setText(this.cyPathFX.game.getCurrentPawn().getAvailableFences()+" fence(s) remaining.");
	}
}