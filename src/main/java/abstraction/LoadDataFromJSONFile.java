package abstraction;

/*
 * Importing java classes needed for the SaveDataInJSONFile class
 * 
 * Importing classes from the java.util package
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Importing classes from the java.io package
 * 
 * It provides input/output functionality for read and write data operations.
 */

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	 * Checks if the backup file name entered by the user is correct
	 * 
	 * @param fileName Name of the file
	 * @return True if the save's name file contains only letters, numbers, the _, the - and the . else false
	 */

	public static boolean isFileNameValid(String fileName) {
		// Using a regular expression to check the characters of the file name.
		String pattern = "^[a-zA-Z0-9_.-]+$";
		return Pattern.matches(pattern, fileName);
	}
 
	/**
	 * It provides the name of the load file
	 * 
	 * @param filePath The path of the save file you want loaded
	 * @return The save's name file without extension or the empty string if the save's name file is incorrect
	 */
 
	public static String extractFileNameWithoutExtension(String filePath) {
		Path path = Paths.get(filePath);
		String fileName = path.getFileName().toString();
		int dotIndex = fileName.lastIndexOf(".json");
		if (dotIndex > 0) {
			return fileName.substring(0, dotIndex);
		} else {
			return "";
		}
	}

	/**
	 * Get an object from a JSON object
	 * 
	 * @param obj JSON object
	 * @param key Key of the value got from obj
	 * @return JSON object at the given key
	 * @throws SaveFileFormatInvalidException If the format of the file is incorrect
	 */

	public static JSONObject getJSONObjectFromJSON(JSONObject obj, String key) throws SaveFileFormatInvalidException {
		if(obj.get(key) instanceof JSONObject)
			return (JSONObject) obj.get(key);
		throw new SaveFileFormatInvalidException();
	}

	/**
	 * Get an object from a JSON object and cast it to an int if it is one
	 * 
	 * @param obj JSON object
	 * @param key Key of the value got from obj
	 * @param minValue Minimum value of the integer associated to the key
	 * @return Value in the JSON object at the given key
	 * @throws SaveFileFormatInvalidException If the format of the file is incorrect
	 */

	public static int getIntegerFromJSON(JSONObject obj, String key, int minValue) throws SaveFileFormatInvalidException {
		if(obj.get(key) instanceof Number) {
			int value = ((Number) obj.get(key)).intValue();
			if(value >= minValue)
				return value;
		}
		throw new SaveFileFormatInvalidException();
	}

	/**
	 * Get an object from a JSON object and cast it to a String if it is one
	 * 
	 * @param obj JSON object
	 * @param key Key of the value got from obj
	 * @return Value in the JSON object at the given key
	 * @throws SaveFileFormatInvalidException If the format of the file is incorrect
	 */

	public static String getStringFromJSON(JSONObject obj, String key) throws SaveFileFormatInvalidException {
		if(obj.get(key) instanceof String)
			return obj.get(key).toString();
		throw new SaveFileFormatInvalidException();
	}

	/**
	 * Procedure to load a game from a file save in the backup folder
	 *
	 * @param filePath Filepath of the file to load
	 * @throws FileNotFoundException If the file name entered doesn't exist
	 * @throws IOException If an I/O error occurs while reading the file
	 * @throws ParseException If an error occurs while parsing the JSON data
	 * @throws FileNameException If the file name entered is incorrect
	 * @throws SaveFileFormatInvalidException If the file loaded is incorrect
	 */
	
	public void load(String filePath) throws FileNotFoundException, IOException, ParseException, FileNameException, SaveFileFormatInvalidException  {			
		File file = new File(filePath);

		if (!file.exists()) {
			throw new FileNotFoundException("The file doesnt' exist");
		}

		if(!isFileNameValid(extractFileNameWithoutExtension(filePath))) {    
			throw new FileNameException();
		}

		try {
			JSONParser parser = new JSONParser();

			JSONObject gameObjects = (JSONObject) parser.parse(new FileReader(filePath));

			this.setRows(getIntegerFromJSON(gameObjects,"rows", 2));
			this.setColumns(getIntegerFromJSON(gameObjects,"columns", 2));
			this.setMaxNbFences(getIntegerFromJSON(gameObjects,"maxNbFences",0));
			this.setCurrentPawnIndex(getIntegerFromJSON(gameObjects,"currentPawnIndex",0));

			Object FencesObj = gameObjects.get("listFences");
			if((FencesObj != null) && (FencesObj instanceof JSONArray)) { 
				JSONArray listFencesJSON = (JSONArray) FencesObj;
				Iterator<?> iteratorFence = listFencesJSON.iterator();
				this.setListFences(new ArrayList<Fence>());
				while(iteratorFence.hasNext()) {
					Object iterFence = iteratorFence.next();
					if (iterFence instanceof JSONObject) {
						JSONObject fenceJSON = (JSONObject) iterFence;
						String orientation = getStringFromJSON(fenceJSON, "orientation");

						Point start = new Point(0,0);

						JSONObject startJSON = getJSONObjectFromJSON(fenceJSON, "start");
						start.setX(getIntegerFromJSON(startJSON, "x", 0));
						start.setY(getIntegerFromJSON(startJSON, "y", 0));                

						Point end = new Point(0,0);

						JSONObject endJSON = getJSONObjectFromJSON(fenceJSON, "end");
						end.setX(getIntegerFromJSON(endJSON, "x", 0));
						end.setY(getIntegerFromJSON(endJSON, "y", 0));
						
						try {
							this.getListFences().add(new Fence(Orientation.valueOf(orientation), start, end));
						} catch (Exception e) {
							// No value found for orientation in the enum Orientation
							throw new SaveFileFormatInvalidException();
						}
					} else {
						throw new SaveFileFormatInvalidException();
					}
				}
			} else {
				throw new SaveFileFormatInvalidException();
			}

			Object PawnsObj = gameObjects.get("listPawns");
			if((PawnsObj != null) && (PawnsObj instanceof JSONArray)) { 
				JSONArray listPawnsJSON = (JSONArray) PawnsObj;
				Iterator<?> iteratorPawn = listPawnsJSON.iterator();
				int size = listPawnsJSON.size();
				this.setPawns(new Pawn[size]);
				int i = 0;
				while(iteratorPawn.hasNext()) {
					Object iterPawn = iteratorPawn.next();
					if (iterPawn instanceof JSONObject) {
						JSONObject pawnJSON = (JSONObject) iterPawn;

						int id = getIntegerFromJSON(pawnJSON, "id", 0);
						String startingSide = getStringFromJSON(pawnJSON, "startingSide");
						String color = getStringFromJSON(pawnJSON, "color");
						JSONObject posJSON = getJSONObjectFromJSON(pawnJSON, "pos");
						
						Point pos = new Point(0,0);
						pos.setX(getIntegerFromJSON(posJSON, "x", 0));
						pos.setY(getIntegerFromJSON(posJSON, "y", 0));
						
						int nbRemainingFences = getIntegerFromJSON(pawnJSON, "nbRemainingFences", 0);

						this.setPawn(i,new Pawn(id, Side.valueOf(startingSide), ColorPawn.valueOf(color), pos, nbRemainingFences));

						i++;
					} else {
						throw new SaveFileFormatInvalidException();
					}
				}
			} else {
				throw new SaveFileFormatInvalidException();
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Set the number of rows
	 * @param rows Number of rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Set the number of columns
	 * @param columns Number of columns
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Set the maximal number of fences
	 * @param maxNbFences Maximal number of fences
	 */
	public void setMaxNbFences(int maxNbFences) {
		this.maxNbFences = maxNbFences;
	}

	/**
	 * Set the list of fences
	 * @param listFences List of fences
	 */
	public void setListFences(ArrayList<Fence> listFences) {
		this.listFences = listFences;
	}

	/**
	 * Set the index of the pawn being played
	 * @param currentPawnIndex Index of the pawn being played
	 */
	public void setCurrentPawnIndex(int currentPawnIndex) {
		this.currentPawnIndex = currentPawnIndex;
	}

	/**
	 * Set the table of pawns
	 * @param pawns Table of pawns
	 */
	public void setPawns(Pawn[] pawns) {
		this.pawns = pawns;
	}

	/**
	 * Get a pawn from the tables of pawns
	 * @param index Index of the pawn
	 * @return Pawn selected by the index
	 */
	public Pawn getPawn(int index){
		return this.pawns[index];
	}

	/**
	 * Set the pawn in the table of pawns with its index
	 * @param index Index of the pawn wanted to be set
	 * @param pawn Pawn wanting to be placed in the table
	 */
	public void setPawn(int index, Pawn pawn){
		this.pawns[index] = pawn;
	}
}
