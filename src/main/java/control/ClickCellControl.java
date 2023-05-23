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
			ClickAddBorderControl clickAddBorderControl = new ClickAddBorderControl(this.cyPathFX, (GameFX) this.game, this.actionButton, this.fence);
			clickAddBorderControl.handle(mouseEvent);
		// Change the orientation of a fence
		} else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			FenceOrientationControl fenceOrientationControl = new FenceOrientationControl(this.cyPathFX, this.fence);
			fenceOrientationControl.handle(mouseEvent);
		}
	}
}
