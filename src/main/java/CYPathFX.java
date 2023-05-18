import java.util.Scanner;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Line;
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

        Scene scene = new Scene(gridPane);
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
        ImageView imageView = null;
        Image emptyCellImage = new Image(getClass().getResource("images/emptyCell.jpg").toExternalForm());
        Line border = null;
        
        for(int i=0; i<2*sizeBoardRows; i+=2) {
            for(int j=0; j<2*sizeBoardColumns; j+=2) {
                // Vertical borders
                border = new Line(0,0,0,50);
                border.setStrokeWidth(2);
                gPane.add(border,i,j);

                // Cells
                imageView = new ImageView(emptyCellImage);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                gPane.add(imageView,i,2*sizeBoardColumns);
            }

            // Last vertical border (right)
            border = new Line(0,0,0,50);
            border.setStrokeWidth(2);
            gPane.add(border,i,2*sizeBoardColumns);

            // Horizontal borders
            for(int j=1; j<2*sizeBoardColumns; j+=2) {
                border = new Line(0,0,50,0);
                border.setStrokeWidth(2);
                gPane.add(border,i,j);
            }
            // Horizontal borders
            gPane.getRowConstraints().add(new RowConstraints(2));
            gPane.getRowConstraints().add(new RowConstraints(50));
        }

        // Last horizontal border (bottom)
        for(int j=1; j<=2*sizeBoardRows; j+=2) {
            border = new Line(0,0,50,0);
            border.setStrokeWidth(2);
            gPane.add(border,sizeBoardRows,j);
        }
        gPane.getRowConstraints().add(new RowConstraints(2));
        
        for(int i=0; i<2*sizeBoardColumns; i+=2) {
            gPane.getColumnConstraints().add(new ColumnConstraints(2));
            gPane.getColumnConstraints().add(new ColumnConstraints(40));
        }
        gPane.getColumnConstraints().add(new ColumnConstraints(2));

        return gPane;
    }


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