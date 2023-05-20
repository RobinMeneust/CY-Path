import java.io.File;
import java.util.LinkedList;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class CYPathFX extends Application {
    private Button actionButton;
    private Button loadButton;
    private Button saveButton;
    private GameFX game;
    private GridPane gPane;
    private HBox buttonsHBox;
    private Orientation fenceOrientation;
    private LinkedList<Line> prevHighlightedFencesList;
    private LinkedList<Rectangle> previousPossibleCells = new LinkedList<Rectangle>();
    private Color possibleCellColor;
    private Color cellColorHover;
    private boolean moveMode;
    private TextField fenceCounter;

    private Thread terminalThread;
    
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene newGameMenuScene;
    private Scene loadGameScene;
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
        this.actionButton.setOnAction(new ActionButtonHandler());
        this.loadButton = new Button("Load");
        this.saveButton = new Button("Save");
        this.loadButton.setOnAction(e -> openFileChooser(primaryStage, "Load"));
        this.saveButton.setOnAction(e -> openFileChooser(primaryStage, "Save"));
        this.mainMenuScene = null;
        this.newGameMenuScene = null;
        this.loadGameScene = null;
        this.gameScene = null;
        this.primaryStage = primaryStage;
        this.terminalThread = null;
        this.fenceCounter = new TextField("test");
        this.fenceCounter.setEditable(false);
        this.fenceCounter.setPrefColumnCount(2);


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

        Button loadGameMenuButton = new Button("Load Game");
        
        buttonsMenuHBox.getChildren().addAll(newGameMenuButton, loadGameMenuButton);
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
        root.setLeft(twoPlayersModeButton);
        root.setRight(fourPlayersModeButton);
        
        this.newGameMenuScene = new Scene(root);
    }
    
    public void createLoadGameScene() {
        BorderPane root = new BorderPane();
        
        // TODO: A file chooser should be added here
        
        this.loadGameScene = new Scene(root);
    }

    public void goToLoadGameScene() {
        if(this.loadGameScene == null) {
            createLoadGameScene();
        }

        this.primaryStage.setScene(this.loadGameScene);
    }

    public void createGameScene(int nbPlayers) {
        BorderPane rootGameScene = new BorderPane();
        this.gPane = createBoard();
        buttonsHBox.getChildren().addAll(actionButton, loadButton, saveButton);

        rootGameScene.setCenter(this.gPane);
        rootGameScene.setTop(buttonsHBox);
        gameScene = new Scene(rootGameScene);


        // Initialize game

        Player[] players = new Player[nbPlayers];
        for (int i = 0; i < nbPlayers; i++){
            players[i] = new Player("Anonymous player" + i);
        }
        try {
            this.game = new GameFX(players,20, 9, 9);
        } catch (InvalidNumberOfPlayersException e) {
            System.err.println(e);
            System.exit(-1);
        }
        actionButton.textProperty().bind(CYPathFX.this.game.getAction());

        //We click on the button two times for update the first player action
        actionButton.fire();
        actionButton.fire();

        //Create a thread to run in the terminal
        if(this.terminalThread != null && this.terminalThread.isAlive()) {
            this.terminalThread.interrupt();
        }
        this.terminalThread = new Thread(() -> runInTerminal());
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
    
        gPane.setOnScroll(new ChangeFenceOrientation());
    
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
                cell.setOnMouseEntered(new HoverBorder());
                cell.setOnMouseExited(new HoverBorder());
                cell.setOnMouseClicked(new ClickAddBorder());
                cellStackPane.getChildren().add(cell);
                System.out.println("column = " + j +" ligne = " + i);                

                // Add player circles to the middle of each side
                if( j == sizeBoardColumns && (i == 1 )) {
                    Circle player1Circle = createPlayerCircle(Color.RED);
                    cellStackPane.getChildren().add(player1Circle);
                } else if (j == sizeBoardColumns && (i == sizeBoardRows * 2 - 1)) {
                    Circle player2Circle = createPlayerCircle(Color.BLUE);
                    cellStackPane.getChildren().add(player2Circle);
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
    
    private Circle createPlayerCircle(Color color) {
        Circle circle = new Circle(15, color);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        return circle;
    }

    public void addCircleToCell(GridPane gridPane, int rowIndex, int columnIndex, Color color) {
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
        System.out.println("SUPPRR column index = " + columnIndex  + "row index = " + rowIndex);
        System.out.println("Circle en cours de suppression");
        if(stack != null) {
            stack.getChildren().removeIf(node -> node instanceof Circle);
            System.out.println("Circle supprimé");
        }
    }

    class ClickAddBorder implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Object o = event.getSource();
            if(o instanceof Rectangle) {
                Rectangle sourceCell = (Rectangle) o;
                if(!CYPathFX.this.isMoveMode() && CYPathFX.this.prevHighlightedFencesList != null){
                    //Update data   
                    Fence fence = null;
                    Point pStartCell = new Point(0,0);
                    Point pStartFenceCoord = new Point(0,0);

                    // Convert from grid coordinates to fence coordinates
                    pStartCell.setX(GridPane.getColumnIndex(sourceCell));
                    pStartCell.setY(GridPane.getRowIndex(sourceCell));

                    pStartFenceCoord.setX((pStartCell.getX()-1)/2);
                    pStartFenceCoord.setY((pStartCell.getY()-1)/2);


                    fence = new Fence(CYPathFX.this.game.getBoard().getFenceLength(), CYPathFX.this.getFenceOrientation(), pStartFenceCoord);

                    try{
                        if(CYPathFX.this.game.getBoard().placeFence(CYPathFX.this.game.getCurrentPlayerIndex(), fence)) {
                            // Add fence to the gridPane
                            for(Line l : CYPathFX.this.prevHighlightedFencesList) {
                                l.setStroke(Color.BLACK);
                                l.toFront();
                                // Clear the prevHighlightedFencesList so that the color isn't removed when the mouse is moved
                            }
                            CYPathFX.this.prevHighlightedFencesList.clear();
                            


                            CYPathFX.this.game.setIsEndTurn(true);

                            //We wait the begining of the next turn
                            while (CYPathFX.this.game.getIsEndTurn()) {
                                try {
                                    Thread.sleep(100); //Wait 100 milliseconds before checking again
                                } catch (InterruptedException ev) {
                                    ev.printStackTrace();
                                }
                            }
                            //update button
                            actionButton.fire();
                        } else {
                            System.out.println("The fence can't be placed here (Starting point:" + fence.getStart() + ").\nTry again.");
                        }
                    } catch (IncorrectPawnIndexException e) {
                        System.err.println("ERROR: Pawn index is incorrect. Check the number of players and the number of pawns and see if they are equals");
                        System.exit(-1);
                    }
                } else if(CYPathFX.this.isMoveMode() && CYPathFX.this.previousPossibleCells != null && CYPathFX.this.previousPossibleCells.contains(sourceCell)) {
                    try {
                        Pawn pawn = CYPathFX.this.game.getBoard().getPawn(CYPathFX.this.game.getCurrentPlayerIndex());

                        removeCircleFromCell(CYPathFX.this.gPane, pawn.getPosition().getY() * 2 + 1, pawn.getPosition().getX() * 2 + 1);

                        StackPane parentStackPane = (StackPane) sourceCell.getParent(); // Récupérer le StackPane parent
                        int columnIndex = GridPane.getColumnIndex(parentStackPane); // Obtenir l'indice de colonne du StackPane
                        int rowIndex = GridPane.getRowIndex(parentStackPane); // Obtenir l'indice de ligne du StackPane
                        pawn.setPosition(new Point(columnIndex / 2, rowIndex / 2));
                        
                        addCircleToCell(CYPathFX.this.gPane, rowIndex, columnIndex, Color.YELLOW);
                        //the information is transmitted to the terminal
                        CYPathFX.this.game.setIsEndTurn(true);
                        //We wait the begining of the next turn
                        while (CYPathFX.this.game.getIsEndTurn()) {
                            try {
                                Thread.sleep(100); //Wait 100 milliseconds before checking again
                            } catch (InterruptedException ev) {
                                ev.printStackTrace();
                            }
                        }
                        //update button
                        actionButton.fire();
                        if(actionButton.getText() != "Move"){
                            actionButton.fire();
                        }

                    } catch(IncorrectPawnIndexException err) {
                        System.err.println(err);
                        System.exit(-1);
                    }
                }
            }
        }
    }

    class HoverBorder implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event){
            Object o = event.getSource();
            if(o instanceof Rectangle) {
                Rectangle sourceCell = (Rectangle) o;
                if(!CYPathFX.this.isMoveMode()) {
                    if(event.getEventType() == MouseEvent.MOUSE_ENTERED) {
                        Point pStartCell = new Point(0,0);
                        Point pStartFenceCoord = new Point(0,0);

                        // Convert from grid coordinates to fence coordinates
                        pStartCell.setX(GridPane.getColumnIndex(sourceCell));
                        pStartCell.setY(GridPane.getRowIndex(sourceCell));

                        pStartFenceCoord.setX((pStartCell.getX()-1)/2);
                        pStartFenceCoord.setY((pStartCell.getY()-1)/2);


                        if(CYPathFX.this.getFenceOrientation() == Orientation.HORIZONTAL) {
                            Fence fence = new Fence(CYPathFX.this.game.getBoard().getFenceLength(), Orientation.HORIZONTAL, pStartFenceCoord);
                            if(CYPathFX.this.game.getBoard().isFencePositionValid(fence)) {
                                // we take the row above the cell
                                int y = pStartCell.getY() - 1; 
                                for(int i=pStartCell.getX(); i<pStartCell.getX()+2*fence.getLength(); i+=2) {
                                    Node n = getNodeFromGridPane(CYPathFX.this.gPane, y,i);
                                    if(n instanceof Line) {
                                        Line l = (Line) n;
                                        if(l.getStroke() != Color.BLACK){
                                            // If it's not already a border
                                            l.setStroke(Color.DARKGREEN);
                                            l.toFront();
                                            CYPathFX.this.prevHighlightedFencesList.add(l);
                                        }
                                    }
                                }
                            } else {
                                Node n = getNodeFromGridPane(CYPathFX.this.gPane, GridPane.getRowIndex(sourceCell)-1, GridPane.getColumnIndex(sourceCell)); // get the upper border of the cell
                                if(n instanceof Line) {
                                    Line l = (Line) n;
                                    if(l.getStroke() != Color.BLACK){
                                        // If it's not already a border
                                        l.setStroke(Color.DARKRED);
                                        l.toFront();
                                        CYPathFX.this.prevHighlightedFencesList.add(l);
                                    }
                                }
                            }
                        } else if(CYPathFX.this.getFenceOrientation() == Orientation.VERTICAL) {
                            Fence fence = new Fence(CYPathFX.this.game.getBoard().getFenceLength(), Orientation.VERTICAL, pStartFenceCoord);
                            if(CYPathFX.this.game.getBoard().isFencePositionValid(fence)) {
                                int x = pStartCell.getX() - 1;
                                for(int i=pStartCell.getY(); i<pStartCell.getY()+2*fence.getLength(); i+=2) {
                                    Node n = getNodeFromGridPane(CYPathFX.this.gPane, i, x);
                                    if(n instanceof Line) {
                                        Line l = (Line) n;
                                        if(l.getStroke() != Color.BLACK){
                                            // If it's not already a border
                                            l.setStroke(Color.DARKGREEN);
                                            l.toFront();
                                            CYPathFX.this.prevHighlightedFencesList.add(l);
                                        }
                                    }
                                }
                            } else {
                                Node n = getNodeFromGridPane(CYPathFX.this.gPane, GridPane.getRowIndex(sourceCell), GridPane.getColumnIndex(sourceCell)-1); // get the left border of the cell
                                if(n instanceof Line) {
                                    Line l = (Line) n;
                                    if(l.getStroke() != Color.BLACK){
                                        // If it's not already a border
                                        l.setStroke(Color.DARKRED);
                                        l.toFront();
                                        CYPathFX.this.prevHighlightedFencesList.add(l);
                                    }
                                }
                            }
                        }
                    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED){
                        if(CYPathFX.this.prevHighlightedFencesList != null){
                            for(Line l : CYPathFX.this.prevHighlightedFencesList) {
                                if(l.getStroke() != Color.BLACK){
                                    // If it's not already a border
                                    l.setStroke(Color.LIGHTGRAY);
                                    l.toBack();
                                }
                            }
                            CYPathFX.this.prevHighlightedFencesList.clear();
                        }
                    }
                } else if(CYPathFX.this.previousPossibleCells != null && CYPathFX.this.previousPossibleCells.contains(sourceCell)) {
                    if(event.getEventType() == MouseEvent.MOUSE_ENTERED) {
                        sourceCell.setFill(cellColorHover);
                    } else if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
                        sourceCell.setFill(possibleCellColor);
                    }
                }
            }
        }
    }


    /**
	 * Get a specific node from the GridPane.
	 * 
	 * @param gridPane is the main pane.
	 * @param row the row from the node we want.
     * @param col the column from the node we want.
	 * @return The specific node from the GridPane we were looking for.
	 * 
	 */

    private Node getNodeFromGridPane(GridPane gridPane, int row, int col) {
        
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

    class ChangeFenceOrientation implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent event) {
            if(!CYPathFX.this.isMoveMode()){
                if(CYPathFX.this.getFenceOrientation() == Orientation.HORIZONTAL) {
                    CYPathFX.this.setFenceOrientation(Orientation.VERTICAL);
                } else {
                    CYPathFX.this.setFenceOrientation(Orientation.HORIZONTAL);
                }
            }
        }
        
    }

    /**
	 * Event that determined the player's choice between "Place a fence" and "Move".
	 */
    class ActionButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
            //If the current player have fence
            CYPathFX.this.resetPossibleCells(CYPathFX.this.game.getCurrentPlayerIndex()); // Use PAC model to consider the game tracking
            try {
                if(actionButton.getText() == "Move" && CYPathFX.this.game.getBoard().getPawn(CYPathFX.this.game.getCurrentPlayerIndex()).getAvailableFences() > 0){
                    CYPathFX.this.game.setAction("Place fence");
                    CYPathFX.this.setMoveMode(false);
                }
                else{
                    CYPathFX.this.showPossibleCells(CYPathFX.this.game.getCurrentPlayerIndex());
                    CYPathFX.this.game.setAction("Move");
                    CYPathFX.this.setMoveMode(true);
                }

                //Update fenceCounter
                CYPathFX.this.fenceCounter.setEditable(true);
                CYPathFX.this.fenceCounter.setText(""+CYPathFX.this.game.getBoard().getPawn(CYPathFX.this.game.getCurrentPlayerIndex()).getAvailableFences());
                CYPathFX.this.fenceCounter.setEditable(false);
                
            } catch(IncorrectPawnIndexException e) {
                System.err.println("ERROR: Pawn index is incorrect. Check the number of players and the number of pawns and see if they are equals");
                System.exit(-1);
            }
        }
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
            System.out.println(p.getX() + "," + p.getY());
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
	 * @param pawnId Int represanting the ID of the player.
	 * 
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



/**
	 * Open a file chooser to save or load a game.
	 * @param primaryStage Main stage
     * @param action "Load" or "Save" to dertermine what the file chooser has to do.
	 */
    private void openFileChooser(Stage primaryStage, String action) {
        FileChooser fileChooser = new FileChooser();

         fileChooser.setTitle("Select Some Files");

         fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        if (action.equals("Load")) {
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
            fileChooser.showOpenDialog(primaryStage);
            
        } else if (action.equals("Save")) {
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
            fileChooser.showSaveDialog(primaryStage);
        }

        System.out.println("Action: " + action);
    }
    /*//JavaFX
    private void runInJavaFX(){
        
    }*/
    //Terminal
    private void runInTerminal() {
        try {
            CYPathFX.this.game.launch();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}