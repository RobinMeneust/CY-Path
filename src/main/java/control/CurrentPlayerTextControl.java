package control;

import abstraction.GameAbstract;
import javafx.scene.text.Text;

import java.util.Observable;
import java.util.Observer;

public class CurrentPlayerTextControl implements Observer {
	private Text currentPlayer;
	private GameAbstract game;

	public CurrentPlayerTextControl(GameAbstract game, Text text){
		this.game = game;
		this.currentPlayer = text;
	}
	@Override
	public void update(Observable observable, Object o) {
		currentPlayer.setText(this.game.getCurrentPlayer().getUsername()+"'s turn");
	}
}
