public class CYPath {
	public static void main(String[] args) {
		try {
			Player[] players= new Player[2];
			players[0] = new Player("Anonymous player 1");
			players[1] = new Player("Anonymous player 2");
			Game game = new Game(2,players,20);
			Board board = new Board(9, 9, game);
			Pawn[] pawns = new Pawn[2];
			pawns[0] = new Pawn(1,Side.TOP,Color.YELLOW,board);
			pawns[1] = new Pawn(2,Side.TOP,Color.BLUE,board);
			
			game.launch();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
