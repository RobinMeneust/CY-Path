package abstraction;

/**
 * Importing java classes needed for the SaveDataInJSONFile class
 * 
 * Importing classes from the java.util package
 */

import java.util.ArrayList;
import java.util.Iterator;

/** 
 * Importing classes from the java.io package
 * 
 * It provides input/output functionality for read and write data operations.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Importing classes from the java.nio package
 * 
 * It provides features for handling non-blocking I/O operations, as well as efficient handling of binary data, such as data transfer between channels and data buffers.
 */

import java.nio.file.Path;
import java.nio.file.Paths;
//import java.nio.file.NoSuchFileException;

/**
 * Importing classes from the org.json.simple package
*/

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 

/**
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 * 
 *         This class stores the elements of the current part in a .json file
 */

public class LoadDataFromJSONFile {
    /**
     * State the SaveData's class attributes
     */

    private int rows;
    private int columns;
    private int maxNbFences;
    private Fence[] listFences;
    private Pawn[] listPawns;

    private String folderPath = "./src/main/resources/data/saves"; // TODO: Needs to be changed so that it works in the .jar

    /**
     * Create a constructor that retrieves the elements that make up a backup file of a part of CY-PATH
     * 
     * @param rows         (int) : number of columns of the game board
     * @param columns      (int) : number of rows of the game board
     * @param listFences   (Fence) : list of fences placed on the board
     * @param placedFences (int) : number of fences placed on the board
     * @param listPawns    (int) : list of pawns of all players of the CY-PATH party
     */

    public LoadDataFromJSONFile(int rows, int columns, Fence[] listFences, int maxNbFences, Pawn[] listPawns) {
        this.rows = rows;
        this.columns = columns;
        this.listFences = listFences;
        this.maxNbFences = maxNbFences;
        this.listPawns = listPawns;
    }

    public static boolean isFileNameExist(String folderPath, String fileName) {
        File folder = new File(folderPath);
        File[] filesInFolder = folder.listFiles();

        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                if (file.getName().equals(fileName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static File loadFile(String folderPath, String fileName) throws FileNameNotExistException {
        File file = null;

        if(!isFileNameExist(folderPath, fileName)) {
            Path folder = Paths.get(folderPath);
            Path filePath = folder.resolve(fileName);
            file = filePath.toFile();
        } else {
            throw new FileNameNotExistException();
        }
        return file;
    }

    /**
     * Procedure to load a game from a file save in the backup folder
    *
    * @param fileName (String)
    * @throws FileNameNotExistException
    */

    public void load(String fileName, boolean isGameFX, Player[] players) throws FileNameNotExistException, InvalidNumberOfPlayersException {
        File savedFile = null;
        GameAbstract loadedGame = null;
                
        try {
            savedFile = loadFile(this.folderPath, fileName);
        } catch (FileNameNotExistException e) {
            throw e;
        }

        try {
            JSONParser parser = new JSONParser();

            JSONObject gameObjects = (JSONObject) parser.parse(new FileReader(folderPath+fileName+".json"));

            String json = gameObjects.toJSONString();

            rows = (int) gameObjects.get("rows");
            columns = (int) gameObjects.get("columns");
            maxNbFences = (int) gameObjects.get("maxNbFences");

            JSONArray listFences = (JSONArray)gameObjects.get("listFences");
            Iterator iteratorFence = listFences.iterator();
            ArrayList<Fence> arrayListFence = new ArrayList<Fence>(listFences.size());
            while(iteratorFence.hasNext()) {
                JSONObject fenceJSON = (JSONObject) iteratorFence.next();
                String orientation = (String) fenceJSON.get("orientation");
                JSONObject start = (JSONObject) fenceJSON.get("start");
                int startX = (int) start.get("x");
                int startY = (int) start.get("y");
                JSONObject end = (JSONObject) fenceJSON.get("start");
                int endX = (int) end.get("x");
                int endY = (int) end.get("y");
                arrayListFence.add(new Fence(Orientation.valueOf(orientation), new Point(startX, startY), new Point(endX, endY)));
            }

            JSONArray listPawns = (JSONArray)gameObjects.get("listPawns");
            Iterator iteratorPawns = listPawns.iterator();
            ArrayList<Pawn> arrayListPawn = new ArrayList<Pawn>(listPawns.size());
            while(iteratorPawns.hasNext()) {
                JSONObject pawnJSON = (JSONObject) iteratorPawns.next();
                int id = (int) pawnJSON.get("id");
                String startingSide = (String) pawnJSON.get("startingSide");
                String color = (String) pawnJSON.get("color");
                JSONObject position = (JSONObject) pawnJSON.get("pos");
                int positionX = (int) position.get("x");
                int positionY = (int) position.get("y");
                int availableFences = (int) pawnJSON.get("availableFences");
                arrayListPawn.add(new Pawn(id, Side.valueOf(startingSide), ColorPawn.valueOf(color), new Point(positionX, positionY), availableFences));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            if(isGameFX) {
                loadedGame = new GameFX(players, maxNbFences, rows, columns);
            } else {
                loadedGame = new GameConsole(players, maxNbFences, rows, columns);
            }
        } catch (InvalidNumberOfPlayersException e) {
            throw e;
        }
    }
}
