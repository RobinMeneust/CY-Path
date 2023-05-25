package presentation; 

import abstraction.Fence;

/*
 * Importing java classes needed for the CYPath class
 * 
 * Importing classes from the java.util package
 */

import abstraction.GameConsole;
import abstraction.LoadDataFromJSONFile;
import abstraction.Player;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Main class of the CYPath project
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class CYPath {
    
    /**
     * Default constructor
     */

    public CYPath() {}

    /**
     * Scanner used to read the user input in console mode
     */
    public static final Scanner scanner = new Scanner(System.in);


    /**
     * Ask the user if he wants to go to the main screen or exit the program
     * @return True if the game restarts and false if the program exits
     */

    public static boolean askIfRestartGame() {
        String line = "";
        System.out.println("Do you want to restart a new game or exit the program ? ('r' (restart) or 'e' (exit))");
        line = CYPath.scanner.nextLine();
        line = line.toUpperCase();
        if(line.toUpperCase().matches("R(ESTART)?")) {
            return true;
        }
        return false;
    }

    /**
     * Create a new game
     * 
     * @return Game created
     * @throws Exception
     */

    private static GameConsole createNewGame() throws Exception  {
        int nbPlayer = 0;
        HashMap<Integer,Player> playersPawnIndex = new HashMap<Integer,Player>(4);
        GameConsole game = null;
        while(nbPlayer != 2 && nbPlayer != 4){
            System.out.println("How many players do you want ? (2 or 4)");
            String userInput = CYPath.scanner.nextLine();
            try {
                if(userInput.equals("2") || userInput.equals("4")){
                    nbPlayer = Integer.parseInt(userInput);
                }
            } catch (NumberFormatException e) {} // we just continue the while loop
        }
        
        Player[] players = new Player[nbPlayer];
        for (int i = 0; i < nbPlayer; i++){
            players[i] = new Player("Player" + i);
            playersPawnIndex.put(i,players[i]);
        }

        try {
            game = new GameConsole(players,20, 9, 9,playersPawnIndex, 2);
        } catch(Exception e) {
            throw e;
        }

        return game;
    }

    /**
     * Load a game from a file
     * 
     * @return Game loaded
     * @throws Exception
     */
    private static GameConsole loadGame() throws Exception  {
        System.out.println("Provide a file path (for instance data/saves/save.json):");
        String fileName = CYPath.scanner.nextLine();

        GameConsole game = null;
        HashMap<Integer,Player> playersPawnIndex = new HashMap<Integer,Player>(4);
        LoadDataFromJSONFile loadDataObject = new LoadDataFromJSONFile();

        try {
            loadDataObject.load(fileName);
            
            Player[] players = new Player[loadDataObject.getPawns().length];
            for(int i=0; i<players.length; i++) {
                players[i] = new Player("Player"+i);
                playersPawnIndex.put(i, players[i]);
            }

            game = new GameConsole(players, loadDataObject.getMaxNbFences(), loadDataObject.getRows(), loadDataObject.getColumns(), playersPawnIndex, loadDataObject.getPawns(), loadDataObject.getCurrentPawnIndex(), 2);
            for(Fence f : loadDataObject.getListFences()){
                game.getBoard().addFenceToData(f);
            }

            System.out.println("Game successfully loaded");
            return game;
        } catch (Exception e) {
            throw e;
        }
    }

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
        } while(!line.matches("W(INDOW)?") && !line.matches("C(ONSOLE)?"));

        if(line.matches("W(INDOW)?")){
            // Display mode
            CYPathFX.main(args);
        } else {
            // Console mode
            do{
                do {
                    System.out.println("Enter 'l' (load) to load a save file, and 'n' (new) to start a new game");
                    line = CYPath.scanner.nextLine();
                    line = line.toUpperCase();
                } while(!line.matches("L(OAD)?") && !line.matches("N(EW)?"));
                
                // Initialize game
                GameConsole game = null;

                try {
                    if(line.matches("L(OAD)?")) {
                        game = loadGame();
                    } else {
                        game = createNewGame();
                    }
                    game.launch();
                } catch (Exception e) {
                    System.out.println("Game could not be launched\n"+e);
                }
            } while(askIfRestartGame());
        }
        CYPath.scanner.close();
    }
}
