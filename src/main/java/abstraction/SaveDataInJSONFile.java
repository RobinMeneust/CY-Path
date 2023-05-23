package abstraction;

/*
 * Importing classes from the java.io package
 * 
 * It provides input/output functionality for read and write data operations.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Importing classes from the java.nio package
 * 
 * It provides features for handling non-blocking I/O operations, as well as efficient handling of binary data, such as data transfer between channels and data buffers.
 */

import java.nio.file.Path;
import java.nio.file.Paths;

/*
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
    private Pawn[] listPawns;
    private Fence[] listFences;
    private int currentPawnIndex;

    private String folderPath = "./src/main/resources/data/saves";

    /**
     * Create a constructor that groups the elements that make up a backup file of a
     * part of CY-PATH
     * 
     * @param rows (int) : number of columns of the game board
     * @param columns (int) : number of rows of the game board
     * @param listFences (Fence) : list of fences placed on the board
     * @param maxNbFences (int) : number of fences placed on the board
     * @param listPawns (int) : list of pawns of all players of the CY-PATH party
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

    /**
     * Check if the file name used of the save is a duplicate
     * @param folderPath Path of the save folder
     * @param fileName Name of the file
     * @return True if the save's name file exits already, false otherwise
     */
    private static boolean isFileNameDuplicate(String folderPath, String fileName) {
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

    /**
     * Create the save file
     * @param folderPath Path of the save folder
     * @param fileName Name of the file
     * @return The new empty save file
     * @throws FileNameIsDuplicateException If the file name already exists
     */
    public static File createFile(String folderPath, String fileName) throws FileNameIsDuplicateException {
        File file = null;

        /*
         * Create the folder if it does not exist
         */
        new File(folderPath).mkdirs();
        
        if (!isFileNameDuplicate(folderPath, fileName)) {
            Path folder = Paths.get(folderPath);
            Path filePath = folder.resolve(fileName);
            file = filePath.toFile();
        } else {
            throw new FileNameIsDuplicateException();
        }

        return file;
    }

    /**
     * Replace the content of the file with new content
     * @param filePath Path of the save file
     * @param jsonObject New JSON to be placed into the file
     */
    public static void replaceExistingJSONFile(String filePath, JSONObject jsonObject) {
        File file = new File(filePath);

        if (file.exists()) {
            try {
                //String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonObject.toString());
                fileWriter.flush();
                fileWriter.close();

                System.out.println("The JSON file has been replaced successfully.");
            } catch (IOException e) {
                System.out.println("Error while reading or writing file : " + e.getMessage());
            }
        } else {
            System.out.println("The file does not exist.");
        }
    }

    /**
     * Fill the save file with the information of the current game to be saved and used later
     * @param fileName Path of the save file
     * @throws FileNameIsDuplicateException If the file name already exists
     * @throws IOException If the entry of the user is wrong
     */
    public void save(String fileName) throws FileNameIsDuplicateException, IOException {
        File newFile = null;

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

        try {
            newFile = createFile(this.folderPath, fileName+".json");
            FileWriter file = new FileWriter(newFile);

            file.write(gameObjects.toString());
            file.flush();
            file.close();
        } catch (FileNameIsDuplicateException e) {
            String overFilePath = this.folderPath+"/"+fileName+".json";
            replaceExistingJSONFile(overFilePath, gameObjects);
        } catch (IOException e) {
            throw e;
        }
    }
}
