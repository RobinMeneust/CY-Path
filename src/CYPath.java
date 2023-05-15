public class CYPath {
	public static void main(String[] args) {
		try {
			Player[] players= new Player[2];
			players[0] = new Player("Anonymous player 1");
			players[1] = new Player("Anonymous player 2");
			Game game = new Game(2,players,20, 9, 9);			
			game.launch();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
