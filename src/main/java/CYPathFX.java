import java.util.LinkedList;
import java.util.Scanner;


import javafx.application.Application;
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
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class CYPathFX extends Application {
    private Button actionButton;
    private Game game;
    private GridPane gPane;
    private HBox buttonsHBox;
    private Orientation fenceOrientation;
    private LinkedList<Line> prevHighlightedFencesList;

    //JavaFX
    public void start(Stage primaryStage) throws Exception {
        // Set up stage
        primaryStage.setTitle("CY Path : the Game");
        primaryStage.setResizable(false);

        fenceOrientation = Orientation.HORIZONTAL;
        this.gPane = createBoard();
        this.buttonsHBox = new HBox();
        actionButton = new Button("Move");
        actionButton.setOnAction(new ActionButtonHandler());
        BorderPane root = new BorderPane();
        buttonsHBox.getChildren().add(actionButton);

        

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

        this.game = new Game(nbPlayer,players,20, 9, 9);


        //Create a thread to run in the terminal
        Thread terminalThread = new Thread(() -> runInTerminal());
        terminalThread.start();

        // Open the window
        primaryStage.show(); 
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
                        System.out.println("H");
                        Fence fence = new Fence(CYPathFX.this.game.getBoard().getFenceLength(), Orientation.HORIZONTAL, pStartFenceCoord);
                        System.out.println("Fence: "+fence);
                        if(CYPathFX.this.game.getBoard().isFencePositionValid(fence)) {
                            System.out.println("valid");
                            int y = pStartCell.getY() - 1; // we take the row above the cell
                            for(int i=pStartCell.getX(); i<pStartCell.getX()+2*fence.getLength(); i+=2) {
                                Node n = getNodeFromGridPane(CYPathFX.this.gPane, y,i);
                                System.out.println("(i,y):"+i+" "+y);
                                System.out.println("n : "+n);
                                if(n instanceof Line) {
                                    Line l = (Line) n;
                                    System.out.println(l);
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
                        System.out.println("V");
                        Fence fence = new Fence(CYPathFX.this.game.getBoard().getFenceLength(), Orientation.VERTICAL, pStartFenceCoord);
                        System.out.println("Fence: "+fence);
                        if(CYPathFX.this.game.getBoard().isFencePositionValid(fence)) {
                            System.out.println("valid");
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
            if(CYPathFX.this.getFenceOrientation() == Orientation.HORIZONTAL) {
                CYPathFX.this.setFenceOrientation(Orientation.VERTICAL);
            } else {
                CYPathFX.this.setFenceOrientation(Orientation.HORIZONTAL);
            }
        }
        
    }

    class ActionButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
            //If the actual player have fence
            if(actionButton.getText() == "Move" && CYPathFX.this.game.getBoard().getPawns(CYPathFX.this.game.getBoard().getPawnIdTurn()).getAvailableFences() > 0){
                actionButton.setText("Place fence"); 
            }
            else{
                actionButton.setText("Move");
            }
        }
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

    public CYPathFX() {
        this.prevHighlightedFencesList = new LinkedList<Line>();
    }

    public static void main(String[] args) {

        launch(args);

    }
}