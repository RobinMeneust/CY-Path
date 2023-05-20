
/**
 * Importing java classes needed for the SaveDataInJSONFile class
 * 
 * Importing classes from the java.util package
 */
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Importing classes from the java.io package
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Importing classes from the java.nio package
 */
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Importing classes from the arg.json package
 */
import org.json.JSONObject;

/**
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault,
 *         KUSMIDER David, MENEUST Robin
 * 
 *         This class stores the elements of the current part in a .json file
 */
public class SaveDataInJSONFile {

    /**
     * State the SaveGame's class attributes
     */
    private int rows;
    private int columns;
    private Fence[] listFences;
    private int placedFences;
    private Pawn[] listPawns;

    private String folderPath = "./src/main/resources/data/";
    private String fileName;

    /**
     * Create a constructor that groups the elements that make up a backup file of a
     * part of CY-PATH
     * 
     * @param rows         (int) number of columns of the game board
     * @param columns      (int) number of rows of the game board
     * @param listFences   (Fence) list of fences placed on the board
     * @param placedFences (int) number of fences placed on the board
     * @param listPawns    (int) list of pawns of all players of the CY-PATH party
     */
    public SaveDataInJSONFile(int rows, int columns, Fence[] listFences, int placedFences, Pawn[] listPawns) {
        this.rows = rows;
        this.columns = columns;
        this.listFences = listFences;
        this.placedFences = placedFences;
        this.listPawns = listPawns;
    }

    private static boolean isFileNameDuplicate(String folderPath, String fileName) {
        File folder = new File(folderPath);
        /*
         * if (!folder.isDirectory()) {
         * throw new
         * IllegalArgumentException("The specified path is not a valid folder!");
         * }
         */

        File[] filesInFolder = folder.listFiles();
        if (filesInFolder != null) {
            for (File file : filesInFolder) {
                if (/* file.isFile() && */ file.getName().equals(fileName)) {
                    return true; // File name already exists
                    /* It will be better to do : throw new FileAlreadyExistsException */
                }
            }
        }

        return false; // File name is unique
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

            scanner.close();

        } catch (NoSuchElementException elm) {
            System.out.println("Error : the name of the file can't be null : " + elm.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void save(SaveDataInJSONFile game, String filePath) {

        JSONObject gameObjects = new JSONObject();
        gameObjects.put("rows", rows);
        gameObjects.put("columns", columns);
        gameObjects.put("placedFences", placedFences);

        CustomJSONArray gameElementsListFences = new CustomJSONArray();

        for (int i = 1; i <= listFences.length; i++) {
            gameElementsListFences.addCustomArrayPoint("StartPawnFence" + i, listFences[i - 1].getStart());
            gameElementsListFences.addCustomArrayPoint("EndPawnFence" + i, listFences[i - 1].getEnd());
        }

        gameObjects.put("listFences", gameElementsListFences);

        CustomJSONArray gameElementsListPawns = new CustomJSONArray();

        for (int i = 1; i <= listPawns.length; i++) {
            gameElementsListPawns.addCustomArrayPoint("PositionPawnPlayer" + i, listPawns[i - 1].getPosition());
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
