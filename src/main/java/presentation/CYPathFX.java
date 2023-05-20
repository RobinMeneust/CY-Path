package presentation; /**
 * Importing java classes needed for the presentation.CYPathFX class
 */

import java.io.File;
import java.util.LinkedList;

/**
 * Importing javafx classes needed for the presentation.CYPathFX class
 */

import abstraction.*;
import control.ActionButtonControl;
import control.ClickAddBorderControl;
import control.FenceOrientationControl;
import control.HoverBorderControl;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class CYPathFX extends Application {
    /**
     * State the CYPATH's class attributes
     */
    
    private Button actionButton;
    public GameFX game;
    public GridPane gPane;
    private HBox buttonsHBox;
    private Orientation fenceOrientation;
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
        this.fenceOrientation = Orientation.HORIZONTAL;
        this.gPane = null;
        this.buttonsHBox = new HBox(3);
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

        loadButton.setOnAction(e -> {
            openFileChooser(this.primaryStage, "Load");
        });
        

        
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
        saveButton.setOnAction(e -> openFileChooser(this.primaryStage, "Save"));

        buttonsHBox.getChildren().addAll(actionButton, saveButton, goBack, fenceCounter);

        

        // Initialize game

        Player[] players = new Player[nbPlayers];
        for (int i = 0; i < nbPlayers; i++){
            players[i] = new Player("Anonymous player" + i);
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
        actionButton.textProperty().bind(CYPathFX.this.game.getAction());

        //We click on the button two times for update the first player action
        actionButton.fire();
        actionButton.fire();

        //Create a thread to run in the terminal
        if(this.terminalThread != null && this.terminalThread.isAlive()) {
            this.terminalThread.interrupt();
        }
        this.terminalThread = new Thread(() -> {
            CYPathFX.this.game.launch();
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
        int sizeBoardRows = 9;
        int sizeBoardColumns = 9;
        Rectangle cell = null;
        Line border = null;
        int lineLength = 50;
        int cellSize = 50;
        int lineWidth = 8;
        int lineLengthBorders = lineLength + lineWidth;
        Color borderColor = Color.LIGHTGRAY;
        Color cellColor = Color.rgb(230, 230, 230);
    
        gPane.setOnMouseClicked(new FenceOrientationControl(this));
    
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
                StackPane cellStackPane = new StackPane(); // Créer un nouveau StackPane pour chaque cellule

                // Cells
                cell = new Rectangle(cellSize, cellSize);
                cell.setFill(cellColor);
                cellStackPane.setOnMouseEntered(new HoverBorderControl(this));
                cellStackPane.setOnMouseExited(new HoverBorderControl(this));
                cellStackPane.setOnMouseClicked(new ClickAddBorderControl(this, this.game, this.actionButton));
                cellStackPane.getChildren().add(cell);
                //System.out.println("column = " + j +" ligne = " + i);                

                // Add player circles to the middle of each side
                try{
                    // TOP
                    if(j == sizeBoardColumns && (i == sizeBoardRows * 2 - 1)) {
                        Circle player1Circle = createPlayerCircle(this.game.getBoard().getPawn(0).getColor()); 
                        System.out.println("couleur joueur "+ this.game.getBoard().getPawn(0) +" = " + this.game.getBoard().getPawn(0).getColor());
                        cellStackPane.getChildren().add(player1Circle);

                        // BOTTOM
                    } else if (j == sizeBoardColumns && (i == 1 )) {
                        Circle player2Circle = createPlayerCircle(this.game.getBoard().getPawn(1).getColor());
                        System.out.println("couleur joueur " + this.game.getBoard().getPawn(1) + " = " + this.game.getBoard().getPawn(1).getColor());
                        cellStackPane.getChildren().add(player2Circle);
                    }

                    // LEFT
                    if(this.game.getNbPlayers() == 4 && j == 1 && (i == sizeBoardRows)) {
                        Circle player3Circle = createPlayerCircle(this.game.getBoard().getPawn(2).getColor());
                        cellStackPane.getChildren().add(player3Circle);
                        // RIGHT
                    } else if(this.game.getNbPlayers() == 4 && (j == sizeBoardColumns * 2 - 1) && (i == sizeBoardRows)){
                        Circle player4Circle = createPlayerCircle(this.game.getBoard().getPawn(3).getColor());
                        cellStackPane.getChildren().add(player4Circle);
                    }
                }catch(IncorrectPawnIndexException e){
                    System.out.println("erreur");
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
        Color color;
        switch(colorP){
            case YELLOW :
                color = Color.YELLOW;
                break;
            case BLUE :
                color = Color.BLUE;
                break;
            case GREEN :
                color = Color.GREEN;
                break;
            case RED :
                color = Color.RED;
                break;
            default :
                color = Color.BLACK; 
        }
        Circle circle = new Circle(15, color);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        return circle;
    }

    public void addCircleToCell(GridPane gridPane, int rowIndex, int columnIndex, ColorPawn color) {
        System.out.println("AJOUT column index = " + columnIndex + "row index = " + rowIndex);
        StackPane stack = getCellStackPane(gridPane, rowIndex, columnIndex);
        System.out.println("Circle en cours d'ajout");
        if(stack != null) {
            Circle circle = createPlayerCircle(color);
            stack.getChildren().add(circle);
            System.out.println("Circle ajouté");
        }
    }
    
    public void removeCircleFromCell(GridPane gridPane, int rowIndex, int columnIndex) {
        StackPane stack = getCellStackPane(gridPane, rowIndex, columnIndex);
        System.out.println("SUPPRR column index = " + columnIndex + "row index = " + rowIndex);
        System.out.println("Circle en cours de suppression");
        if(stack != null) {
            stack.getChildren().removeIf(node -> node instanceof Circle);
            System.out.println("Circle supprimé");
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

    public Orientation getFenceOrientation() {
        return fenceOrientation;
    }


    public void setFenceOrientation(Orientation fenceOrientation) {
        this.fenceOrientation = fenceOrientation;
    }


    /**
	 * Color all the cells that the player can move to.
     * Change the cell's color if the player hover.
	 * 
	 * @param pawnId Int representing the ID of the abstraction.Player.
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
            System.out.println(p.getX() + "," + p.getY());
            if( node instanceof Rectangle){
                Rectangle rec = (Rectangle) node;
                rec.setFill(possibleCellColor);
                this.previousPossibleCells.add(rec);

                //abstraction.Point previousPosition = this.game.getBoard().getPawn(pawnId).getPosition();
            }
        }
    }

    /**
	 * Reset previous possible cells to be updated with the game.
     * 
	 * @param pawnId Int represanting the ID of the player.
	 */

    public void resetPossibleCells(int pawnId){
        Color cellColor = Color.rgb(230, 230, 230);

        while (!previousPossibleCells.isEmpty()) {
            Rectangle rec = previousPossibleCells.getFirst();
            rec.setFill(cellColor);
            previousPossibleCells.removeFirst();
        }

        //System.out.println("Reset des couleurs");
    }



    private void loadGame(File file)  {
        // TODO
    }

    private void saveGame(File file) {
        // TODO
    }


    /**
	 * Open a file chooser to save or load a game.
     * 
	 * @param primaryStage Main stage
     * @param action "Load" or "Save" to dertermine what the file chooser has to do.
	 */
    private void openFileChooser(Stage primaryStage, String action) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select Some Files");

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        if(action.equals("Load")) {
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if(file != null) {
                loadGame(file);
            }
        } else if (action.equals("Save")) {
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if(file != null) {
                saveGame(file);
            }
        }


        //TODO: save and load the game in the given file

        System.out.println("Action: " + action);
    }


    public static void main(String[] args) {
        launch(args);
    }
}