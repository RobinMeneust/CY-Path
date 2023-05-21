package abstraction;


/** 
 * Importing classes from the java.io package
 * 
 * It provides input/output functionality for read and write data operations.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;

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
     */

    public SaveDataInJSONFile(int rows, int columns, Fence[] listFences, int placedFences, Pawn[] listPawns) {
        this.rows = rows;
        this.columns = columns;
        this.listFences = listFences;
        this.maxNbFences = placedFences;
        this.listPawns = listPawns;
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

    public static File createFile(String folderPath, String fileName) throws FileNameIsDuplicate {
        File file = null;

        new File(folderPath).mkdirs(); // Create the folder if it doesn't exist
        if (!isFileNameDuplicate(folderPath, fileName)) {
            Path folder = Paths.get(folderPath);
            Path filePath = folder.resolve(fileName);
            file = filePath.toFile();
        } else {
            throw new FileNameIsDuplicate();
        }
        return file;
    }

    public boolean save(String fileName) {
        File newFile = null;
        try {
            newFile = createFile(this.folderPath, fileName+".json");
        } catch (FileNameIsDuplicate e) {
            return false;
        }

        JSONObject gameObjects = new JSONObject();
        gameObjects.put("rows", rows);
        gameObjects.put("columns", columns);
        gameObjects.put("maxNbFences", maxNbFences);

        
        CustomJSONArray gameElementsListFences = new CustomJSONArray();
        
        for (int i = 0; i < listFences.length; i++) {
            JSONObject fenceObject = new JSONObject();
            fenceObject.put("start", listFences[i].getStart());
            fenceObject.put("end", listFences[i].getEnd());
            fenceObject.put("orientation", listFences[i].getOrientation());
            gameElementsListFences.put(fenceObject);
        }

        gameObjects.put("listFences", gameElementsListFences);

        CustomJSONArray gameElementsListPawns = new CustomJSONArray();

        for (int i = 0; i < listPawns.length; i++) {
            JSONObject pawnObject = new JSONObject();

            pawnObject.put("color",listPawns[i].getColor().toString());
            JSONObject pointObject = new JSONObject();
            pointObject.put("x", listPawns[i].getPosition().getX());
            pointObject.put("y", listPawns[i].getPosition().getY());
            pawnObject.put("pos",pointObject);
            pawnObject.put("nbRemainingFences",listPawns[i].getAvailableFences());

            gameElementsListPawns.put(listPawns[i].getId(),pawnObject);
        }

        gameObjects.put("listPawns", gameElementsListPawns);

        try (FileWriter file = new FileWriter(newFile)) {
            file.write(gameObjects.toString());
            file.flush();
            file.close();
            System.out.println("The backup was successfully performed !");
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred while saving : " + e.getMessage());
        }
        return false;
    }
}
