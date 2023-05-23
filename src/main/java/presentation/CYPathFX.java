package presentation;

import java.io.File;
import java.util.HashMap;

/*
 * Importing java classes needed for the CYPathFX class
 */

import java.util.LinkedList;
import java.util.Optional;

/*
 * Importing javafx classes needed for the CYPathFX class
 */

import abstraction.*;
import control.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("deprecation")

/**
 * Main JavaFX class of the CYPath project
 * Manage all the JavaFX elements and the game loop for the window mode
 * 
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

public class CYPathFX extends Application {
    /**
     * The action button used in the game interface.
     */
    public Button actionButton;

    /**
     * The GameFX object representing the game.
     */
    public GameFX game;

    /**
     * The GridPane used to display the game grid.
     */
    public GridPane gPane;

    /**
     * The fence object.
     */
    private Fence fence;

    /**
     * A linked list of Line objects representing previously highlighted fences.
     */
    public LinkedList<Line> prevHighlightedFencesList;

    /**
     * A linked list of Rectangle objects representing previously possible cells.
     */
    public LinkedList<Rectangle> previousPossibleCells = new LinkedList<Rectangle>();

    /**
     * The color used for cell hover effects.
     */
    public Color cellColorHover;

    /**
     * A boolean indicating the move mode.
     */
    private boolean moveMode;

    /**
     * The Text object representing the fence counter.
     */
    public Text fenceCounter;

    /**
     * The terminal thread use during the game.
     */
    private Thread terminalThread;

    /**
     * The primary stage of the application.
     */
    private Stage primaryStage;

    /**
     * The main menu scene.
     */
    private Scene mainMenuScene;

    /**
     * The new game menu scene.
     */
    private Scene newGameMenuScene;

    /**
     * The game scene.
     */
    private Scene gameScene;

    /**
     * The continue game button.
     */
    private Button continueGameButton;

    private Button gameSkipTurnButton;


    /**
     * Default constructor
     */
    
    public CYPathFX() {}

    /**
     * Method running at the launch of the graphical interface.
     * @param primaryStage Stage of the window
     * @throws Exception Any exception thrown by start
     */

    public void start(Stage primaryStage) throws Exception {
        this.cellColorHover = Color.rgb(239,255,172);
        this.prevHighlightedFencesList = new LinkedList<Line>();
        this.moveMode = true;
        this.fence = new Fence(Orientation.HORIZONTAL);
        this.gPane = null;
        this.actionButton = new Button("Place fence");
        this.mainMenuScene = null;
        this.newGameMenuScene = null;
        this.gameScene = null;
        this.primaryStage = primaryStage;
        this.terminalThread = null;
        this.fenceCounter = new Text("0");
        this.continueGameButton = new Button("Continue current game");
        this.continueGameButton.setOnAction(e -> {
            goToGameScene();
        });
        this.continueGameButton.setManaged(false);
        this.gameSkipTurnButton = null;

        // Set up stage
        primaryStage.setTitle("CY Path : the Game");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        // Open main menu
        createMainMenuScene();
        goToMainMenuScene();
    }

    /**
     * Create the main menu scene
     */
    public void createMainMenuScene() {
        BorderPane rootMainMenu = new BorderPane();
        this.mainMenuScene = new Scene(rootMainMenu);
        
        HBox buttonsMenuHBox = new HBox();
        rootMainMenu.setCenter(buttonsMenuHBox);

        Button newGameMenuButton = new Button("New Game");
        newGameMenuButton.setOnAction(e -> goToNewGameMenu());

        Button loadButton = new Button("Load");

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        loadButton.setOnAction(e -> loadGame());


        buttonsMenuHBox.getChildren().addAll(newGameMenuButton, loadButton, exitButton, continueGameButton);
    }

    /**
     * Change the scene to go the main menu
     */
    public void goToMainMenuScene() {
        if(this.mainMenuScene == null) {
            createMainMenuScene();
        }

        this.continueGameButton.setManaged(this.game != null);
        
        this.primaryStage.setScene(this.mainMenuScene);
        if(!this.primaryStage.isShowing()) {
            this.primaryStage.show();
        }
    }

    /**
     * Change the scene to the new game scene
     */
    public void goToNewGameMenu() {
        if (this.newGameMenuScene == null) {
            createNewGameScene();
        }

        this.primaryStage.setScene(this.newGameMenuScene);
    }

    /**
     * Prepare the game and game scene and then change the scene to the game scene
     * @param nPlayer Number of players for the game
     */
    public void prepareGameScene(int nPlayer){
        try {
            initializeGame(nPlayer);
            createGameScene();
            goToGameScene();
        } catch (GameNotInitializedException err) {
            err.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Create the menu when creating a new game
     */
    public void createNewGameScene() {
        BorderPane root = new BorderPane();
        
        Button twoPlayersModeButton = new Button("2 Players");
        twoPlayersModeButton.setOnAction(e -> {
            prepareGameScene(2);
        });
        Button fourPlayersModeButton = new Button("4 Players");
        fourPlayersModeButton.setOnAction(e -> {
            prepareGameScene(4);
        });

        Button goBack = new Button("Main Menu");
        goBack.setOnAction(e -> {
            goToMainMenuScene();
        });

        root.setTop(goBack);
        root.setLeft(twoPlayersModeButton);
        root.setRight(fourPlayersModeButton);
        
        this.newGameMenuScene = new Scene(root);
    }

    /**
     * Prepare the game to be launched with the graphical interface.
     * @param nbPlayers Number of players for the game
     */
    public void initializeGame(int nbPlayers) {
        Player[] players = new Player[nbPlayers];
        HashMap<Integer, Player> playersPawnIndex = new HashMap<Integer, Player>(4);
        for (int i = 0; i < nbPlayers; i++){
            players[i] = new Player("Player" + i);
            playersPawnIndex.put(i, players[i]);
        }
        try {
            this.game = new GameFX(players,20, 9, 9, playersPawnIndex);
        } catch (InvalidNumberOfPlayersException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Create a new game scene, ready to by used py the players
     * @throws GameNotInitializedException If the game is not initialised properly
     */
    public void createGameScene() throws GameNotInitializedException {
        if(this.game == null) {
            throw new GameNotInitializedException();
        }
        BorderPane rootGameScene = new BorderPane();
        
        Button goBack = new Button("Main Menu");
        goBack.setOnAction(e -> {
            goToMainMenuScene();
        });
        this.actionButton.setOnAction(new ActionButtonControl(this, this.actionButton, this.game));
        
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveGame());

        this.gameSkipTurnButton = new Button("Skip");
        this.gameSkipTurnButton.setVisible(false);
        this.gameSkipTurnButton.setOnAction(e -> {
            this.game.setIsEndTurn(true);
            this.gameSkipTurnButton.setVisible(false);
            this.actionButton.fire();
            this.actionButton.fire();
        });
        
        HBox buttonsHBox = new HBox();
        buttonsHBox.getChildren().addAll(actionButton, saveButton, goBack, fenceCounter,gameSkipTurnButton);
        this.gPane = createBoard();
        rootGameScene.setCenter(this.gPane);
        rootGameScene.setTop(buttonsHBox);
        gameScene = new Scene(rootGameScene);

        this.game.isEndGame.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Button newGameButton = new Button("New game");
                newGameButton.setOnAction(e -> {
                    prepareGameScene(this.game.getNbPlayers());
                });

                Text winner = null;
                try {
                    winner = new Text("Winner is " + this.game.getBoard().getPawn(this.game.getBoard().getWinner()).getColor().toString());
                } catch (IncorrectPawnIndexException e1) {
                    e1.printStackTrace();
                }
                
                this.gPane.setDisable(true);
                buttonsHBox.getChildren().clear();
                buttonsHBox.getChildren().addAll(winner,newGameButton,goBack);    
                buttonsHBox.setStyle("-fx-alignment: center;");  
                buttonsHBox.setSpacing(10);

            } 
        });

        Text currentPlayerText = new Text();
        CurrentPlayerTextControl currentPlayerTextControl = new CurrentPlayerTextControl(this.game, currentPlayerText);
        this.game.addObserver(currentPlayerTextControl);
        buttonsHBox.getChildren().add(currentPlayerText);
        //We click on the button two times for update the first player action
        actionButton.fire();
        actionButton.fire();
        //Create a thread to run in the terminal
        if(this.terminalThread != null && this.terminalThread.isAlive()) {
            this.terminalThread.interrupt();
        }
        this.terminalThread = new Thread(() -> {
            this.game.launch();
        });
        this.terminalThread.setDaemon(true);
        this.terminalThread.start();
    }

    /**
     * Change the scene to be the game by creating a new game scene.
     */
    public void goToGameScene()  {
        if(this.gameScene == null) {
            try {
                createGameScene();
            } catch (GameNotInitializedException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText(""+e);
                alert.showAndWait();
                goToMainMenuScene();
            }
        }

        this.primaryStage.setScene(gameScene);
    }

    /**
     * Get the state of move mode.
     * @return True if the mode is "Move", false if the mode is "Place fence"
     */
    public boolean isMoveMode() {
        return moveMode;
    }

    /**
     * Change the mode of action
     * The mode can be "Move" or "Place fence" with the state of moveMode.
     * @param moveMode True if the mode is "Move", false if the mode is "Place fence"
     */
    public void setMoveMode(boolean moveMode) {
        this.moveMode = moveMode;
    }

    /**
     * Create a line to be the border
     * @param xStart X coordinate for the starting point
     * @param yStart Y coordinate for the starting point
     * @param xEnd X coordinate for the ending point
     * @param yEnd Y coordinate for the ending point
     * @param color Color of the line
     * @param lineWidth Width of the line
     * @return Line to be used as a border
     */
    public Line createLineBorder(int xStart, int yStart, int xEnd, int yEnd, Color color, int lineWidth) {
        Line border = new Line(xStart,yStart,xEnd,yEnd);
        border.setStrokeWidth(lineWidth);
        border.setStroke(color);
        return border;
    }


    /**
     * Create a cell of the board and add it
     * 
     * @param parent Board where the cell is added
     * @param row Row of the cell added
     * @param col Column of the cell added
     * @param size Size of the cell added (size = width = height)
     * @param color Color of the cell background
     */

    private void createBoardCell(GridPane parent, int row, int col, int size, Color color) {
        if(parent != null) {
            StackPane cellStackPane = new StackPane(); // Create a new StackPane for every cell
            Rectangle cell = new Rectangle(size, size);

            cell.setFill(color);
            cellStackPane.setOnMouseEntered(new HoverBorderControl(this, this.fence));
            cellStackPane.setOnMouseExited(new HoverBorderControl(this, this.fence));
            cellStackPane.setOnMouseClicked(new ClickCellControl(this, this.fence, this.game, this.actionButton));
            cellStackPane.getChildren().add(cell);       

            // Check if there is a pawn on this cell and add it if there is one
            Pawn pawn = this.game.getBoard().getPawnAtPos(CYPathFX.gPaneCellCoordToGameCoord(col,row));
            if(this.game.getBoard().getPawnAtPos(CYPathFX.gPaneCellCoordToGameCoord(col,row)) != null) {
                cellStackPane.getChildren().add(this.createPlayerCircle(pawn.getColor()));
            }

            parent.add(cellStackPane, col, row);
        }
    }

    /**
     * Create the grid for the player to play on.
     * The grid is main element in the graphical interface of the game.
     * 
     * @return GridPane with a grid drawn on it.
     */

    public GridPane createBoard() {
        GridPane gPane = new GridPane();
        int sizeBoardRows = this.game.getBoard().getNbRows();
        int sizeBoardColumns = this.game.getBoard().getNbCols();
        Line border = null;
        int lineLength = 50;
        int cellSize = 50;
        int lineWidth = 8;
        int lineLengthBorders = lineLength + lineWidth;
        Color borderColor = Color.LIGHTGRAY;
        Color cellColor = Color.rgb(230, 230, 230);

        // First horizontal border (top)
        for (int j = 1; j <= 2 * sizeBoardColumns; j += 2) {
            border = createLineBorder(0, 0, lineLengthBorders, 0, borderColor, lineWidth);
            gPane.add(border, j, 0);
        }
        gPane.getRowConstraints().add(new RowConstraints(lineWidth));
    
        for (int i = 1; i <= 2 * sizeBoardRows; i += 2) {
            // First vertical border (left)
            border = createLineBorder(0, 0, 0, lineLengthBorders, borderColor, lineWidth);
            gPane.add(border, 0, i);
    
            for (int j = 1; j <= 2 * sizeBoardColumns; j += 2) {
                // Cell
                createBoardCell(gPane, i, j, cellSize, cellColor);
    
                // Vertical border
                border = createLineBorder(0, 0, 0, lineLength, borderColor, lineWidth);
                gPane.add(border, j + 1, i);
            }
    
            // Horizontal borders
            for (int j = 1; j < 2 * sizeBoardColumns; j += 2) {
                border = createLineBorder(0, 0, lineLength, 0, borderColor, lineWidth);
                gPane.add(border, j, i + 1);
            }
            // Horizontal borders
            gPane.getRowConstraints().add(new RowConstraints(cellSize));
            gPane.getRowConstraints().add(new RowConstraints(lineWidth));
        }
    
        gPane.getColumnConstraints().add(new ColumnConstraints(lineWidth));
        for (int i = 0; i < sizeBoardColumns; i++) {
            gPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
            gPane.getColumnConstraints().add(new ColumnConstraints(lineWidth));
        }

        // Add fences to board (after loading a game from a save)
        addFencesToBoard(gPane);
        return gPane;
    }

    /**
     * Add a fence on the board show the player graphical interface made with JavaFX
     * @param gPane Grid shown to the player
     */
    private void addFencesToBoard(GridPane gPane){
        for(Fence f : this.game.getBoard().getFencesArray()) {
            Point pStartFenceGPaneCoord = new Point(f.getStart().getX()*2,f.getStart().getY()*2);
            Point pEndFenceGPaneCoord = new Point(f.getEnd().getX()*2,f.getEnd().getY()*2);
            
            if(f.getOrientation() == Orientation.HORIZONTAL) {
                int y = pStartFenceGPaneCoord.getY();
                for(int x = pStartFenceGPaneCoord.getX(); x < pEndFenceGPaneCoord.getX(); x++) {
                    Node n = this.getNodeFromGridPane(gPane, y, x);
                    if(n instanceof Line) {
                        Line l = (Line) n;
                        l.setStroke(Color.BLACK);
                        l.toFront();
                    }
                }
            } else {
                int x = pStartFenceGPaneCoord.getX();
                for(int y = pStartFenceGPaneCoord.getY(); y < pEndFenceGPaneCoord.getY(); y++) {
                    Node n = this.getNodeFromGridPane(gPane, y, x);
                    if(n instanceof Line) {
                        Line l = (Line) n;
                        l.setStroke(Color.BLACK);
                        l.toFront();
                    }
                }
            }
        }
    }

    /**
     * Create a circle to be the pawn of the player
     * @param colorP Color of the pawn
     * @return Circle fill with a color
     */
    private Circle createPlayerCircle(ColorPawn colorP) {
        Circle circle = new Circle(15, colorP.toColorFX());
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        return circle;
    }

    /**
     * Add the pawn of the player, represented by a circle, to a specific cell with its coordinate.
     * @param gridPane Grid shown to the player
     * @param rowIndex Index of the circle's row in the grid pane
     * @param columnIndex Index of the circle's column in the grid pane
     * @param color Color of the circle
     */
    public void addCircleToCell(GridPane gridPane, int rowIndex, int columnIndex, ColorPawn color) {
        StackPane stack = getCellStackPane(gridPane, rowIndex, columnIndex);
        if(stack != null) {
            Circle circle = createPlayerCircle(color);
            stack.getChildren().add(circle);
        }
    }

    /**
     * Remove the pawn of the player, represented by a circle, from a specific cell with its coordinate.
     * @param gridPane Grid shown to the player
     * @param rowIndex Index of the circle's row in the grid pane
     * @param columnIndex Index of the circle's column in the grid pane
     */
    public void removeCircleFromCell(GridPane gridPane, int rowIndex, int columnIndex) {
        StackPane stack = getCellStackPane(gridPane, rowIndex, columnIndex);
        if(stack != null) {
            stack.getChildren().removeIf(node -> node instanceof Circle);
        }
    }

    /**
	 * Get a specific node from the GridPane.
	 * 
	 * @param gridPane is the main pane.
	 * @param row the row from the node we want.
     * @param col the column from the node we want.
	 * @return The specific node from the GridPane we were looking for.
	 */

    public Node getNodeFromGridPane(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Get the stack pane from the grid pane with its coordinate.
     * @param gridPane Grid shown to the player
     * @param row Row number of the stack pane
     * @param column Column number of the stack pane
     * @return The stack pane at this coordinate
     */
    public StackPane getCellStackPane(GridPane gridPane, int row, int column) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row && node instanceof StackPane) {
                return (StackPane) node;
            }
        }
        return null;
    }

    /**
     * Displays a pop-up window in the center of the screen.
     *
     * @param owner The owner stage of the pop-up.
     */
    private void showPopupWindow(Stage owner) {
        //Creation of the pop-up window
        Stage popupStage = new Stage(StageStyle.UTILITY);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(owner);
        popupStage.setTitle("You can't move");
        popupStage.setWidth(400);
        popupStage.setHeight(80);

        //Content of the pop-up window
        Label label = new Label(this.game.getCurrentPawn().getColor().toString() + " can't move. Try to place a fence or skip your turn");
        StackPane popupRoot = new StackPane(label);
        popupRoot.setAlignment(Pos.CENTER);

        //Adding content to the pop-up window
        popupStage.setScene(new Scene(popupRoot));
        popupStage.showAndWait();
    }

    /**
	 * Color all the cells that the player can move to.
     * Change the cell's color if the player hover.
	 * 
	 * @param pawnId Int representing the ID of the Player.
	 */

    public void showPossibleCells(int pawnId){
        LinkedList<Point> possibleMoves = null;
        try {
            possibleMoves = this.game.getBoard().listPossibleMoves(this.game.getBoard().getPawn(pawnId).getPosition());

            if(possibleMoves.isEmpty()){
                System.out.println(this.game.getCurrentPawn().getColor().toString() + " pawn can't move");
                showPopupWindow(primaryStage);
                this.gameSkipTurnButton.setVisible(true);
            }

        } catch(IncorrectPawnIndexException err) {
            err.printStackTrace();
            System.exit(-1);
        }
        
        for( Point p : possibleMoves){
            StackPane stack = getCellStackPane(gPane, p.getY()*2+1, p.getX()*2+1);
            ObservableList<Node> children = stack.getChildren();
            int lastIndex = children.size() - 1;
            Node node = children.get(lastIndex);

            if( node instanceof Rectangle){
                Rectangle rec = (Rectangle) node;
                rec.setFill(this.game.getCurrentPawn().getColor().toColorPossibleMove());
                this.previousPossibleCells.add(rec);
            }
        }
    }
    
    /**
     * Get the coordinates of a node in its parent GridPane
     * 
     * @param node Node in the GridPane
     * @return Position of the node in the GridPane
     */

    public static Point getGPaneNodeCoord(Node node) {
        Point coord = new Point(0, 0);
        coord.setX(GridPane.getColumnIndex(node));
        coord.setY(GridPane.getRowIndex(node));

        return coord;
    }

    /**
     * Convert the coordinates of a cell in a GridPane to the coordinates used in the game
     * 
     * @param nodeCoord Position of the node in the parent GridPane
     * @return Position in the game
     */
    
    public static Point gPaneCellCoordToGameCoord(Point nodeCoord) {   
        return gPaneCellCoordToGameCoord(nodeCoord.getX(), nodeCoord.getY());
    }

    /**
     * Convert the coordinates of a cell in a GridPane to the coordinates used in the game
     * 
     * @param x X coordinate of the node in the parent GridPane
     * @param y Y coordinate of the node in the parent GridPane
     * @return Position in the game
     */

    public static Point gPaneCellCoordToGameCoord(int x, int y) {   
        Point coord = new Point(0, 0);
        coord.setX((x-1) / 2);
        coord.setY((y-1) / 2);
        
        return coord;
    }

    /**
	 * Reset previous possible cells to be updated with the game.
     * 
	 * @param pawnId Int representing the ID of the player.
	 */

    public void resetPossibleCells(int pawnId){
        Color cellColor = Color.rgb(230, 230, 230);

        while (!previousPossibleCells.isEmpty()) {
            Rectangle rec = previousPossibleCells.getFirst();
            rec.setFill(cellColor);
            previousPossibleCells.removeFirst();
        }
    }

    /**
     * Load a game from a file
     */
    private void loadGame()  {
        String saveDefaultPath = "./src/main/resources/data/saves";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.json"));
        new File(saveDefaultPath).mkdirs();
        fileChooser.setInitialDirectory(new File(saveDefaultPath));

        File file = fileChooser.showOpenDialog(this.primaryStage);

        
        if(file != null) {
            LoadDataFromJSONFile loadDataObject = new LoadDataFromJSONFile();
            Alert alert = null;
            try {
                loadDataObject.load(file.getAbsolutePath());
                HashMap<Integer,Player> playersPawnIndex = new HashMap<Integer,Player>(4);
                Player[] players = new Player[loadDataObject.getPawns().length];
                for(int i=0; i<players.length; i++) {
                    players[i] = new Player("Player"+i);
                    playersPawnIndex.put(i, players[i]);
                }

                this.game = new GameFX(players, loadDataObject.getMaxNbFences(), loadDataObject.getRows(), loadDataObject.getColumns(), playersPawnIndex, loadDataObject.getPawns(), loadDataObject.getCurrentPawnIndex());
                for(Fence f : loadDataObject.getListFences()){
                    this.game.getBoard().addFenceToData(f);
                }

                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Game successfully loaded");
                alert.showAndWait();
                this.createGameScene();
                this.goToGameScene();
            } catch (Exception e) {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error while loading the game.\n\n"+e);
                e.printStackTrace();
                alert.showAndWait();
            }
        }
    }


    /**
     * Save a game to a file
     */
    private void saveGame() {
        boolean failure = false;
        TextInputDialog dialog = new TextInputDialog("save");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alertAlreadyExists = new Alert(AlertType.WARNING,"The file already exists.\nDo you want to overwrite it?",yesButton,noButton);
        alertAlreadyExists.setTitle("File already exists");

        dialog.setContentText("Choose a name for your save file");
        
        dialog.showAndWait();
        Optional<String> dialogResult = dialog.showAndWait();
        String fileName = null;

        if (dialogResult.isPresent()){
            fileName = dialogResult.get();
        }

        if(fileName != null) {
            SaveDataInJSONFile saveDataObject = new SaveDataInJSONFile(this.game.getBoard().getNbRows(), this.game.getBoard().getNbCols(), this.game.getBoard().getFencesArray(), this.game.getNbMaxTotalFences(), this.game.getBoard().getPawnsArray(), this.game.getCurrentPawnIndex());
            Alert alert = null;
            try {
                saveDataObject.save(fileName, false);
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Game saved");
                alert.showAndWait();
            } catch(FileNameIsDuplicateException e) {
                Optional<ButtonType> overwriteResult = alertAlreadyExists.showAndWait();
                ButtonBar.ButtonData overwriteChoice = null;
                if (overwriteResult.isPresent()) {
                    overwriteChoice = overwriteResult.get().getButtonData();
                }
                if(overwriteChoice == ButtonBar.ButtonData.OK_DONE) {
                    try {
                        saveDataObject.save(fileName, true);
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setContentText("Game saved");
                        alert.showAndWait();
                    } catch (Exception err) {
                        failure = true;
                    }
                }
            } catch (Exception e) {
                failure = true;
            }
            if(failure) {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error while saving the game. Please check if the file already exists in resources/data/saves");
                alert.showAndWait();
            }
        }
    }

    /**
     * Main method
     * Launch the game in console mode or window mode
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}