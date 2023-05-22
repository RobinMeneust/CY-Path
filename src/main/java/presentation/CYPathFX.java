package presentation; /**
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
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

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
    public Color possibleCellColor;
    public Color cellColorHover;
    private boolean moveMode;
    public Text fenceCounter;

    private Thread terminalThread;
    
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene newGameMenuScene;
    private Scene gameScene;

    //JavaFX
    public void start(Stage primaryStage) throws Exception {
        this.possibleCellColor = Color.rgb(172, 255, 214);
        this.cellColorHover = Color.rgb(239,255,172);
        this.prevHighlightedFencesList = new LinkedList<Line>();
        this.moveMode = true;
        this.fence = new Fence(Orientation.HORIZONTAL);
        this.gPane = null;
        this.actionButton = new Button("Move");
        this.mainMenuScene = null;
        this.newGameMenuScene = null;
        this.gameScene = null;
        this.primaryStage = primaryStage;
        this.terminalThread = null;
        this.fenceCounter = new Text("0");

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

        if(this.gameScene != null) {
            Button continueGameButton = new Button("Continue current game");
            continueGameButton.setOnAction(e -> {
                try {
                    goToGameScene();
                    // The GameNotInitializedException should not be thrown here since gameScene is not null
                    // But even if it's thrown it's not an issue, we can just prevent the user from going back to its exited game and start a new one
                } catch (GameNotInitializedException err) {}
            });
            buttonsMenuHBox.getChildren().add(continueGameButton);
        }

        Button newGameMenuButton = new Button("New Game");
        newGameMenuButton.setOnAction(e -> goToNewGameMenu());

        Button loadButton = new Button("Load");

        loadButton.setOnAction(e -> loadGame());


        buttonsMenuHBox.getChildren().addAll(newGameMenuButton, loadButton);
    }

    public void goToMainMenuScene() {
        if(this.mainMenuScene == null) {
            createMainMenuScene();
        }
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

    public void createNewGameScene() {
        BorderPane root = new BorderPane();
        
        Button twoPlayersModeButton = new Button("2 Players");
        twoPlayersModeButton.setOnAction(e -> {
            try {    
                createGameScene(2);
                goToGameScene();
            } catch (GameNotInitializedException err) {
                System.err.println(err);
                System.exit(-1);
            }
        });
        Button fourPlayersModeButton = new Button("4 Players");
        fourPlayersModeButton.setOnAction(e -> {
            try {    
                createGameScene(4);
                goToGameScene();
            } catch (GameNotInitializedException err) {
                System.err.println(err);
                System.exit(-1);
            }
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

    public void createGameScene(int nbPlayers) {
        BorderPane rootGameScene = new BorderPane();
        
        Button goBack = new Button("Main Menu");
        goBack.setOnAction(e -> {
            goToMainMenuScene();
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveGame());

        HBox buttonsHBox = new HBox();
        buttonsHBox.getChildren().addAll(actionButton, saveButton, goBack, fenceCounter);

        // Initialize game

        Player[] players = new Player[nbPlayers];
        for (int i = 0; i < nbPlayers; i++){
            players[i] = new Player("Player" + i);
        }
        try {
            this.game = new GameFX(players,20, 9, 9);
            this.actionButton.setOnAction(new ActionButtonControl(this, this.actionButton, this.game));
        } catch (InvalidNumberOfPlayersException e) {
            System.err.println(e);
            System.exit(-1);
        }
        this.gPane = createBoard();
        rootGameScene.setCenter(this.gPane);
        rootGameScene.setTop(buttonsHBox);
        gameScene = new Scene(rootGameScene);
        actionButton.textProperty().bind(this.game.getAction());

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
            Platform.exit();
            System.exit(0);
        });
        this.terminalThread.setDaemon(true);
        this.terminalThread.start();
    }

    public void goToGameScene() throws GameNotInitializedException {
        if(this.gameScene == null) {
            throw new GameNotInitializedException();
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
            System.err.println(err);
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
                //System.out.println("column = " + j +" ligne = " + i);                

                // Add player circles to the middle of each side
                try {
                    for (int l = 0; l < this.game.getNbPlayers(); l++) {
                        if (this.game.getBoard().getPawn(l).getPosition().equals(new Point((j-1)/2, (i-1)/2))) {
                            cellStackPane.getChildren().add(pawnCircles[l]);
                        }
                    }
                }catch (IncorrectPawnIndexException err){
                    System.err.println(err);
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
    
        return gPane;
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
            System.err.println(err);
            System.exit(-1);
        }
        
        for( Point p : possibleMoves){
            StackPane stack = getCellStackPane(gPane, p.getY()*2+1, p.getX()*2+1);
            ObservableList<Node> children = stack.getChildren();
            int lastIndex = children.size() - 1;
            Node node = children.get(lastIndex);
            // System.out.println(p.getX() + "," + p.getY());
            if( node instanceof Rectangle){
                Rectangle rec = (Rectangle) node;
                rec.setFill(possibleCellColor);
                this.previousPossibleCells.add(rec);

                //Point previousPosition = this.game.getBoard().getPawn(pawnId).getPosition();
            }
        }
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
        // TODO
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
            SaveDataInJSONFile saveDataObject = new SaveDataInJSONFile(this.game.getBoard().getNbRows(), this.game.getBoard().getNbCols(), this.game.getBoard().getFencesArray(), this.game.getNbFences(), this.game.getBoard().getPawnsArray());
            Alert alert = null;
            if(saveDataObject.save(fileName)) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Game saved");
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setContentText("Error while saving the game. Please check if the file already exists in resources/data/saves");
            }
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}