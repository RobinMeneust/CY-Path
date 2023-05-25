package control;

import abstraction.GameFX;
import abstraction.IncorrectJavaFXBoardException;
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
		this.getCyPathFX().resetPossibleCells(this.getGame().getCurrentPawnIndex());
		// Change the text inside the button depending on the action
		if (this.getActionButton().getText().equals("Place fence") && this.getGame().getCurrentPawn().getAvailableFences() > 0) {
			this.getGame().setAction("Place fence");
			this.getCyPathFX().setMoveMode(false);
			this.getActionButton().setText("Move");
		}else{
			try {
				this.getCyPathFX().showPossibleCells(this.getGame().getCurrentPawnIndex());
			} catch(IncorrectJavaFXBoardException e) {
				System.err.println(e.getMessage());
			}
			this.getGame().setAction("Move");
			this.getCyPathFX().setMoveMode(true);
			this.getActionButton().setText("Place fence");
		}

		//Update fenceCounter
		this.getCyPathFX().getFenceCounter().setText(this.getCyPathFX().getGame().getCurrentPawn().getAvailableFences()+" fence(s) remaining.");
	}

	/**
	 * Get the action button
	 * @return Action button
	 */
	public Button getActionButton() {
		return actionButton;
	}

	/**
	 * Set the action button
	 * @param actionButton Button of action
	 */
	public void setActionButton(Button actionButton) {
		this.actionButton = actionButton;
	}

	/**
	 * Get the current game
	 * @return Game currently playing
	 */
	public GameFX getGame() {
		return game;
	}

	/**
	 * Set the game
	 * @param game Game wanting to be changed
	 */
	public void setGame(GameFX game) {
		this.game = game;
	}

	/**
	 * Get the JavaFX graphical interface
	 * @return JavaFX Graphical interface of the application
	 */
	public CYPathFX getCyPathFX() {
		return cyPathFX;
	}

	/**
	 * Set the JavaFX graphical interface used in the application
	 * @param cyPathFX avaFX graphical interface
	 */
	public void setCyPathFX(CYPathFX cyPathFX) {
		this.cyPathFX = cyPathFX;
	}
}