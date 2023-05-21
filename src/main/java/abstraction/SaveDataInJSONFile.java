package abstraction;

/**
 * Importing java classes needed for the SaveDataInJSONFile class
 * 
 * Importing classes from the java.util package
 */

import java.util.Scanner;
import java.util.NoSuchElementException;

/** 
 * Importing classes from the java.io package
 * 
 * It provides input/output functionality for read and write data operations.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    private String folderPath = "./src/main/resources/data/";
    private String fileName;

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

    public void createFile(String folderPath) {

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the name of the backup file: ");
            fileName = scanner.nextLine();

            boolean isFileNameDuplicateTest = isFileNameDuplicate(folderPath, fileName);

            if (!isFileNameDuplicateTest) {
                Path folder = Paths.get(folderPath);
                Path filePath = folder.resolve(fileName);
            }

        } catch (NoSuchElementException elm) {
            System.out.println("Error : the name of the file can't be null : " + elm.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void save(String filePath) {

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

        try (FileWriter file = new FileWriter(filePath + ".json")) {
            file.write(gameObjects.toString());
            file.flush();
            file.close();
            System.out.println("The backup was successfully performed !");
        } catch (IOException e) {
            System.out.println("An error occurred while saving : " + e.getMessage());
        }
    }
}
