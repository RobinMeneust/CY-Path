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

	private CYPathFX cyPathFX;
	private GameFX game;
	private Button actionButton;
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
	 * Handler when a cell is clicked to place a fence.
	 * This handler places a fence in the available row or column between two cells.
	 * With that, a player can place a fence wherever he wants if the position of the wanted fence complies with the game's rules.
	 * @param event	Event of the mouse when it's pressed
	 */
	@Override
	public void handle(MouseEvent event) {
		Object o = event.getSource();
		if (o instanceof StackPane) {
			StackPane stackPane = (StackPane) o;
			Shape sourceCell = null;

			// The cell retrieved may be just a rectangle or have a circle in it.
			if (stackPane.getChildren().get(stackPane.getChildren().size() - 1) instanceof Rectangle) {
				sourceCell = (Rectangle) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
			} else if (stackPane.getChildren().get(stackPane.getChildren().size() - 1) instanceof Circle) {
				sourceCell = (Circle) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
			} else {
				System.err.println("ERROR: Can't cast to a rectangle or a circle");
			}
			if (!this.cyPathFX.isMoveMode() && this.cyPathFX.prevHighlightedFencesList != null && event.getButton() == MouseButton.PRIMARY) {
				//Update data
				Fence fence = null;
				Point pStartCell = new Point(0, 0);
				Point pStartFenceCoord = new Point(0, 0);

				// Convert from grid coordinates to fence coordinates
				pStartCell.setX(GridPane.getColumnIndex(stackPane));
				pStartCell.setY(GridPane.getRowIndex(stackPane));

				pStartFenceCoord.setX((pStartCell.getX() - 1) / 2);
				pStartFenceCoord.setY((pStartCell.getY() - 1) / 2);


				fence = new Fence(this.game.getBoard().getFenceLength(), this.fence.getOrientation(), pStartFenceCoord);

				try {
					if (this.game.getBoard().placeFence(this.game.getCurrentPlayerIndex(), fence)) {
						// Add fence to the gridPane
						for (Line l : this.cyPathFX.prevHighlightedFencesList) {
							l.setStroke(Color.BLACK);
							l.toFront();
							// Clear the prevHighlightedFencesList so that the color isn't removed when the mouse is moved
						}
						this.cyPathFX.prevHighlightedFencesList.clear();


						this.game.setIsEndTurn(true);

						//We wait the begining of the next turn
						while (this.game.getIsEndTurn()) {
							try {
								Thread.sleep(100); //Wait 100 milliseconds before checking again
							} catch (InterruptedException ev) {
								ev.printStackTrace();
							}
						}
						//update button
						actionButton.fire();
					} else {
						System.out.println("The fence can't be placed here (Starting point:" + fence.getStart() + ").\nTry again.");
					}
				} catch (IncorrectPawnIndexException e) {
					System.err.println("ERROR: Pawn index is incorrect. Check the number of players and the number of pawns and see if they are equals");
					System.exit(-1);
				}
			} else if (this.cyPathFX.isMoveMode() && this.cyPathFX.previousPossibleCells != null && this.cyPathFX.previousPossibleCells.contains(sourceCell)) {
				try {
					Pawn pawn = this.game.getBoard().getPawn(this.game.getCurrentPlayerIndex());

					// We move circle (pawn of the player) to its new location
					this.cyPathFX.removeCircleFromCell(this.cyPathFX.gPane, pawn.getPosition().getY() * 2 + 1, pawn.getPosition().getX() * 2 + 1);

					StackPane parentStackPane = (StackPane) sourceCell.getParent();
					int columnIndex = GridPane.getColumnIndex(parentStackPane);
					int rowIndex = GridPane.getRowIndex(parentStackPane);
					
					this.game.getBoard().movePawn(this.game.getPawn(this.game.getCurrentPlayer()).getId(), new Point(columnIndex / 2, rowIndex / 2));

					this.cyPathFX.addCircleToCell(this.cyPathFX.gPane, rowIndex, columnIndex, pawn.getColor());
					//the information is transmitted to the terminal
					this.game.setIsEndTurn(true);
					//We wait the begining of the next turn
					while (this.game.getIsEndTurn()) {
						try {
							Thread.sleep(100); //Wait 100 milliseconds before checking again
						} catch (InterruptedException ev) {
							ev.printStackTrace();
						}
					}
					//update button
					actionButton.fire();
					if (!(actionButton.getText().equals("Move"))) {
						actionButton.fire();
					}
				} catch (IncorrectPawnIndexException err) {
					System.err.println(err);
					System.exit(-1);
				}
			}
		}
	}
}
