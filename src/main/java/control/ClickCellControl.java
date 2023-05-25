package control;

import abstraction.Fence;
import abstraction.GameAbstract;
import abstraction.GameFX;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presentation.CYPathFX;

/**
 * Combination of the control for handling the rotation of a fence or the placement of a fence.
 *
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */
public class ClickCellControl implements EventHandler<MouseEvent> {
	/**
	 * Graphical interface
	 */
	private CYPathFX cyPathFX;
	/**
	 * Fence wanted to interact with
	 */
	private Fence fence;
	/**
	 * Game currently playing
	 */
	private GameAbstract game;
	/**
	 * Button to choose the action
	 */
	private Button actionButton;


	/**
	 * Default constructor.
	 * @param cyPathFX 		Reference to the JavaFX interface
	 * @param fence			Reference to fence
	 * @param game			Reference to the game
	 * @param actionButton	Reference to the action button
	 */
	public ClickCellControl(CYPathFX cyPathFX, Fence fence, GameAbstract game, Button actionButton){
		this.cyPathFX = cyPathFX;
		this.fence = fence;
		this.game = game;
		this.actionButton = actionButton;
	}

	/**
	 * Depending on which mouse button is clicked, the handler helps to dissociate the two main of the mouse to perform different action.
	 * @param mouseEvent Event when the buttons mouse are pressed
	 */
	@Override
	public void handle(MouseEvent mouseEvent) {
		// Add a fence
		if(mouseEvent.getButton() == MouseButton.PRIMARY){
			ClickAddBorderControl clickAddBorderControl = new ClickAddBorderControl(this.getCyPathFX(), (GameFX) this.getGame(), this.getActionButton(), this.getFence());
			clickAddBorderControl.handle(mouseEvent);
		// Change the orientation of a fence
		} else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			FenceOrientationControl fenceOrientationControl = new FenceOrientationControl(this.getCyPathFX(), this.getFence());
			fenceOrientationControl.handle(mouseEvent);
		}
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
	 * Get the current fence
	 * @return Fence wanting to be placed
	 */
	public Fence getFence() {
		return fence;
	}

	/**
	 * Set the current fence
	 * @param fence Fence wanting to be placed
	 */
	public void setFence(Fence fence) {
		this.fence = fence;
	}
}
