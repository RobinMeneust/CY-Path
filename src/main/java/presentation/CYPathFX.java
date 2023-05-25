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
import javafx.scene.image.Image;

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
        this.setCellColorHover(Color.rgb(239,255,172));
        this.setPrevHighlightedFencesList(new LinkedList<Line>());
        this.setMoveMode(true);
        this.setFence(new Fence(Orientation.HORIZONTAL));
        this.setGPane(null);
        this.setActionButton(new Button("Place fence"));
        this.setMainMenuScene(null);
        this.setNewGameMenuScene(null);
        this.setGameScene(null);
        this.setPrimaryStage(primaryStage);
        this.setTerminalThread(null);
        this.setFenceCounter(new Text("0"));
        this.setContinueGameButton(new Button("Continue"));
        this.getContinueGameButton().getStyleClass().add("menu-button");
        this.getContinueGameButton().setId("continue-game-button");
        this.getContinueGameButton().setOnAction(e -> {
            goToGameScene();
        });
        this.getContinueGameButton().setManaged(false);
        this.setGameSkipTurnButton(null);

        // Set up stage
        this.getPrimaryStage().setTitle("CY Path : the Game");
        Image icon = new Image("icone-pion.png");
        primaryStage.getIcons().add(icon);
        this.getPrimaryStage().setMinWidth(545);
        this.getPrimaryStage().setMinHeight(595);
        this.getPrimaryStage().setWidth(545);
        this.getPrimaryStage().setHeight(595);
        this.getPrimaryStage().setOnCloseRequest(e -> {
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
        this.setMainMenuScene(new Scene(rootMainMenu));

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

        VBox buttonsMenuVBox = new VBox(15);
        buttonsMenuVBox.setAlignment(Pos.CENTER);
        rootMainMenu.setCenter(buttonsMenuVBox);

        buttonsMenuVBox.getChildren().addAll(newGameMenuButton, this.getContinueGameButton(), loadButton, exitButton);
        this.getMainMenuScene().getStylesheets().add("styleMainMenu.css");
    }

    /**
     * Change the scene to go the main menu
     */
    public void goToMainMenuScene() {
        if(this.getMainMenuScene() == null) {
            createMainMenuScene();
        }

        this.getContinueGameButton().setManaged(this.getGame() != null);
        
        this.getPrimaryStage().setScene(this.getMainMenuScene());
        if(!this.getPrimaryStage().isShowing()) {
            this.getPrimaryStage().show();
        }
    }

    /**
     * Change the scene to the new game scene
     */
    public void goToNewGameMenu() {
        if (this.getNewGameMenuScene() == null) {
            createNewGameScene();
        }

        this.getPrimaryStage().setScene(this.getNewGameMenuScene());
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
            System.err.println(err.getMessage());
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
        VBox buttonsMenuVBox = new VBox(15);
        buttonsMenuVBox.getChildren().addAll(twoPlayersModeButton, fourPlayersModeButton, goBack);
        buttonsMenuVBox.setAlignment(Pos.CENTER);

        // Adding the VBox in the center of the BorderPane
        root.setTop(titleGameMode);
        root.setCenter(buttonsMenuVBox);
        BorderPane.setAlignment(titleGameMode, Pos.CENTER);
    
        this.setNewGameMenuScene(new Scene(root));
        this.getNewGameMenuScene().getStylesheets().add("styleGameMode.css");
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
            this.setGame(new GameFX(players,20, 9, 9, playersPawnIndex, 2));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Create a new game scene, ready to by used py the players
     * @throws GameNotInitializedException If the game is not initialised properly
     */
    public void createGameScene() throws GameNotInitializedException {
        if(this.getGame() == null) {
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

        this.setGameSkipTurnButton(new Button("Skip"));
        this.getGameSkipTurnButton().setVisible(false);
        this.getGameSkipTurnButton().setOnAction(e -> {
            this.getGame().setIsEndTurn(true);
            this.getGameSkipTurnButton().setVisible(false);

            checkEndTurn(this.getGame());

            //update button
            this.getActionButton().fire();
            if (!(this.getActionButton().getText().equals("Place fence"))) {
                this.getActionButton().fire();
            }
        });
        
        HBox buttonsHBox = new HBox();
        buttonsHBox.setAlignment(Pos.CENTER_LEFT);
        buttonsHBox.getChildren().addAll(this.getActionButton(), saveButton, goBack, this.getFenceCounter(), this.getGameSkipTurnButton());
        this.setGPane(createBoard(50,8,Color.LIGHTGRAY, Color.rgb(230, 230, 230)));

        rootGameScene.setCenter(this.getGPane());
        rootGameScene.setTop(buttonsHBox);
        this.setGameScene(new Scene(rootGameScene));

        // When it's the end of a game, change the user interface
        this.getGame().getIsEndGame().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Button newGameButton = new Button("New game");
                newGameButton.setOnAction(e -> {
                    prepareGameScene(this.getGame().getNbPlayers());
                });

                Text winner = null;
                try {
                    winner = new Text("Winner is " + this.getGame().getBoard().getPawn(this.getGame().getBoard().getWinner()).getColor().toString());
                } catch (IncorrectPawnIndexException e1) {
                    System.err.println(e1.getMessage());
                }
                
                this.getGPane().setDisable(true);
                buttonsHBox.getChildren().clear();
                buttonsHBox.getChildren().addAll(winner,newGameButton,goBack);    
                buttonsHBox.setStyle("-fx-alignment: center;");  
                buttonsHBox.setSpacing(10);

            } 
        });

        Text currentPlayerText = new Text();
        CurrentPlayerTextControl currentPlayerTextControl = new CurrentPlayerTextControl(this.game, currentPlayerText);
        this.getGame().addObserver(currentPlayerTextControl);
        buttonsHBox.getChildren().add(currentPlayerText);
        //We click on the button two times for update the first player action
        this.getActionButton().fire();
        this.getActionButton().fire();
        //Create a thread to run in the terminal
        if(this.getTerminalThread() != null && this.getTerminalThread().isAlive()) {
            this.getTerminalThread().interrupt();
        }
        this.setTerminalThread(new Thread(() -> {
            this.getGame().launch();
        }));
        this.getTerminalThread().setDaemon(true);
        this.getTerminalThread().start();
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
        if(this.getGameScene() == null) {
            try {
                createGameScene();
            } catch (GameNotInitializedException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText(""+e);
                alert.showAndWait();
                goToMainMenuScene();
            }
        }

        this.getPrimaryStage().setScene(this.getGameScene());
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
        cellStackPane.setOnMouseEntered(new HoverBorderControl(this, this.getFence()));
        cellStackPane.setOnMouseExited(new HoverBorderControl(this, this.getFence()));
        cellStackPane.setOnMouseClicked(new ClickCellControl(this, this.getFence(), this.getGame(), this.getActionButton()));
        cellStackPane.getChildren().add(cell);       

        // Check if there is a pawn on this cell and add it if there is one
        Pawn pawn = this.getGame().getBoard().getPawnAtPos(CYPathFX.gPaneCellCoordToGameCoord(col,row));
        if(this.getGame().getBoard().getPawnAtPos(CYPathFX.gPaneCellCoordToGameCoord(col,row)) != null) {
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
        int sizeBoardRows = this.getGame().getBoard().getNbRows();
        int sizeBoardColumns = this.getGame().getBoard().getNbCols();
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
        for(Fence f : this.getGame().getBoard().getFencesArray()) {
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
        Label label = new Label(this.getGame().getCurrentPawn().getColor().toString() + " can't move. Try to place a fence or skip your turn");
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
            possibleMoves = this.getGame().getBoard().listPossibleMoves(this.getGame().getBoard().getPawn(pawnId).getPosition());

            if(possibleMoves.isEmpty() && !this.getGame().getIsEndTurn()){

                System.out.println(this.getGame().getCurrentPawn().getColor().toString() + " pawn can't move");
                showPopupWindow(this.getPrimaryStage());
                this.getGameSkipTurnButton().setVisible(true);
            }

        } catch(IncorrectPawnIndexException err) {
            System.err.println(err.getMessage());
            System.exit(-1);
        }
        
        for(Point p : possibleMoves){
            StackPane stack = getCellStackPane(this.getGPane(), p.getY()*2+1, p.getX()*2+1);
            if(stack == null) {
                throw new IncorrectJavaFXBoardException();
            }
            ObservableList<Node> children = stack.getChildren();
            int lastIndex = children.size() - 1;
            Node node = children.get(lastIndex);

            if(node instanceof Rectangle){
                Rectangle rec = (Rectangle) node;
                rec.setFill(this.getGame().getCurrentPawn().getColor().toColorPossibleMove());
                this.getPreviousPossibleCells().add(rec);
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

        while (!this.getPreviousPossibleCells().isEmpty()) {
            Rectangle rec = this.getPreviousPossibleCells().getFirst();
            rec.setFill(cellColor);
            this.getPreviousPossibleCells().removeFirst();
        }
    }

    /**
     * Load a game from a file
     */
    private void loadGame()  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.json"));
        new File(this.getSaveDefaultPath()).mkdirs();
        fileChooser.setInitialDirectory(new File(this.getSaveDefaultPath()));

        File file = fileChooser.showOpenDialog(this.getPrimaryStage());

        
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

                this.setGame(new GameFX(players, loadDataObject.getMaxNbFences(), loadDataObject.getRows(), loadDataObject.getColumns(), playersPawnIndex, loadDataObject.getPawns(), loadDataObject.getCurrentPawnIndex(), 2));
                for(Fence f : loadDataObject.getListFences()){
                    this.getGame().getBoard().addFenceToData(f);
                }

                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Game successfully loaded");
                alert.showAndWait();
                this.createGameScene();
                this.goToGameScene();
            } catch (Exception e) {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error while loading the game.\n"+e);
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
        new File(this.getSaveDefaultPath()).mkdirs();
        fileChooser.setInitialDirectory(new File(this.getSaveDefaultPath()));
        fileChooser.setInitialFileName("save.json");

        File file = fileChooser.showSaveDialog(this.getPrimaryStage());
        
        if(file != null) {
            SaveDataInJSONFile saveDataObject = new SaveDataInJSONFile(this.getGame().getBoard().getNbRows(), this.getGame().getBoard().getNbCols(), this.getGame().getBoard().getFencesArray(), this.getGame().getNbMaxTotalFences(), this.getGame().getBoard().getPawnsArray(), this.getGame().getCurrentPawnIndex());
            Alert alert = null;
            try {
                saveDataObject.save(file, true);
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Game saved");
                alert.showAndWait();
            } catch (Exception e) {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error while saving the game.\n"+e);
                alert.showAndWait();
            }
        }
    }

    /**
     * Set the action button.
     * @param actionButton Button of action.
     */
    public void setActionButton(Button actionButton) {
        this.actionButton = actionButton;
    }

    /**
     * Set the game.
     * @param game Game wanting to be changed.
     */
    public void setGame(GameFX game) {
        this.game = game;
    }

    /**
     * Set the GridPane used to display the grid to the user.
     * @param gPane GridPane to be set to show the grid.
     */
    public void setGPane(GridPane gPane) {
        this.gPane = gPane;
    }

    /**
     * Get the fence whether it's to be placed or to highlight possible fence placement.
     * @return Fence that it's used by the player.
     */
    public Fence getFence() {
        return fence;
    }

    /**
     * Set the fence used by the player.
     * @param fence Fence used by the player.
     */
    public void setFence(Fence fence) {
        this.fence = fence;
    }

    /**
     * Set the list of the fences, represented by Line, used to show to the player where it can place a fence.
     * @param prevHighlightedFencesList List the previous highlighted fences shown to the player when placing a fence.
     */
    public void setPrevHighlightedFencesList(LinkedList<Line> prevHighlightedFencesList) {
        this.prevHighlightedFencesList = prevHighlightedFencesList;
    }

    /**
     * Set the list of possible cells to move on before moving to another a cell.
     * @param previousPossibleCells List of cells where you move on.
     */
    public void setPreviousPossibleCells(LinkedList<Rectangle> previousPossibleCells) {
        this.previousPossibleCells = previousPossibleCells;
    }

    /**
     * Set the color of cells you can hover
     * @param cellColorHover Color of the cells you can hover
     */
    public void setCellColorHover(Color cellColorHover) {
        this.cellColorHover = cellColorHover;
    }

    /**
     * Set the text of the fence counter for a player
     * @param fenceCounter Text of the fence counter
     */
    public void setFenceCounter(Text fenceCounter) {
        this.fenceCounter = fenceCounter;
    }

    /**
     * Get the terminal thread running behind the graphical interface
     * @return Thread of the console application
     */
    public Thread getTerminalThread() {
        return terminalThread;
    }

    /**
     * Set the terminal thread running behind the graphical interface
     * @param terminalThread Thread running behind the graphical interface
     */
    public void setTerminalThread(Thread terminalThread) {
        this.terminalThread = terminalThread;
    }

    /**
     * Get the primary stage of the graphical application's window
     * @return Stage where the main interaction is being done
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Set the primary stage of the graphical application's window
     * @param primaryStage Stage where the main interaction is being done
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Set the scene of the main menu when launching the application.
     * @return Scene of the main menu
     */
    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    /**
     * Set the scene of the main menu when launching the application.
     * @param mainMenuScene Scene of the main menu.
     */
    public void setMainMenuScene(Scene mainMenuScene) {
        this.mainMenuScene = mainMenuScene;
    }

    /**
     * Get the scene when creating a new game.
     * @return Scene when creating a new game.
     */
    public Scene getNewGameMenuScene() {
        return newGameMenuScene;
    }

    /**
     * Set the scene when creating a new game.
     * @param newGameMenuScene Scene when creating a new game.
     */
    public void setNewGameMenuScene(Scene newGameMenuScene) {
        this.newGameMenuScene = newGameMenuScene;
    }

    /**
     * Get the scene of the game.
     * @return Scene of the game.
     */
    public Scene getGameScene() {
        return gameScene;
    }

    /**
     * Set the scene of the game.
     * @param gameScene Scene of the game.
     */
    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    /**
     * Get the button to continue a game. This button can be found on the main menu.
     * @return Button to continue a game.
     */
    public Button getContinueGameButton() {
        return continueGameButton;
    }

    /**
     * Set the button to continue a game. This button can be found on the main menu.
     * @param continueGameButton Button to continue a game.
     */
    public void setContinueGameButton(Button continueGameButton) {
        this.continueGameButton = continueGameButton;
    }

    /**
     * Get the button to skip a turn when no there are no possible actions.
     * @return Button for skipping a turn when no there are no possible actions.
     */
    public Button getGameSkipTurnButton() {
        return gameSkipTurnButton;
    }

    /**
     * Set the button to skip a turn when no there are no possible actions.
     * @param gameSkipTurnButton Button for skipping a turn when no there are no possible actions.
     */
    public void setGameSkipTurnButton(Button gameSkipTurnButton) {
        this.gameSkipTurnButton = gameSkipTurnButton;
    }


    /**
     * Get the default saving path when creating a new save.
     * @return String of the default saving path.
     */
    public String getSaveDefaultPath() {
        return saveDefaultPath;
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