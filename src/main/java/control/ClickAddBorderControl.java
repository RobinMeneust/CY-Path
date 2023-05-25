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
		Fence fence = new Fence(this.getGame().getBoard().getFenceLength(), this.getFence().getOrientation(), pStartFenceCoord);

		try {
			// If a fence is selected (highlighted)
			if(this.getCyPathFX().getPrevHighlightedFencesList() != null){
				if (this.getGame().getBoard().placeFence(this.getGame().getCurrentPawnIndex(), fence)) {
					// Add fence to the gridPane
					for (Line l : this.getCyPathFX().getPrevHighlightedFencesList()) {
						l.setStroke(Color.BLACK);
						l.toFront();
					}
					// Clear the prevHighlightedFencesList so that the color isn't removed when the mouse is moved in the next round
					this.getCyPathFX().getPrevHighlightedFencesList().clear();

					this.getGame().setIsEndTurn(true);
					//We wait the beginning of the next turn
					while (this.getGame().getIsEndTurn()) {
						try {
							Thread.sleep(100); //Wait 100 milliseconds before checking again
						} catch (InterruptedException ev) {
							Thread.currentThread().interrupt();
						}
					}
					//update button
					this.getActionButton().fire();
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
			if(this.getCyPathFX().getPreviousPossibleCells() != null && this.getCyPathFX().getPreviousPossibleCells().contains(sourceCell)) {
				Pawn pawn = this.getGame().getCurrentPawn();

				// We move circle (pawn of the player) to its new location
				this.getCyPathFX().removeCircleFromCell(this.getCyPathFX().getGPane(), pawn.getPosition().getY() * 2 + 1, pawn.getPosition().getX() * 2 + 1);

				StackPane parentStackPane = (StackPane) sourceCell.getParent();
				int columnIndex = GridPane.getColumnIndex(parentStackPane);
				int rowIndex = GridPane.getRowIndex(parentStackPane);
				
				this.getGame().getBoard().movePawn(this.getGame().getCurrentPawn().getId(), new Point(columnIndex / 2, rowIndex / 2));
				
				this.getCyPathFX().addCircleToCell(this.getCyPathFX().getGPane(), rowIndex, columnIndex, pawn.getColor());
				//the information is transmitted to the terminal
				this.getGame().setIsEndTurn(true);
				//We wait the beginning of the next turn
				CYPathFX.checkEndTurn(this.getGame());

				//update button
				this.getActionButton().fire();
				if (!(this.getActionButton().getText().equals("Place fence"))) {
					this.getActionButton().fire();
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
				if (!this.getCyPathFX().isMoveMode() && event.getButton() == MouseButton.PRIMARY) {
					placeFenceOnBoard(stackPane);
				} else if (this.getCyPathFX().isMoveMode()) {
					// The cell is a shape (rectangle if there is no pawn and circle if there is a pawn)
					if (stackPane.getChildren().get(stackPane.getChildren().size() - 1) instanceof Shape) {
						sourceCell = (Shape) stackPane.getChildren().get(stackPane.getChildren().size() - 1);
					}
					placePawnOnBoard(sourceCell);
				}
			} catch(Exception e) {
				System.err.println(e.getMessage());
				System.exit(-1);
			}
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