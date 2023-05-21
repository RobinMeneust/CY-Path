package control;

import abstraction.Fence;
import abstraction.Orientation;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presentation.CYPathFX;

/**
 * Control of orientation of the fence.
 *
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */
public class FenceOrientationControl implements EventHandler<MouseEvent> {
    private CYPathFX cyPathFX;
    private Fence fence;

    /**
     * Default constructor
     * @param cyPathFX  Reference to the JavaFX interface
     * @param fence     Reference to the barrier whose orientation is to be changed
     */
    public FenceOrientationControl(CYPathFX cyPathFX, Fence fence){
        this.cyPathFX = cyPathFX;
        this.fence = fence;
    }

    /**
     * Handler to change the orientation of the placed to be placed.
     * @param event Event of the mouse it's clicked
     */
    @Override
    public void handle(MouseEvent event) {
        // Check if the right click of the mouse is pressed
        if(!this.cyPathFX.isMoveMode() && event.getButton() == MouseButton.SECONDARY){
            if(this.fence.getOrientation() == Orientation.HORIZONTAL) {
                this.fence.setOrientation(Orientation.VERTICAL);
            } else {
                this.fence.setOrientation(Orientation.HORIZONTAL);
            }
            // Reset the old dummy fence to show a new one
            HoverBorderControl hoverBorderControl = new HoverBorderControl(this.cyPathFX, this.fence);
            hoverBorderControl.resetHighlightedFences(this.cyPathFX);
            hoverBorderControl.handle(event);
        }
    }
}
