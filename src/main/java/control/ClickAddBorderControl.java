package control;

import abstraction.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import presentation.CYPathFX;
import javafx.scene.control.Button;

/**
 * Control when the player wants to place a fence on the board.
 *
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class ClickAddBorderControl implements EventHandler<MouseEvent> {

	/**
	 * Graphical interface
	 */
	private CYPathFX cyPathFX;
	/**
	 * Game currently playing to know the current action
	 */
	private GameFX game;
	/**
	 * Button to choose the action
	 */
	private Button actionButton;
	/**
	 * Fence going to be placed
	 */
	private Fence fence;

	/**
	 * Default constructor.
	 * @param cyPathFX 		Reference to the JavaFX interface
	 * @param game			Reference to the game for the JavaFX interface
	 * @param actionButton	Reference to the action button
	 * @param fence			Reference to the fence to be placed
	 */

	public ClickAddBorderControl(CYPathFX cyPathFX, GameFX game, Button actionButton, Fence fence) {
		this.cyPathFX = cyPathFX;
		this.game = game;
		this.actionButton = actionButton;
		this.fence = fence;
	}

	/**
	 * Place the selected fence on the board
	 * 
	 * @param stackPane Cell clicked. The fence is either in its left or upper border depending on its orientation
	 * @throws IncorrectPawnIndexException If the current pawn index is incorrect or if its id is invalid
	 */

	private void placeFenceOnBoard(StackPane stackPane) throws IncorrectPawnIndexException {
		Point pStartFenceCoord = CYPathFX.gPaneCellCoordToGameCoord(CYPathFX.getGPaneNodeCoord(stackPane));
		Fence fence = new Fence(this.game.getBoard().getFenceLength(), this.fence.getOrientation(), pStartFenceCoord);

		try {
			// If a fence is selected (highlighted)
			if(this.cyPathFX.getPrevHighlightedFencesList() != null){
				if (this.game.getBoard().placeFence(this.game.getCurrentPawnIndex(), fence)) {
					// Add fence to the gridPane
					for (Line l : this.cyPathFX.getPrevHighlightedFencesList()) {
						l.setStroke(Color.BLACK);
						l.toFront();
					}
					// Clear the prevHighlightedFencesList so that the color isn't removed when the mouse is moved in the next round
					this.cyPathFX.getPrevHighlightedFencesList().clear();

					this.game.setIsEndTurn(true);
					//We wait the beginning of the next turn
					while (this.game.getIsEndTurn()) {
						try {
							Thread.sleep(100); //Wait 100 milliseconds before checking again
						} catch (InterruptedException ev) {
							Thread.currentThread().interrupt();
						}
					}
					//update button
					actionButton.fire();
				} else {
					System.out.println("The fence can't be placed here (Starting point:" + fence.getStart() + ").\nTry again.");
				}
			}
		} catch (IncorrectPawnIndexException e) {
			throw e;
		}
	}

	/**
	 * Place the current pawn at the selected position on the board
	 * 
	 * @param sourceCell Cell clicked. It must be either a Circle or a Rectangle
	 * @throws IncorrectShapeException If the shape is not a Circle or a Rectangle
	 * @throws IncorrectPawnIndexException If the current pawn index is incorrect or if its id is invalid
	 */

	private void placePawnOnBoard(Shape sourceCell) throws IncorrectShapeException, IncorrectPawnIndexException {
		if(!(sourceCell instanceof Circle) && !(sourceCell instanceof Rectangle)){
			throw new IncorrectShapeException();
		}
		try {
			// If there are possible moves
			if(this.cyPathFX.getPreviousPossibleCells() != null && this.cyPathFX.getPreviousPossibleCells().contains(sourceCell)) {
				Pawn pawn = this.game.getCurrentPawn();

				// We move circle (pawn of the player) to its new location
				this.cyPathFX.removeCircleFromCell(this.cyPathFX.getGPane(), pawn.getPosition().getY() * 2 + 1, pawn.getPosition().getX() * 2 + 1);

				StackPane parentStackPane = (StackPane) sourceCell.getParent();
				int columnIndex = GridPane.getColumnIndex(parentStackPane);
				int rowIndex = GridPane.getRowIndex(parentStackPane);
				
				this.game.getBoard().movePawn(this.game.getCurrentPawn().getId(), new Point(columnIndex / 2, rowIndex / 2));
				
				this.cyPathFX.addCircleToCell(this.cyPathFX.getGPane(), rowIndex, columnIndex, pawn.getColor());
				//the information is transmitted to the terminal
				this.game.setIsEndTurn(true);
				//We wait the beginning of the next turn
				CYPathFX.checkEndTurn(this.game);

				//update button
				actionButton.fire();
				if (!(actionButton.getText().equals("Place fence"))) {
					actionButton.fire();
				}
			}
		} catch (IncorrectPawnIndexException err) {
			throw err;
		}
	}

	/**
	 * Handler when a cell is clicked to place a fence.
	 * This handler places a fence in the available row or column between two cells.
	 * With that, a player can place a fence wherever he wants if the position of the wanted fence complies with the game's rules.
	 * 
	 * @param event	Event of the mouse when it's pressed
	 */

	@Override
	public void handle(MouseEvent event) {
		Object o = event.getSource();
		if (o instanceof StackPane) {
			StackPane stackPane = (StackPane) o;
			Shape sourceCell = null;

			try {
				if (!this.cyPathFX.isMoveMode() && event.getButton() == MouseButton.PRIMARY) {
					placeFenceOnBoard(stackPane);
				} else if (this.cyPathFX.isMoveMode()) {
					// The cell is a shape (rectangle if there is no pawn and circle if there is a pawn)
					if (stackPane.getChildren().get(stackPane.getChildren().size() - 1) instanceof Shape) {
						sourceCell = (Shape) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
					}
					placePawnOnBoard(sourceCell);
				}
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}