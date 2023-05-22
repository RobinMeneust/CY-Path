package abstraction;

/** 
 * Importing classes from the java.io package
 * 
 * It provides input/output functionality for read and write data operations.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.nio.file.InvalidPathException;

/**
 * Importing classes from the java.nio package
 * 
 * It provides features for handling non-blocking I/O operations, as well as efficient handling of binary data, such as data transfer between channels and data buffers.
 */

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Importing classes from the org.json package
 */

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class stores the elements of the current part in a .json file
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class SaveDataInJSONFile {
    /**
     * State the SaveGame's class attributes
     */

    private int rows;
    private int columns;
    private int maxNbFences;
    private Fence[] listFences;
    private Pawn[] listPawns;
    private int currentPawnIndex;

    private String folderPath = "./src/main/resources/data/saves"; // TODO: Needs to be changed so that it works in the .jar

    /**
     * Create a constructor that groups the elements that make up a backup file of a
     * part of CY-PATH
     * 
     * @param rows         (int) : number of columns of the game board
     * @param columns      (int) : number of rows of the game board
     * @param listFences   (Fence) : list of fences placed on the board
     * @param maxNbFences (int) : number of fences placed on the board
     * @param listPawns    (int) : list of pawns of all players of the CY-PATH party
     * @param currentPawnIndex (int) : index of the current player (that will play in the first round)
     */

    public SaveDataInJSONFile(int rows, int columns, Fence[] listFences, int maxNbFences, Pawn[] listPawns, int currentPawnIndex) {
        this.rows = rows;
        this.columns = columns;
        this.listFences = listFences;
        this.maxNbFences = maxNbFences;
        this.listPawns = listPawns;
        this.currentPawnIndex = currentPawnIndex;
    }

    private static boolean isFileNameDuplicate(String folderPath, String fileName) {
        File folder = new File(folderPath);
        File[] filesInFolder = folder.listFiles();

        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                if (file.getName().equals(fileName)) {
                    return true;
                    /* It will be better to do : throw new FileAlreadyExistsException */
                }
            }
        }

        return false;
    }

    public static File createFile(String folderPath, String fileName) throws FileNameIsDuplicateException {
        File file = null;

        new File(folderPath).mkdirs(); // Create the folder if it doesn't exist
        if (!isFileNameDuplicate(folderPath, fileName)) {
            Path folder = Paths.get(folderPath);
            Path filePath = folder.resolve(fileName);
            file = filePath.toFile();
        } else {
            throw new FileNameIsDuplicateException();
        }
        return file;
    }

    public void save(String fileName) throws FileNameIsDuplicateException, IOException {
        File newFile = null;
        try {
            newFile = createFile(this.folderPath, fileName+".json");
        } catch (FileNameIsDuplicateException e) {
            throw e;
        }

        JSONObject gameObjects = new JSONObject();
        gameObjects.put("rows", rows);
        gameObjects.put("columns", columns);
        gameObjects.put("maxNbFences", maxNbFences);
        gameObjects.put("currentPawnIndex", currentPawnIndex);

        
        JSONArray gameElementsListFences = new JSONArray();
        
        for (int i = 0; i < listFences.length; i++) {
            JSONObject fenceObject = new JSONObject();

            JSONObject pointObject1 = new JSONObject();
            pointObject1.put("x", listFences[i].getStart().getX());
            pointObject1.put("y", listFences[i].getStart().getY());
            fenceObject.put("start", pointObject1);
            JSONObject pointObject2 = new JSONObject();
            pointObject2.put("x", listFences[i].getEnd().getX());
            pointObject2.put("y", listFences[i].getEnd().getY());
            fenceObject.put("end", pointObject2);

            fenceObject.put("orientation", listFences[i].getOrientation());
            
            gameElementsListFences.put(fenceObject);
        }

        gameObjects.put("listFences", gameElementsListFences);

        JSONArray gameElementsListPawns = new JSONArray();

        for (int i = 0; i < listPawns.length; i++) {
            JSONObject pawnObject = new JSONObject();

            pawnObject.put("id",listPawns[i].getId());
            pawnObject.put("startingSide",listPawns[i].getStartingSide());
            pawnObject.put("color",listPawns[i].getColor().toString());
            JSONObject pointObject = new JSONObject();
            pointObject.put("x", listPawns[i].getPosition().getX());
            pointObject.put("y", listPawns[i].getPosition().getY());
            pawnObject.put("pos",pointObject);
            pawnObject.put("nbRemainingFences",listPawns[i].getAvailableFences());

            gameElementsListPawns.put(pawnObject);
        }

        gameObjects.put("listPawns", gameElementsListPawns);

        try (FileWriter file = new FileWriter(newFile)) {
            file.write(gameObjects.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            throw e;
        }
    }
}
