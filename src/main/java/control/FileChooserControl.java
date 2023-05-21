package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChooserControl implements EventHandler<ActionEvent> {

	private String action;
	private Stage stage;

	/**
	 * Open a file chooser to save or load a game.
	 *
	 * @param stage Main stage
	 * @param action "Load" or "Save" to dertermine what the file chooser has to do.
	 */
	public FileChooserControl(Stage stage, String action){
		this.action = action;
		this.stage = stage;
	};

	@Override
	public void handle(ActionEvent actionEvent) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select Some Files");

		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		if(action.equals("Load")) {
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
			File file = fileChooser.showSaveDialog(stage);
			if(file != null) {
				// TODO
				//loadGame(file);
			}
		} else if (action.equals("Save")) {
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
			File file = fileChooser.showSaveDialog(stage);
			if(file != null) {
				// TODO
				//saveGame(file);
			}
		}
	}

}
