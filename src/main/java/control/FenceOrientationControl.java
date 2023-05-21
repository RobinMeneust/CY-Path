package control;

import abstraction.Fence;
import abstraction.Orientation;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presentation.CYPathFX;

public class FenceOrientationControl implements EventHandler<MouseEvent> {
    private CYPathFX cyPathFX;
    private Fence fence;

    public FenceOrientationControl(CYPathFX cyPathFX, Fence fence){
        this.cyPathFX = cyPathFX;
        this.fence = fence;
    }
    @Override
    public void handle(MouseEvent event) {
        if(!this.cyPathFX.isMoveMode() && event.getButton() == MouseButton.SECONDARY){
            if(this.fence.getOrientation() == Orientation.HORIZONTAL) {
                this.fence.setOrientation(Orientation.VERTICAL);
            } else {
                this.fence.setOrientation(Orientation.HORIZONTAL);
            }
        }
    }
}
