package presentation;

import java.io.File;
import java.util.HashMap;

/**
 * Importing java classes needed for the CYPathFX class
 */

import java.util.LinkedList;
import java.util.Optional;

/**
 * Importing javafx classes needed for the CYPathFX class
 */

import abstraction.*;
import control.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("deprecation")
public class CYPathFX extends Application {
    /**
     * State the CYPATH's class attributes
     */
    
    public Button actionButton;
    public GameFX game;
    public GridPane gPane;
    private Fence fence;
    public LinkedList<Line> prevHighlightedFencesList;
    public LinkedList<Rectangle> previousPossibleCells = new LinkedList<Rectangle>();
    public Color cellColorHover;
    private boolean moveMode;
    public Text fenceCounter;

    private Thread terminalThread;
    
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene newGameMenuScene;
    private Scene gameScene;

    private Button continueGameButton;

    //JavaFX
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

    public void createMainMenuScene() {
        BorderPane rootMainMenu = new BorderPane();
        this.mainMenuScene = new Scene(rootMainMenu);
        
        HBox buttonsMenuHBox = new HBox();
        rootMainMenu.setCenter(buttonsMenuHBox);

        Button newGameMenuButton = new Button("New Game");
        newGameMenuButton.setOnAction(e -> goToNewGameMenu());

        Button loadButton = new Button("Load");

        loadButton.setOnAction(e -> loadGame());


        buttonsMenuHBox.getChildren().addAll(newGameMenuButton, loadButton, continueGameButton);
    }

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

    public void goToNewGameMenu() {
        if (this.newGameMenuScene == null) {
            createNewGameScene();
        }

        this.primaryStage.setScene(this.newGameMenuScene);
    }

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

        HBox buttonsHBox = new HBox();
        buttonsHBox.getChildren().addAll(actionButton, saveButton, goBack, fenceCounter);
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
    
    public boolean isMoveMode() {
        return moveMode;
    }

    public void setMoveMode(boolean moveMode) {
        this.moveMode = moveMode;
    }

    public Line createLineBorder(int xStart, int yStart, int xEnd, int yEnd, Color color, int lineWidth) {
        Line border = new Line(xStart,yStart,xEnd,yEnd);
        border.setStrokeWidth(lineWidth);
        border.setStroke(color);
        return border;
    }

    public GridPane createBoard() {
        GridPane gPane = new GridPane();
        int sizeBoardRows = this.game.getBoard().getNbRows();
        int sizeBoardColumns = this.game.getBoard().getNbCols();
        Rectangle cell = null;
        Line border = null;
        int lineLength = 50;
        int cellSize = 50;
        int lineWidth = 8;
        int lineLengthBorders = lineLength + lineWidth;
        Color borderColor = Color.LIGHTGRAY;
        Color cellColor = Color.rgb(230, 230, 230);
        Circle[] pawnCircles = new Circle[this.game.getNbPlayers()];

        try {
            for (int k = 0; k < this.game.getNbPlayers(); k++) {
                pawnCircles[k] = this.createPlayerCircle(this.game.getBoard().getPawn(k).getColor());
            }
        }catch (IncorrectPawnIndexException err){
            err.printStackTrace();
        }

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
                // Cells
                StackPane cellStackPane = new StackPane(); // Create a new StackPane for every cell

                // Cells
                cell = new Rectangle(cellSize, cellSize);
                cell.setFill(cellColor);
                cellStackPane.setOnMouseEntered(new HoverBorderControl(this, this.fence));
                cellStackPane.setOnMouseExited(new HoverBorderControl(this, this.fence));
                cellStackPane.setOnMouseClicked(new ClickCellControl(this, this.fence, this.game, this.actionButton));
                cellStackPane.getChildren().add(cell);              

                // Add player circles to their position
                try {
                    for (int l = 0; l < this.game.getBoard().getNbPawns(); l++) {
                        if (this.game.getBoard().getPawn(l).getPosition().equals(new Point((j-1)/2, (i-1)/2))) {
                            cellStackPane.getChildren().add(pawnCircles[l]);
                        }
                    }
                }catch (IncorrectPawnIndexException err){
                    err.printStackTrace();
                }

                gPane.add(cellStackPane, j, i);
    
                // Vertical borders
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

    private Circle createPlayerCircle(ColorPawn colorP) {
        Circle circle = new Circle(15, colorP.toColorFX());
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        return circle;
    }

    public void addCircleToCell(GridPane gridPane, int rowIndex, int columnIndex, ColorPawn color) {
        StackPane stack = getCellStackPane(gridPane, rowIndex, columnIndex);
        if(stack != null) {
            Circle circle = createPlayerCircle(color);
            stack.getChildren().add(circle);
        }
    }
    
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

    public StackPane getCellStackPane(GridPane gridPane, int row, int column) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row && node instanceof StackPane) {
                return (StackPane) node;
            }
        }
        return null;
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

    public static Point gameCoordToGPaneCoord(Node node) {
        Point coord = new Point(0, 0);
        coord.setX(GridPane.getColumnIndex(node));
        coord.setY(GridPane.getRowIndex(node));

        return coord;
    }

    public static Point gPaneCoordToGameCoord(Point gPaneCoord) {   
        Point coord = new Point(0, 0);
        coord.setX(gPaneCoord.getX() / 2);
        coord.setY(gPaneCoord.getY() / 2);
        
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
                this.goToGameScene();
            } catch (Exception e) {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error while loading the game.\n\n"+e);
                e.printStackTrace();
                alert.showAndWait();
            }
        }
    }

    private void saveGame() {
        TextInputDialog dialog = new TextInputDialog("save");
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
                saveDataObject.save(fileName);
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Game saved");
                alert.showAndWait();
            } catch(Exception e) {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error while saving the game. Please check if the file already exists in resources/data/saves");
                alert.showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}