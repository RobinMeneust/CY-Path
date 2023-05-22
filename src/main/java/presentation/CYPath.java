package presentation; 

/**
 * Importing java classes needed for the CYPath class
 * 
 * Importing classes from the java.util package
 */

import abstraction.GameConsole;
import abstraction.Player;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Main class of the CYPath project
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class CYPath {
    public static final Scanner scanner = new Scanner(System.in);
    /** 
     * Main method
     * Launch the game in console mode or window mode
     * 
     * @param args Command line arguments
     */

    public static void main(String[] args) {
        // Initialize game
        System.out.println("Welcome to CY-Path.\n");
        String line = "";
        do{
            System.out.println("Type 'c' if you want to play in console mode or in a window with visuals 'w'");
            line = CYPath.scanner.nextLine();
            line = line.toUpperCase();
        } while(!line.equals("W") && !line.equals("C"));

        if(line.equals("W")){
            // Display mode
            CYPathFX.main(args);
        } else {
            // Console mode
            // Initialize game
            int nbPlayer = 0;
            while(nbPlayer != 2 && nbPlayer != 4){
                System.out.println("How many players do you want ? (2 or 4)");
                String userInput = CYPath.scanner.nextLine();
                try {
                    if(userInput.equals("2") || userInput.equals("4")){
                        nbPlayer = Integer.parseInt(userInput);
                    }
                } catch (NumberFormatException e) {}
            }
            HashMap<Integer,Player> playersPawnIndex = new HashMap<Integer,Player>(4);

            Player[] players= new Player[nbPlayer];
            for (int i = 0; i < nbPlayer; i++){
                players[i] = new Player("Player" + i);
                playersPawnIndex.put(i,players[i]);
            }
            try {
                GameConsole game = new GameConsole(players,20, 9, 9,playersPawnIndex);
                game.launch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CYPath.scanner.close();
    }
}
