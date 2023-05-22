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
    private ArrayList<Fence> listFences;
    private Pawn[] pawns;
    private int currentPawnIndex;

    /**
     * Create a constructor that retrieves the elements that make up a backup file of a part of CY-PATH
     * 
     * @param rows         (int) : number of columns of the game board
     * @param columns      (int) : number of rows of the game board
     * @param listFences   (Fence) : list of fences placed on the board
     * @param placedFences (int) : number of fences placed on the board
     * @param pawns        (Pawn[]) : list of pawns of all players of the CY-PATH party
     */

    public LoadDataFromJSONFile() {
        this.rows = 0;
        this.columns = 0;
        this.listFences = null;
        this.maxNbFences = 0;
        this.pawns = null;
        this.currentPawnIndex = 0;
    }
    

    public int getRows() {
        return rows;
    }


    public int getColumns() {
        return columns;
    }


    public int getMaxNbFences() {
        return maxNbFences;
    }


    public ArrayList<Fence> getListFences() {
        return listFences;
    }

    public Pawn[] getPawns() {
        return pawns;
    }

    public int getCurrentPawnIndex() {
        return currentPawnIndex;
    }

    public static File loadFile(String filePath) throws FileNameNotExistException {
        File file = new File(filePath);
        if(file != null && file.isFile()) {
            return file;
        } else {
            throw new FileNameNotExistException();
        }
    }

    /**
     * Procedure to load a game from a file save in the backup folder
    *
    * @param fileName (String)
    * @throws FileNameNotExistException
     * @throws IOException
     * @throws ParseException
    */

    public void load(String filePath) throws FileNameNotExistException, IOException, ParseException {
        File savedFile = null;
        
        try {
            savedFile = loadFile(filePath);
        } catch (FileNameNotExistException e) {
            throw e;
        }

        try {
            JSONParser parser = new JSONParser();

            JSONObject gameObjects = (JSONObject) parser.parse(new FileReader(savedFile.getAbsolutePath()));
            this.rows = ((Number) gameObjects.get("rows")).intValue();
            this.columns = ((Number) gameObjects.get("columns")).intValue();
            this.maxNbFences = ((Number) gameObjects.get("maxNbFences")).intValue();
            this.currentPawnIndex = ((Number) gameObjects.get("currentPawnIndex")).intValue();
            JSONArray listFences = (JSONArray)gameObjects.get("listFences");
            Iterator<?> iteratorFence = listFences.iterator();
            this.listFences = new ArrayList<Fence>(listFences.size());

            while(iteratorFence.hasNext()) {
                JSONObject fenceJSON = (JSONObject) iteratorFence.next();
                String orientation = (String) fenceJSON.get("orientation").toString();
                JSONObject start = (JSONObject) fenceJSON.get("start");
                int startX = ((Number) start.get("x")).intValue();
                int startY = ((Number) start.get("y")).intValue();
                JSONObject end = (JSONObject) fenceJSON.get("end");
                int endX = ((Number) end.get("x")).intValue();
                int endY = ((Number) end.get("y")).intValue();
                this.listFences.add(new Fence(Orientation.valueOf(orientation), new Point(startX, startY), new Point(endX, endY)));
            }

            JSONArray listPawns = (JSONArray)gameObjects.get("listPawns");
            Iterator<?> iteratorPawns = listPawns.iterator();
            this.pawns = new Pawn[listPawns.size()];
            int i = 0;
            while(iteratorPawns.hasNext()) {
                JSONObject pawnJSON = (JSONObject) iteratorPawns.next();
                int id = ((Number) pawnJSON.get("id")).intValue();
                String startingSide = (String) pawnJSON.get("startingSide").toString();
                String color = (String) pawnJSON.get("color").toString();
                JSONObject position = (JSONObject) pawnJSON.get("pos");
                int positionX = ((Number) position.get("x")).intValue();
                int positionY = ((Number) position.get("y")).intValue();
                int nbRemainingFences = ((Number) pawnJSON.get("nbRemainingFences")).intValue();
                this.pawns[i] = new Pawn(id, Side.valueOf(startingSide), ColorPawn.valueOf(color), new Point(positionX, positionY), nbRemainingFences);
                i++;
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (ParseException e) {
            throw e;
        }
    }
}
