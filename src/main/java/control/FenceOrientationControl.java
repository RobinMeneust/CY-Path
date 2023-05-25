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
    /**
     * Graphical interface
     */
    private CYPathFX cyPathFX;
    /**
     * Fence wanted to be rotated
     */
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
        if(!this.getCyPathFX().isMoveMode() && event.getButton() == MouseButton.SECONDARY){
            if(this.getFence().getOrientation() == Orientation.HORIZONTAL) {
                this.getFence().setOrientation(Orientation.VERTICAL);
            } else {
                this.getFence().setOrientation(Orientation.HORIZONTAL);
            }
            // Reset the old dummy fence to show a new one
            HoverBorderControl hoverBorderControl = new HoverBorderControl(this.getCyPathFX(), this.getFence());
            hoverBorderControl.resetHighlightedFences(this.getCyPathFX());
            hoverBorderControl.handle(event);
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
