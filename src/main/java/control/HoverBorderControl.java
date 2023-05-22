package control;

import abstraction.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import presentation.CYPathFX;

/**
 * Control when the player hovers his cursor on the board.
 *
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */
public class HoverBorderControl implements EventHandler<MouseEvent> {

	private CYPathFX cyPathFX;
	private Fence fence;

	/**
	 * @param cyPathFX	Reference to the JavaFX interface
	 * @param fence		Reference to abstraction.Fence
	 */
	public HoverBorderControl(CYPathFX cyPathFX, Fence fence) {
		this.cyPathFX = cyPathFX;
		this.fence = fence;
	}

	/**
	 * Handler of a cell when the cursor hovers it.
	 * This handler helps the players to acknowledge where he can move his pawn or where it's possible to place a fence.
	 * @param event	Event of the mouse when it entered or exited a cell
	 */
	@Override
	public void handle(MouseEvent event) {
		Object o = event.getSource();
		if (o instanceof StackPane) {
			StackPane stackPane = (StackPane) o;
			Shape sourceCell = null;
			if (stackPane.getChildren().get(stackPane.getChildren().size() - 1) instanceof Rectangle) {
				sourceCell = (Rectangle) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
			} else if (stackPane.getChildren().get(stackPane.getChildren().size() - 1) instanceof Circle) {
				sourceCell = (Circle) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
			}else{
				System.err.println("ERROR: Can't cast to a rectangle or a circle");
			}
			// The player wants to place a fence
			if (!this.cyPathFX.isMoveMode()) {
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED || event.getEventType() == MouseEvent.MOUSE_CLICKED) {
					Point pStartCell = new Point(0, 0);
					Point pStartFenceCoord = new Point(0, 0);

					// Convert from grid coordinates to fence coordinates
					pStartCell.setX(GridPane.getColumnIndex(stackPane));
					pStartCell.setY(GridPane.getRowIndex(stackPane));

					pStartFenceCoord.setX((pStartCell.getX() - 1) / 2);
					pStartFenceCoord.setY((pStartCell.getY() - 1) / 2);

					// Depending on the orientation of the fence, a dummy fence will be displayed to show where it is possible to place a fence.
					if (this.fence.getOrientation() == Orientation.HORIZONTAL) {
						Fence fence = new Fence(this.cyPathFX.game.getBoard().getFenceLength(), Orientation.HORIZONTAL, pStartFenceCoord);
						if (this.cyPathFX.game.getBoard().isFencePositionValid(fence)) {
							// we take the row above the cell
							int y = pStartCell.getY() - 1;
							for (int i = pStartCell.getX(); i < pStartCell.getX() + 2 * fence.getLength(); i += 2) {
								setColorLine(i, y);
							}
						} else {
							Node n = this.cyPathFX.getNodeFromGridPane(this.cyPathFX.gPane, GridPane.getRowIndex(stackPane) - 1, GridPane.getColumnIndex(stackPane)); // get the upper border of the cell
							if (n instanceof Line) {
								Line l = (Line) n;
								if (l.getStroke() != Color.BLACK) {
									// If it's not already a border
									l.setStroke(Color.DARKRED);
									l.toFront();
									this.cyPathFX.prevHighlightedFencesList.add(l);
								}
							}
						}
					} else if (this.fence.getOrientation() == Orientation.VERTICAL) {
						Fence fence = new Fence(this.cyPathFX.game.getBoard().getFenceLength(), Orientation.VERTICAL, pStartFenceCoord);
						if (this.cyPathFX.game.getBoard().isFencePositionValid(fence)) {
							int x = pStartCell.getX() - 1;
							for (int i = pStartCell.getY(); i < pStartCell.getY() + 2 * fence.getLength(); i += 2) {
								setColorLine(x, i);
							}
						} else {
							Node n = this.cyPathFX.getNodeFromGridPane(this.cyPathFX.gPane, GridPane.getRowIndex(stackPane), GridPane.getColumnIndex(stackPane) - 1); // get the left border of the cell
							if (n instanceof Line) {
								Line l = (Line) n;
								if (l.getStroke() != Color.BLACK) {
									// If it's not already a border
									l.setStroke(Color.DARKRED);
									l.toFront();
									this.cyPathFX.prevHighlightedFencesList.add(l);
								}
							}
						}
					}
				} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					// If the cursor moves from the current cell, we delete the current dummy fence for the next one to appear properly
					this.resetHighlightedFences(this.cyPathFX);
				}
			// The player wants to move
			} else if (this.cyPathFX.previousPossibleCells != null && this.cyPathFX.previousPossibleCells.contains(sourceCell) && sourceCell != null) {
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
					sourceCell.setFill(this.cyPathFX.cellColorHover);
				} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					try {
						sourceCell.setFill(this.cyPathFX.game.getBoard().getPawn(this.cyPathFX.game.getCurrentPawnIndex()).getColor().toColorPossibleMove());
					}catch (IncorrectPawnIndexException err){
						System.err.println(err);
					}
				}
			}
		}
	}


	/**
	 * Set the color the dummy fence for the player to show where it is possible to place a fence.
	 * @param x	The X coordinate of the origin of the fence on the grid
	 * @param y The Y coordinate of the origin of the fence on the grid
	 */
	private void setColorLine(int x, int y) {
		Node n = this.cyPathFX.getNodeFromGridPane(this.cyPathFX.gPane, y, x);
		if (n instanceof Line) {
			Line l = (Line) n;
			if (l.getStroke() != Color.BLACK) {
				// If it's not already a border
				l.setStroke(Color.DARKGREEN);
				l.toFront();
				this.cyPathFX.prevHighlightedFencesList.add(l);
			}
		}
	}

	/**
	 * Resets the separation of the grid to the original.
	 * With this, the old dummy fence is deleted to let the new one be visible and distinct from the rest of the grid.
	 * @param cyPathFX Reference to the JavaFX interface
	 */
	public void resetHighlightedFences(CYPathFX cyPathFX) {
		if (cyPathFX.prevHighlightedFencesList != null) {
			for (Line l : cyPathFX.prevHighlightedFencesList) {
				// If the color is BLACK, it means that it's border already placed, and it must not be changed.
				if (l.getStroke() != Color.BLACK) {
					// If it's not already a border
					l.setStroke(Color.LIGHTGRAY);
					l.toBack();
				}
			}
			cyPathFX.prevHighlightedFencesList.clear();
		}
	}
}
