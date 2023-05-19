import java.util.Scanner;

public class CYPath {
    public static void main(String[] args) {
        // Initialize game
        System.out.println("Welcome to CY-Path.\n");
        char mode = '\0';
        Scanner sc = new Scanner(System.in);
        do{
            System.out.println("Type 'c' if you want to play in console mode or in a window with visuals 'w'");
            String line = sc.nextLine();
            mode = line.charAt(0);
        } while(mode != 'w' && mode != 'c');

        if(mode == 'w'){
            // Display mode
            CYPathFX.main(args);
        } else {
            // Console mode
            // Initialize game
            System.out.println("Welcome to CY-Path.\n");
            int nbPlayer = 0;
            while(nbPlayer != 2 && nbPlayer != 4){
                System.out.println("How many players do you want ? (2 or 4)");
                nbPlayer = Integer.parseInt(sc.nextLine());
            }

            Player[] players= new Player[nbPlayer];
            for (int i = 0; i < nbPlayer; i++){
                players[i] = new Player("Anonymous player" + i);
            }
            try {
                GameConsole game = new GameConsole(nbPlayer,players,20, 9, 9);
                game.launch();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
