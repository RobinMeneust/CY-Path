package control;

import abstraction.GameFX;
import abstraction.IncorrectPawnIndexException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import presentation.CYPathFX;
import javafx.scene.control.Button;

/**
 * Event that determined the player's choice between "Place a fence" and "Move".
 */
public class ActionButtonControl implements EventHandler<ActionEvent> {
    private Button actionButton;
    private GameFX game;
    private CYPathFX cyPathFX;

    public ActionButtonControl(CYPathFX cyPathFX, Button button, GameFX game){
        this.cyPathFX = cyPathFX;
        this.actionButton = button;
        this.game = game;
    }
    @Override
    public void handle(ActionEvent event) {
        //If the current player have fence
        this.cyPathFX.resetPossibleCells(this.game.getCurrentPlayerIndex()); // Use PAC model to consider the game tracking
        try {
            if (this.actionButton.getText().equals("Move") && this.game.getBoard().getPawn(this.game.getCurrentPlayerIndex()).getAvailableFences() > 0) {
                this.game.setAction("Place fence");
                this.cyPathFX.setMoveMode(false);
            } else {
                this.cyPathFX.showPossibleCells(this.game.getCurrentPlayerIndex());
                this.game.setAction("Move");
                this.cyPathFX.setMoveMode(true);
            }

            //Update fenceCounter
            this.cyPathFX.fenceCounter.setText(this.cyPathFX.game.getBoard().getPawn(this.cyPathFX.game.getCurrentPlayerIndex()).getAvailableFences()+" fence(s) remaining.");
        } catch (IncorrectPawnIndexException e) {
            System.err.println("ERROR: abstraction.Pawn index is incorrect. Check the number of players and the number of pawns and see if they are equals");
            System.exit(-1);
        }
    }
}