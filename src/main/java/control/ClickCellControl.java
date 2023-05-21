package control;

import abstraction.Fence;
import abstraction.GameAbstract;
import abstraction.GameFX;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import presentation.CYPathFX;

public class ClickCellControl implements EventHandler<MouseEvent> {
	private CYPathFX cyPathFX;
	private Fence fence;
	private GameAbstract game;
	private Button actionButton;

	public ClickCellControl(CYPathFX cyPathFX, Fence fence, GameAbstract game, Button actionButton){
		this.cyPathFX = cyPathFX;
		this.fence = fence;
		this.game = game;
		this.actionButton = actionButton;
	}
	@Override
	public void handle(MouseEvent mouseEvent) {
		if(mouseEvent.getButton() == MouseButton.PRIMARY){
			ClickAddBorderControl clickAddBorderControl = new ClickAddBorderControl(this.cyPathFX, (GameFX) this.game, this.actionButton, this.fence);
			clickAddBorderControl.handle(mouseEvent);
		} else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
			FenceOrientationControl fenceOrientationControl = new FenceOrientationControl(this.cyPathFX, this.fence);
			fenceOrientationControl.handle(mouseEvent);
		}
	}
}
