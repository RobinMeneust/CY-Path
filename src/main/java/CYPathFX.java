import java.util.Scanner;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CYPathFX extends Application {
    private Button actionButton;
    private Game game;

    //JavaFX
    public void start(Stage primaryStage) throws Exception {
        // Set up stage
        primaryStage.setTitle("CY Path : the Game");
        primaryStage.setResizable(false);

        GridPane gridPane = createBoard();
        actionButton = new Button("Move");
        actionButton.setOnAction(new actionButtonHandler());
        BorderPane root = new BorderPane();

        root.setCenter(gridPane);
        root.setTop(actionButton);
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
        

        // First horizontal border (top)
        for(int j=1; j<=2*sizeBoardColumns; j+=2) {
            border = new Line(0,0,lineLengthBorders,0);
            border.setStrokeWidth(lineWidth);
            border.setStroke(borderColor);
            gPane.add(border,j,0);
        }
        gPane.getRowConstraints().add(new RowConstraints(lineWidth));
        
        for(int i=1; i<=2*sizeBoardRows; i+=2) {
            // First vertical border (left)
            border = new Line(0,0,0,lineLengthBorders);
            border.setStrokeWidth(lineWidth);
            border.setStroke(borderColor);
            gPane.add(border,0,i);
            
            for(int j=1; j<=2*sizeBoardColumns; j+=2) {
                // Cells
                cell = new Rectangle(cellSize,cellSize);
                cell.setFill(cellColor);
                gPane.add(cell,j,i);
                
                // Vertical borders
                border = new Line(0,0,0,lineLength);
                border.setStrokeWidth(lineWidth);
                border.setStroke(borderColor);
                gPane.add(border,j+1,i);
            }
            
            // Horizontal borders
            for(int j=1; j<2*sizeBoardColumns; j+=2) {
                border = new Line(0,0,lineLength,0);
                border.setStrokeWidth(lineWidth);
                border.setStroke(borderColor);
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

    class actionButtonHandler implements EventHandler<ActionEvent>{
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
    public static void main(String[] args) {

        launch(args);

    }
}