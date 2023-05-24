package presentation;

import java.io.File;
import java.util.HashMap;

/*
 * Importing java classes needed for the CYPathFX class
 */

import java.util.LinkedList;

/*
 * Importing javafx classes needed for the CYPathFX class
 */

import abstraction.*;
import control.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * Main JavaFX class of the CYPath project
 * Manage all the JavaFX elements and the game loop for the window mode
 *
 * @author BARRE Romain, ETRILLARD Yann, GARCIA-MEGEVAND Thibault, KUSMIDER David, MENEUST Robin
 */

@SuppressWarnings("deprecation")
public class CYPathFX extends Application {
    /**
     * The action button used in the game interface.
     */
    private Button actionButton;

    /**
     * The GameFX object representing the game.
     */
    private GameFX game;

    /**
     * The GridPane used to display the game grid.
     */
    private GridPane gPane;

    /**
     * The fence object.
     */
    private Fence fence;

    /**
     * A linked list of Line objects representing previously highlighted fences.
     */
    private LinkedList<Line> prevHighlightedFencesList;

    /**
     * A linked list of Rectangle objects representing previously possible cells.
     */
    private LinkedList<Rectangle> previousPossibleCells = new LinkedList<Rectangle>();

    /**
     * The color used for cell hover effects.
     */
    private Color cellColorHover;

    /**
     * A boolean indicating the move mode.
     */
    private boolean moveMode;

    /**
     * The Text object representing the fence counter.
     */
    private Text fenceCounter;

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

    /**
     * Button to skip a player's turn
     */
    private Button gameSkipTurnButton;

    /**
     * Default path of save files
     */
    private final String saveDefaultPath = "./data/saves";


    /**
     * Default constructor
     */

    public CYPathFX() {}

    /**
     * Get the action button
     * @return Action Button
     */
    public Button getActionButton(){
        return this.actionButton;
    }

    /**
     * Get the game associated to the graphical interface
     * @return Current game
     */
    public GameFX getGame() {
        return game;
    }

    /**
     * Get color of cells when you can hover them
     * @return Color of when hovering cells
     */
    public Color getCellColorHover() {
        return cellColorHover;
    }

    /**
     * Get the list of highlighted fences, representing by a line on the graphical interface
     * @return List of Line
     */
    public LinkedList<Line> getPrevHighlightedFencesList() {
        return prevHighlightedFencesList;
    }

    /**
     * Get the list of cells where it's possible for the player to move on
     * @return List of Rectangle where the player can go to
     */
    public LinkedList<Rectangle> getPreviousPossibleCells() {
        return previousPossibleCells;
    }

    /**
     * Get the grid show to the player
     * @return GridPane show to the player
     */
    public GridPane getGPane(){
        return this.gPane;
    }

    /**
     * Get the text of number of fences
     * @return Text of fence's number
     */
    public Text getFenceCounter() {
        return fenceCounter;
    }

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
        this.continueGameButton.getStyleClass().add("menu-button");
        this.continueGameButton.setId("continue-game-button");
        this.continueGameButton.setOnAction(e -> {
            goToGameScene();
        });
        this.continueGameButton.setManaged(false);
        this.gameSkipTurnButton = null;

        // Set up stage
        this.primaryStage.setTitle("CY Path : the Game");
        this.primaryStage.setMinWidth(545);
        this.primaryStage.setMinHeight(595);
        this.primaryStage.setWidth(545);
        this.primaryStage.setHeight(595);
        this.primaryStage.setOnCloseRequest(e -> {
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

        Label titleMainMenu = new Label("CY Path");
        titleMainMenu.setAlignment(Pos.CENTER);
        titleMainMenu.setId("title-main-menu");
        rootMainMenu.setTop(titleMainMenu);
        BorderPane.setAlignment(titleMainMenu, Pos.CENTER);

        Button newGameMenuButton = new Button("New Game");
        newGameMenuButton.setId("new-game-button");
        newGameMenuButton.getStyleClass().add("menu-button");
        newGameMenuButton.setOnAction(e -> goToNewGameMenu());

        Button loadButton = new Button("Load");
        loadButton.setId("load-game-button");
        loadButton.getStyleClass().add("menu-button");

        Button exitButton = new Button("Exit");
        exitButton.setId("exit-game-button");
        exitButton.getStyleClass().add("menu-button");
        exitButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        loadButton.setOnAction(e -> loadGame());

        VBox buttonsMenuHBox = new VBox(15);
        buttonsMenuHBox.setAlignment(Pos.CENTER);
        rootMainMenu.setCenter(buttonsMenuHBox);

        buttonsMenuHBox.getChildren().addAll(newGameMenuButton, loadButton, exitButton, continueGameButton);
        mainMenuScene.getStylesheets().add("styleMainMenu.css");
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
    
        // Creation of the buttons
        Button twoPlayersModeButton = new Button("2 Players");
        twoPlayersModeButton.setPadding(new Insets(10, 28, 10, 28));
        twoPlayersModeButton.getStyleClass().add("menu-button");
        twoPlayersModeButton.setId("two-players-mode-button");
        twoPlayersModeButton.setOnAction(e -> {
            prepareGameScene(2);
        });
        Button fourPlayersModeButton = new Button("4 Players");
        fourPlayersModeButton.setPadding(new Insets(10, 28, 10, 28));
        fourPlayersModeButton.getStyleClass().add("menu-button");
        fourPlayersModeButton.setId("four-players-mode-button");
        fourPlayersModeButton.setOnAction(e -> {
            prepareGameScene(4);
        });
    
        Button goBack = new Button("Main Menu");
        goBack.setPadding(new Insets(10, 20, 10, 20));
        goBack.getStyleClass().add("menu-button");
        goBack.setId("go-back");
        goBack.setOnAction(e -> {
            goToMainMenuScene();
        });
    
        // Creation of the label
        Label titleGameMode = new Label("Game mode");
        titleGameMode.setId("title-game-mode");
        titleGameMode.setAlignment(Pos.CENTER);
        titleGameMode.setPadding(new Insets(10));
    
        // Creation of the VBox
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(twoPlayersModeButton, fourPlayersModeButton, goBack);
        vbox.setAlignment(Pos.CENTER);

        // Adding the VBox in the center of the BorderPane
        root.setTop(titleGameMode);
        root.setCenter(vbox);
        BorderPane.setAlignment(titleGameMode, Pos.CENTER);
    
        this.newGameMenuScene = new Scene(root);
        this.newGameMenuScene.getStylesheets().add("styleGameMode.css");
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
            this.game = new GameFX(players,20, 9, 9, playersPawnIndex, 2);
        } catch (Exception e) {
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
        this.getActionButton().setOnAction(new ActionButtonControl(this, this.getActionButton(), this.game));
        
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveGame());

        this.gameSkipTurnButton = new Button("Skip");
        this.gameSkipTurnButton.setVisible(false);
        this.gameSkipTurnButton.setOnAction(e -> {
            this.game.setIsEndTurn(true);
            this.gameSkipTurnButton.setVisible(false);

            checkEndTurn(this.game);

            //update button
            this.getActionButton().fire();
            if (!(this.getActionButton().getText().equals("Place fence"))) {
                this.getActionButton().fire();
            }
        });
        
        HBox buttonsHBox = new HBox();
        buttonsHBox.getChildren().addAll(this.getActionButton(), saveButton, goBack, fenceCounter,gameSkipTurnButton);
        this.gPane = createBoard(50,8,Color.LIGHTGRAY, Color.rgb(230, 230, 230));

        rootGameScene.setCenter(this.gPane);
        rootGameScene.setTop(buttonsHBox);
        gameScene = new Scene(rootGameScene);

        // When it's the end of a game, change the user interface
        this.game.getIsEndGame().addListener((observable, oldValue, newValue) -> {
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
        this.getActionButton().fire();
        this.getActionButton().fire();
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
     * Check if the turn ends and check if it's the end of the game
     * @param game Current game
     */
    public static void checkEndTurn(GameFX game) {
        while (game.getIsEndTurn()) {
            try {
                Thread.sleep(100); //Wait 100 milliseconds before checking again
            } catch (InterruptedException ev) {
                Thread.currentThread().interrupt();
            }
        }

        if(game.getBoard().getWinner() != -1){
            game.getIsEndGame().setValue(true);
        }
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
     * Create a cell of the board
     * 
     * @param row Row of the cell added
     * @param col Column of the cell added
     * @param size Size of the cell added (size = width = height)
     * @param color Color of the cell background
     * @return Cell created
     */

    private StackPane createBoardCell(int row, int col, int size, Color color) {
        StackPane cellStackPane = new StackPane(); // Create a new StackPane for every cell
        Rectangle cell = new Rectangle(size, size);

        cell.setFill(color);
        cellStackPane.setOnMouseEntered(new HoverBorderControl(this, this.fence));
        cellStackPane.setOnMouseExited(new HoverBorderControl(this, this.fence));
        cellStackPane.setOnMouseClicked(new ClickCellControl(this, this.fence, this.game, this.getActionButton()));
        cellStackPane.getChildren().add(cell);       

        // Check if there is a pawn on this cell and add it if there is one
        Pawn pawn = this.game.getBoard().getPawnAtPos(CYPathFX.gPaneCellCoordToGameCoord(col,row));
        if(this.game.getBoard().getPawnAtPos(CYPathFX.gPaneCellCoordToGameCoord(col,row)) != null) {
            cellStackPane.getChildren().add(this.createPlayerCircle(pawn.getColor()));
        }
        return cellStackPane;
    }

    /**
     * Create the grid for the player to play on.
     * The grid is main element in the graphical interface of the game.
     * 
     * @param cellSize Size of the cells (size = width = height)
     * @param lineWidth Width of the line of the borders
     * @param borderColor Color of the borders without fences
     * @param cellColor Color of the cells
     * @return GridPane with a grid drawn on it.
     */

    public GridPane createBoard(int cellSize, int lineWidth, Color borderColor, Color cellColor) {
        GridPane gPane = new GridPane();
        int sizeBoardRows = this.game.getBoard().getNbRows();
        int sizeBoardColumns = this.game.getBoard().getNbCols();
        Line border = null;
        int lineLength = cellSize;

        // To fix the top left corner
        gPane.add(createLineBorder(0, 0, 0, lineWidth, borderColor, lineWidth), 0, 0);

    
        // First horizontal border (top)
        for (int j = 1; j <= 2 * sizeBoardColumns; j += 2) {
            border = createLineBorder(0, 0, lineLength, 0, borderColor, lineWidth);
            gPane.add(border, j, 0);
        }
        gPane.getRowConstraints().add(new RowConstraints(lineWidth));
    
        for (int i = 1; i <= 2 * sizeBoardRows; i += 2) {
            // First vertical border (left)
            border = createLineBorder(0, 0, 0, lineLength, borderColor, lineWidth);
            gPane.add(border, 0, i);
    
            for (int j = 1; j <= 2 * sizeBoardColumns; j += 2) {
                // Cell
                gPane.add(createBoardCell(i, j, cellSize, cellColor), j ,i);
    
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
     * @throws IncorrectJavaFXBoardException If the board created in the JavaFX window mode is incorrect
	 */

    public void showPossibleCells(int pawnId) throws IncorrectJavaFXBoardException {
        LinkedList<Point> possibleMoves = null;
        try {
            possibleMoves = this.game.getBoard().listPossibleMoves(this.game.getBoard().getPawn(pawnId).getPosition());

            if(possibleMoves.isEmpty() && !this.game.getIsEndTurn()){

                System.out.println(this.game.getCurrentPawn().getColor().toString() + " pawn can't move");
                showPopupWindow(primaryStage);
                this.gameSkipTurnButton.setVisible(true);
            }

        } catch(IncorrectPawnIndexException err) {
            err.printStackTrace();
            System.exit(-1);
        }
        
        for(Point p : possibleMoves){
            StackPane stack = getCellStackPane(gPane, p.getY()*2+1, p.getX()*2+1);
            if(stack == null) {
                throw new IncorrectJavaFXBoardException();
            }
            ObservableList<Node> children = stack.getChildren();
            int lastIndex = children.size() - 1;
            Node node = children.get(lastIndex);

            if(node instanceof Rectangle){
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

                this.game = new GameFX(players, loadDataObject.getMaxNbFences(), loadDataObject.getRows(), loadDataObject.getColumns(), playersPawnIndex, loadDataObject.getPawns(), loadDataObject.getCurrentPawnIndex(), 2);
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.json"));
        new File(saveDefaultPath).mkdirs();
        fileChooser.setInitialDirectory(new File(saveDefaultPath));
        fileChooser.setInitialFileName("save.json");

        File file = fileChooser.showSaveDialog(this.primaryStage);
        
        if(file != null) {
            SaveDataInJSONFile saveDataObject = new SaveDataInJSONFile(this.game.getBoard().getNbRows(), this.game.getBoard().getNbCols(), this.game.getBoard().getFencesArray(), this.game.getNbMaxTotalFences(), this.game.getBoard().getPawnsArray(), this.game.getCurrentPawnIndex());
            Alert alert = null;
            try {
                saveDataObject.save(file, true);
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Game saved");
                alert.showAndWait();
            } catch (Exception e) {
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