package control;

import abstraction.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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
	/**
	 * Graphical interface
	 */
	private CYPathFX cyPathFX;
	/**
	 * Fence wanted to be placed
	 */
	private Fence fence;

	/**
	 * Main constructor of HoverBorderControl
	 * 
	 * @param cyPathFX Reference to the JavaFX interface
	 * @param fence Reference to Fence
	 */

	public HoverBorderControl(CYPathFX cyPathFX, Fence fence) {
		this.cyPathFX = cyPathFX;
		this.fence = fence;
	}

	/**
	 * Handler of a cell when the cursor hovers it.
	 * This handler helps the players to acknowledge where he can move his pawn or where it's possible to place a fence.
	 * @param event	Event of the mouse when it entered or exited a cell
	 * 
	 */
	@Override
	public void handle(MouseEvent event) {
		Object o = event.getSource();
		if (o instanceof StackPane) {
			StackPane stackPane = (StackPane) o;
			Node sourceNode = stackPane.getChildren().get(stackPane.getChildren().size() - 1); // Get the last element (it's a pawn if there is one on the cell)
			Shape sourceCell = null;
			if (sourceNode instanceof Rectangle) {
				sourceCell = (Rectangle) sourceNode;
			} else if (sourceNode instanceof Circle) {
				sourceCell = (Circle) sourceNode;
			}else{
				System.err.println("ERROR: Can't cast to a rectangle or a circle. The board content is invalid");
				System.exit(-1);
			}
			// The player wants to place a fence
			if (!this.cyPathFX.isMoveMode()) {
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED || event.getEventType() == MouseEvent.MOUSE_CLICKED) {
					Point pStartCell = CYPathFX.getGPaneNodeCoord(stackPane);
					Point pStartFenceCoord = CYPathFX.gPaneCellCoordToGameCoord(pStartCell);

					Fence fence = new Fence(this.cyPathFX.getGame().getBoard().getFenceLength(), this.fence.getOrientation(), pStartFenceCoord);
					if (this.cyPathFX.getGame().getBoard().isFencePositionValid(fence)) {
						highlightFence(fence.getOrientation(), pStartCell, fence.getLength(), Color.DARKGREEN);
					} else {
						highlightFence(fence.getOrientation(), pStartCell, fence.getLength(), Color.DARKRED);
					}

				} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					// If the cursor moves from the current cell, we delete the current temporary fence for the next one to appear properly
					this.resetHighlightedFences(this.cyPathFX);
				}
				// The player wants to move
			} else if (this.cyPathFX.getPreviousPossibleCells() != null && this.cyPathFX.getPreviousPossibleCells().contains(sourceCell) && sourceCell != null) {
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
					sourceCell.setFill(this.cyPathFX.getCellColorHover());
				} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					sourceCell.setFill(this.cyPathFX.getGame().getCurrentPawn().getColor().toColorPossibleMove());
				}
			}
		}
	}

	private void highlightFence(Orientation orientation, Point start, int length, Color color) {
		if(orientation == Orientation.HORIZONTAL) {
			int y = start.getY() - 1; // get the upper border
			for (int i = start.getX(); i < start.getX() + 2 * length; i += 2) {
				highlightLine(i, y, color);
			}
		} else {
			int x = start.getX() - 1; // get the left border
			for (int i = start.getY(); i < start.getY() + 2 * length; i += 2) {
				highlightLine(x, i, color);
			}
		}
	}


	/**
	 * Set the color the dummy fence for the player to show where it is possible to place a fence.
	 * 
	 * @param x	The X coordinate of the origin of the fence on the grid
	 * @param y The Y coordinate of the origin of the fence on the grid
	 * @param newColor New color of the line
	 */

	private void highlightLine(int x, int y, Color newColor) {
		Node n = this.cyPathFX.getNodeFromGridPane(this.cyPathFX.getGPane(), y, x);
		if (n instanceof Line) {
			Line l = (Line) n;
			if (l.getStroke() != Color.BLACK) {
				// If it's not already a border
				l.setStroke(newColor);
				l.toFront();
				this.cyPathFX.getPrevHighlightedFencesList().add(l);
			}
		}
	}

	/**
	 * Resets the separation of the grid to the original.
	 * With this, the old dummy fence is deleted to let the new one be visible and distinct from the rest of the grid.
	 * 
	 * @param cyPathFX Reference to the JavaFX interface
	 */

	public void resetHighlightedFences(CYPathFX cyPathFX) {
		if (cyPathFX.getPrevHighlightedFencesList() != null) {
			for (Line l : cyPathFX.getPrevHighlightedFencesList()) {
				// If the color is BLACK, it means that it's border already placed, and it must not be changed.
				if (l.getStroke() != Color.BLACK) {
					// If it's not already a border
					l.setStroke(Color.LIGHTGRAY);
					l.toBack();
				}
			}
			cyPathFX.getPrevHighlightedFencesList().clear();
		}
	}
}
