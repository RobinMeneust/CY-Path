package control;

import abstraction.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import presentation.CYPathFX;
import javafx.scene.control.Button;

public class ClickAddBorderControl implements EventHandler<MouseEvent> {

	private CYPathFX cyPathFX;
	private GameFX game;
	private Button actionButton;
	private Fence fence;

	public ClickAddBorderControl(CYPathFX cyPathFX, GameFX game, Button actionButton, Fence fence) {
		this.cyPathFX = cyPathFX;
		this.game = game;
		this.actionButton = actionButton;
		this.fence = fence;
	}

	@Override
	public void handle(MouseEvent event) {
		Object o = event.getSource();
		if (o instanceof StackPane) {
			StackPane stackPane = (StackPane) o;
			if (stackPane.getChildren().get(stackPane.getChildren().size() - 1) instanceof Rectangle) {
				Rectangle sourceCell = (Rectangle) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
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

						this.cyPathFX.removeCircleFromCell(this.cyPathFX.gPane, pawn.getPosition().getY() * 2 + 1, pawn.getPosition().getX() * 2 + 1);

						StackPane parentStackPane = (StackPane) sourceCell.getParent(); // Récupérer le StackPane parent
						int columnIndex = GridPane.getColumnIndex(parentStackPane); // Obtenir l'indice de colonne du StackPane
						int rowIndex = GridPane.getRowIndex(parentStackPane); // Obtenir l'indice de ligne du StackPane
						pawn.setPosition(new Point(columnIndex / 2, rowIndex / 2));

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
}
