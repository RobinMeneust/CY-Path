import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
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
    private boolean moveMode;

    //JavaFX
    public void start(Stage primaryStage) throws Exception {
        this.prevHighlightedFencesList = new LinkedList<Line>();
        this.moveMode = true;
        // Set up stage
        primaryStage.setTitle("CY Path : the Game");
        primaryStage.setResizable(false);

        fenceOrientation = Orientation.HORIZONTAL;
        this.gPane = createBoard();
        this.buttonsHBox = new HBox(3);
        actionButton = new Button("Move");
        actionButton.setOnAction(new ActionButtonHandler());
        this.loadButton = new Button("Load");
        this.saveButton = new Button("Save");
        loadButton.setOnAction(e -> openFileChooser(primaryStage, "Load"));
        saveButton.setOnAction(e -> openFileChooser(primaryStage, "Save"));
        BorderPane root = new BorderPane();
        buttonsHBox.getChildren().addAll(actionButton, loadButton, saveButton);

        root.setCenter(this.gPane);
        root.setTop(buttonsHBox);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        

        // Initialize game
        System.out.println("Welcome to CY-Path.\n");
        int nbPlayer = 0;
        Scanner sc = new Scanner(System.in);
        while(nbPlayer != 2 && nbPlayer != 4){
            System.out.println("How many players do you want ? (2 or 4)");
            nbPlayer = Integer.parseInt(sc.nextLine());
        }

        Player[] players= new Player[nbPlayer];
        for (int i = 0; i < nbPlayer; i++){
            players[i] = new Player("Anonymous player" + i);
        }

        this.game = new GameFX(players,20, 9, 9);
        actionButton.textProperty().bind(CYPathFX.this.game.getAction());

        //We click on the button two times for update the first player action
        actionButton.fire();
        actionButton.fire();

        //Create a thread to run in the terminal
        Thread terminalThread = new Thread(() -> runInTerminal());
        terminalThread.setDaemon(true);
        terminalThread.start();

        // Open the window
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
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
        for(int j=1; j<=2*sizeBoardColumns; j+=2) {
            border = createLineBorder(0,0,lineLengthBorders,0, borderColor, lineWidth);
            gPane.add(border,j,0);
        }
        gPane.getRowConstraints().add(new RowConstraints(lineWidth));
        
        for(int i=1; i<=2*sizeBoardRows; i+=2) {
            // First vertical border (left)
            border = createLineBorder(0,0,0,lineLengthBorders, borderColor, lineWidth);
            gPane.add(border,0,i);
            
            for(int j=1; j<=2*sizeBoardColumns; j+=2) {
                // Cells
                cell = new Rectangle(cellSize,cellSize);
                cell.setFill(cellColor);
                cell.setOnMouseEntered(new HoverBorder());
                cell.setOnMouseExited(new HoverBorder());
                gPane.add(cell,j,i);
                
                // Vertical borders
                border = createLineBorder(0,0,0,lineLength, borderColor, lineWidth);
                gPane.add(border,j+1,i);
            }
            
            // Horizontal borders
            for(int j=1; j<2*sizeBoardColumns; j+=2) {
                border = createLineBorder(0,0,lineLength, 0, borderColor, lineWidth);
                gPane.add(border,j,i+1);
            }
            // Horizontal borders
            gPane.getRowConstraints().add(new RowConstraints(cellSize));
            gPane.getRowConstraints().add(new RowConstraints(lineWidth));
        }
        
        gPane.getColumnConstraints().add(new ColumnConstraints(lineWidth));
        for(int i=0; i<sizeBoardColumns; i++) {
            gPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
            gPane.getColumnConstraints().add(new ColumnConstraints(lineWidth));
        }
        
        return gPane;
    }

    class HoverBorder implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event){
            if(!CYPathFX.this.isMoveMode()){
                Object o = event.getSource();
                if(o instanceof Rectangle) {
                    Rectangle sourceCell = (Rectangle) o;
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
                                int y = pStartCell.getY() - 1; // we take the row above the cell
                                for(int i=pStartCell.getX(); i<pStartCell.getX()+2*fence.getLength(); i+=2) {
                                    Node n = getNodeFromGridPane(CYPathFX.this.gPane, y,i);
                                    if(n instanceof Line) {
                                        Line l = (Line) n;
                                        l.setStroke(Color.DARKGREEN);
                                        l.toFront();
                                        CYPathFX.this.prevHighlightedFencesList.add(l);
                                    }
                                }
                            } else {
                                Node n = getNodeFromGridPane(CYPathFX.this.gPane, GridPane.getRowIndex(sourceCell)-1, GridPane.getColumnIndex(sourceCell)); // get the upper border of the cell
                                if(n instanceof Line) {
                                    Line l = (Line) n;
                                    l.setStroke(Color.DARKRED);
                                    l.toFront();
                                    CYPathFX.this.prevHighlightedFencesList.add(l);
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
                                        l.setStroke(Color.DARKGREEN);
                                        l.toFront();
                                        CYPathFX.this.prevHighlightedFencesList.add(l);
                                    }
                                }
                            } else {
                                Node n = getNodeFromGridPane(CYPathFX.this.gPane, GridPane.getRowIndex(sourceCell), GridPane.getColumnIndex(sourceCell)-1); // get the left border of the cell
                                if(n instanceof Line) {
                                    Line l = (Line) n;
                                    l.setStroke(Color.DARKRED);
                                    l.toFront();
                                    CYPathFX.this.prevHighlightedFencesList.add(l);
                                }
                            }
                        }

                        //Update data   
                        sourceCell.setOnMouseClicked(e -> {
                            Fence fence = null;
                            
                            System.out.println("click");
                            if(CYPathFX.this.getFenceOrientation() == Orientation.HORIZONTAL){
                                fence = new Fence(CYPathFX.this.game.getBoard().getFenceLength(), Orientation.HORIZONTAL, pStartFenceCoord);
                            }
                            else{
                                fence = new Fence(CYPathFX.this.game.getBoard().getFenceLength(), Orientation.VERTICAL, pStartFenceCoord);
                            }

                            if(CYPathFX.this.game.getBoard().isFencePositionValid(fence)){
                                CYPathFX.this.game.getBoard().addFenceToData(fence);
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
                            }
                        });
                        
                    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED){
                        if(CYPathFX.this.prevHighlightedFencesList != null){
                            for(Line l : CYPathFX.this.prevHighlightedFencesList) {
                                l.setStroke(Color.LIGHTGRAY);
                                l.toBack();
                            }
                            CYPathFX.this.prevHighlightedFencesList.clear();
                        }
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
        Color cellColor = Color.rgb(172, 255, 214);
        Color cellColorHover = Color.rgb(239,255,172);
        for( Point p : possibleMoves){
            Node node = (Rectangle) getNodeFromGridPane(gPane, p.getY()*2+1, p.getX()*2+1);
            //System.out.println(p.getX() + "," + p.getY());
            if( node instanceof Rectangle){
                Rectangle rec = (Rectangle) node;
                rec.setFill(cellColor);
                this.previousPossibleCells.add(rec);

                //Point previousPosition = this.game.getBoard().getPawn(pawnId).getPosition();

                rec.setOnMouseClicked(e -> {
                    try {
                        this.game.getBoard().getPawn(pawnId).setPosition(new Point(GridPane.getColumnIndex(rec) / 2, GridPane.getRowIndex(rec) / 2));
                        //the information is transmitted to the terminal
                        this.game.setIsEndTurn(true);
                        //We wait the begining of the next turn
                        while (this.game.getIsEndTurn()) {
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
                });

                rec.setOnMouseEntered(e -> rec.setFill(cellColorHover));
                rec.setOnMouseExited(e -> rec.setFill(cellColor));
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

        disableCellEvents();

        while (!previousPossibleCells.isEmpty()) {
            Rectangle rec = previousPossibleCells.getFirst();
            rec.setFill(cellColor);
            previousPossibleCells.removeFirst();
        }

        //System.out.println("Reset des couleurs");
    }


    /**
	 * Reset previous possible cell's event.
	 */
    public void disableCellEvents() {
        for (Rectangle rec : previousPossibleCells) {
            rec.setOnMouseClicked(null);
            rec.setOnMouseEntered(null);
            rec.setOnMouseExited(null);
        }
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