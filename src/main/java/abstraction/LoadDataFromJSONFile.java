package abstraction;

/*
 * Importing java classes needed for the SaveDataInJSONFile class
 * 
 * Importing classes from the java.util package
 */

import java.util.ArrayList;
import java.util.Iterator;

/*
 * Importing classes from the java.io package
 * 
 * It provides input/output functionality for read and write data operations.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


/*
 * Importing classes from the org.json.simple package
*/

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 

/**
 * This class stores the elements of the current part in a .json file
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class LoadDataFromJSONFile {

    /**
     * Number of rows
     */
    private int rows;
    /**
     * Number of columns
     */
    private int columns;
    /**
     * Maximal number of fence available
     */
    private int maxNbFences;
    /**
     * List of fences to placed on the board
     */
    private ArrayList<Fence> listFences;
    /**
     * Table of pawns to placed on the board
     */
    private Pawn[] pawns;
    /**
     * Index of current pawn playing
     */
    private int currentPawnIndex;

    /**
     * Create a constructor that retrieves the elements that make up a backup file of a part of CY-PATH
     */

    public LoadDataFromJSONFile() {
        this.rows = 0;
        this.columns = 0;
        this.listFences = null;
        this.maxNbFences = 0;
        this.pawns = null;
        this.currentPawnIndex = 0;
    }

    /**
     * Get the number of rows
     *
     * @return Number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the number of columns
     *
     * @return Number of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get the number of maximum fence
     *
     * @return Number of maximum fence
     */
    public int getMaxNbFences() {
        return maxNbFences;
    }


    /**
     * Get the list of every fence placed
     *
     * @return list of every fence placed
     */
    public ArrayList<Fence> getListFences() {
        return listFences;
    }

    /**
     * Get the table of pawns
     *
     * @return Table of pawns
     */
    public Pawn[] getPawns() {
        return pawns;
    }

    /**
     * Get the current index of the pawn playing
     *
     * @return Current index of the pawn playing
     */
    public int getCurrentPawnIndex() {
        return currentPawnIndex;
    }


    /**
     * Get the file of the game from a filepath
     * @param filePath Filepath of the file to load
     * @return File wanted to be loaded
     * @throws FileNameNotExistException If the file name entered doesn't exist
     */
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
     * @param filePath Filepath of the file to load
     * @throws FileNameNotExistException If the file name entered doesn't exist
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
