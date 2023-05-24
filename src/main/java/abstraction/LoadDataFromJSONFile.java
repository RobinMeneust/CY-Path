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
        String pattern = "^[a-zA-Z0-9_-.]+$";
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
      * Procedure to load a game from a file save in the backup folder
      *
      * @param filePath Filepath of the file to load
      * @throws FileNameNotExistException If the file name entered doesn't exist
      * @throws IOException If an I/O error occurs while reading the file
      * @throws ParseException If an error occurs while parsing the JSON data
      * @throws FileNameException If the file name entered by the player is incorrect
      * @throws FileContentModifiedException If the file loaded by the palyer are incorrect
      */
    public void load(String filePath) throws FileNameNotExistException, IOException, ParseException, FileNameException, FileContentModifiedException  {
        String orientation;

        int id;
        String startingSide;
        String color;
        int nbRemainingFences;
        int positionX;
        int positionY;
                
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNameNotExistException();
        }

        if(!isFileNameValid(extractFileNameWithoutExtension(filePath))) {    
            throw new FileNameException();
        }

        try {
            JSONParser parser = new JSONParser();

            JSONObject gameObjects = (JSONObject) parser.parse(new FileReader(filePath));

            if((gameObjects.get("rows") instanceof Integer) && ((Integer)(gameObjects.get("rows")) >= 2)) {
                rows = ((Integer) gameObjects.get("rows")).intValue();
            } else {
                throw new FileContentModifiedException();
            }

            if((gameObjects.get("columns") instanceof Integer) && ((Integer)(gameObjects.get("columns")) >= 2)) {
                columns = ((Integer) gameObjects.get("columns")).intValue();
            } else {
                throw new FileContentModifiedException();
            }

            if((gameObjects.get("maxNbFences") instanceof Integer) && ((Integer)(gameObjects.get("maxNbFences")) >= 0)) {
                maxNbFences = ((Integer) gameObjects.get("maxNbFences")).intValue();
            } else {
                throw new FileContentModifiedException();
            }

            if((gameObjects.get("currentPawnIndex") instanceof Integer) && ((Integer)(gameObjects.get("currentPawnIndex")) >= 0)) {
                currentPawnIndex = ((Integer) gameObjects.get("currentPawnIndex")).intValue();
            } else {
                throw new FileContentModifiedException();
            }
            
            Object FencesObj = gameObjects.get("listFences");
            if((FencesObj != null) && (FencesObj instanceof JSONArray)) { 
                JSONArray listFencesJSON = (JSONArray) FencesObj;
                Iterator<?> iteratorFence = listFencesJSON.iterator(); // It is an array that contains objects that can be a string associated with the key "orientation" and then two int that form an object associated with the key "start" and "end" 
                this.listFences = new ArrayList<Fence>(); //check if it's really what we want to have
                while(iteratorFence.hasNext()) {
                    Object iterFence = iteratorFence.next();
                    if (iterFence instanceof JSONObject) {
                        JSONObject fenceJSON = (JSONObject) iterFence;

                        if (fenceJSON.get("orientation") instanceof String) {
                            orientation = ((String) fenceJSON.get("orientation")).toString();
                        } else {
                            throw new FileContentModifiedException();
                        }

                        Point start = new Point(0,0);

                        if((fenceJSON.get("start") instanceof JSONObject)) {
                            if(fenceJSON.get("x") instanceof Integer) {
                                start.setX(((Integer) fenceJSON.get("x")).intValue());
                                if(start.getX() < 0) {
                                    throw new FileContentModifiedException();
                                }
                            } else {
                                throw new FileContentModifiedException();
                            }

                            if(fenceJSON.get("y") instanceof Integer) {
                                start.setY(((Integer) fenceJSON.get("y")).intValue());
                                if(start.getY() < 0) {
                                    throw new FileContentModifiedException();
                                }
                            } else {
                                throw new FileContentModifiedException();
                            }

                        } else {
                            throw new FileContentModifiedException();
                        }                        

                        Point end = new Point(0,0);

                        if((fenceJSON.get("end") instanceof JSONObject)) {
                            if(fenceJSON.get("x") instanceof Integer) {
                                end.setX(((Integer) fenceJSON.get("x")).intValue());
                                if(end.getX() < 0) {
                                    throw new FileContentModifiedException();
                                }
                            } else {
                                throw new FileContentModifiedException();
                            }

                            if(fenceJSON.get("y") instanceof Integer) {
                                end.setY(((Integer) fenceJSON.get("y")).intValue());
                                if(end.getY() < 0) {
                                    throw new FileContentModifiedException();
                                }
                            } else {
                                throw new FileContentModifiedException();
                            }

                        } else {
                            throw new FileContentModifiedException();
                        } 
                        
                        try {
                            this.listFences.add(new Fence(Orientation.valueOf(orientation), start, end));
                        } catch (Exception e) {
                            throw new FileContentModifiedException();
                        }

                    } else {
                        throw new FileContentModifiedException();
                    }
                }

            } else {
                throw new FileContentModifiedException();
            }

            Object PawnsObj = gameObjects.get("listPawns");
            if((PawnsObj != null) && (PawnsObj instanceof JSONArray)) { 
                JSONArray listPawnsJSON = (JSONArray) PawnsObj;
                Iterator<?> iteratorPawn = listPawnsJSON.iterator(); //C'est un tableau qui contient des objets qui peuvent être un int associé à la clé "id", un Side associé à la clé "startingSide", une String associée à la clé "color", puis deux int associé à la clé "x" et "y" qui forme un objet associé à la clé "pos" et un int associé à la clé "nbRemainingFences" 
                int size = listPawnsJSON.size(); //check if it's really what we want to have
                this.pawns = new Pawn[size];
                int i = 0;
                while(iteratorPawn.hasNext()) {
                    Object iterPawn = iteratorPawn.next();
                    if (iterPawn instanceof JSONObject) {
                        JSONObject pawnJSON = (JSONObject) iterPawn;

                        if (pawnJSON.get("id") instanceof Integer) {
                            id = ((Integer) pawnJSON.get("id")).intValue();
                            if(id < 0) {
                                throw new FileContentModifiedException();
                            }
                        } else {
                            throw new FileContentModifiedException();
                        }

                        if (pawnJSON.get("startingSide") instanceof String) {
                            startingSide = ((String) pawnJSON.get("startingSide")).toString();
                        } else {
                           throw new FileContentModifiedException();
                        }

                        if (pawnJSON.get("color") instanceof String) {
                            color = ((String) pawnJSON.get("color")).toString();
                        } else {
                            throw new FileContentModifiedException();
                        }

                        if (pawnJSON.get("pos") instanceof JSONObject) {
                            JSONObject position = (JSONObject) pawnJSON.get("pos");
                            if (position.get("x") instanceof Integer) {
                                positionX = ((Integer) position.get("x")).intValue();
                                if (positionX < 0) {
                                    throw new FileContentModifiedException();
                                }
                            } else {
                                throw new FileContentModifiedException();
                            }

                            if (position.get("y") instanceof Integer) {
                                positionY = ((Integer) position.get("y")).intValue();
                                if (positionY < 0) {
                                    throw new FileContentModifiedException();
                                }
                            } else {
                                throw new FileContentModifiedException();
                            }
                        } else {
                            throw new FileContentModifiedException();
                        }

                        if (pawnJSON.get("nbRemainingFences") instanceof Integer) {
                            nbRemainingFences = ((Integer) pawnJSON.get("nbRemainingFences")).intValue();
                        } else {
                           throw new FileContentModifiedException();
                        }

                        try {
                            this.pawns[i] = new Pawn(id, Side.valueOf(startingSide), ColorPawn.valueOf(color), new Point(positionX, positionY), nbRemainingFences);
                            i++;
                        } catch (Exception e) {
                            throw new FileContentModifiedException();
                        }

                    } else {
                        throw new FileContentModifiedException();
                    }
                }

            } else {
                throw new FileContentModifiedException();
            }

        } catch (FileContentModifiedException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (ParseException e) {
            throw e;
        }
    }
}
