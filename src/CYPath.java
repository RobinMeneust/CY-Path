import java.util.Scanner;

public class CYPath {
	public static void main(String[] args) {
		try {
			System.out.println("Welcome to CY-Path.\n");
			int nbPlayer = 0;
			Scanner sc = new Scanner(System.in);
			while(nbPlayer != 2 && nbPlayer != 4){
				System.out.println("How many player do you want ? (2 or 4)");
				nbPlayer = Integer.parseInt(sc.nextLine());
			}

			Player[] players= new Player[nbPlayer];
			for (int i = 0; i < nbPlayer; i++){
				players[i] = new Player("Anonymous player" + i);
			}

			Game game = new Game(nbPlayer,players,20, 9, 9);
			game.launch();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}