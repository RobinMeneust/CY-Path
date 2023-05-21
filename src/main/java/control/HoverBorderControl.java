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

public class HoverBorderControl implements EventHandler<MouseEvent> {

	private CYPathFX cyPathFX;
	private Fence fence;

	public HoverBorderControl(CYPathFX cyPathFX, Fence fence) {
		this.cyPathFX = cyPathFX;
		this.fence = fence;
	}

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
			if (!this.cyPathFX.isMoveMode()) {
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
					Point pStartCell = new Point(0, 0);
					Point pStartFenceCoord = new Point(0, 0);


					// Convert from grid coordinates to fence coordinates
					pStartCell.setX(GridPane.getColumnIndex(stackPane));
					pStartCell.setY(GridPane.getRowIndex(stackPane));

					pStartFenceCoord.setX((pStartCell.getX() - 1) / 2);
					pStartFenceCoord.setY((pStartCell.getY() - 1) / 2);


					if (this.fence.getOrientation() == Orientation.HORIZONTAL) {
						Fence fence = new Fence(this.cyPathFX.game.getBoard().getFenceLength(), Orientation.HORIZONTAL, pStartFenceCoord);
						if (this.cyPathFX.game.getBoard().isFencePositionValid(fence)) {
							// we take the row above the cell
							int y = pStartCell.getY() - 1;
							for (int i = pStartCell.getX(); i < pStartCell.getX() + 2 * fence.getLength(); i += 2) {
								Node n = this.cyPathFX.getNodeFromGridPane(this.cyPathFX.gPane, y, i);
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
								Node n = this.cyPathFX.getNodeFromGridPane(this.cyPathFX.gPane, i, x);
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
					if (this.cyPathFX.prevHighlightedFencesList != null) {
						for (Line l : this.cyPathFX.prevHighlightedFencesList) {
							if (l.getStroke() != Color.BLACK) {
								// If it's not already a border
								l.setStroke(Color.LIGHTGRAY);
								l.toBack();
							}
						}
						this.cyPathFX.prevHighlightedFencesList.clear();
					}
				}
			} else if (this.cyPathFX.previousPossibleCells != null && this.cyPathFX.previousPossibleCells.contains(sourceCell)) {
				if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
					sourceCell.setFill(this.cyPathFX.cellColorHover);
				} else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
					sourceCell.setFill(this.cyPathFX.possibleCellColor);
				}
			}
		}
	}
}
