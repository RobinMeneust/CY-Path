package control;

import abstraction.Orientation;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presentation.CYPathFX;

public class FenceOrientationControl implements EventHandler<MouseEvent> {
    private CYPathFX cyPathFX;

    public FenceOrientationControl(CYPathFX cyPathFX){
        this.cyPathFX = cyPathFX;
    }
    @Override
    public void handle(MouseEvent event) {
        if(!this.cyPathFX.isMoveMode() && event.getButton() == MouseButton.SECONDARY){
            if(this.cyPathFX.getFenceOrientation() == Orientation.HORIZONTAL) {
                this.cyPathFX.setFenceOrientation(Orientation.VERTICAL);
            } else {
                this.cyPathFX.setFenceOrientation(Orientation.HORIZONTAL);
            }
        }
    }
}
