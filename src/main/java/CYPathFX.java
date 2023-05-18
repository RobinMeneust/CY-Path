import java.util.Scanner;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class CYPathFX extends Application {
    private Button actionButton;
    private Game game;

    //JavaFX
    public void start(Stage primaryStage) throws Exception {
        // Set up stage
        primaryStage.setTitle("CY Path : the Game");
        primaryStage.setResizable(false);

        //GridPane gridPane = createBoard();
        actionButton = new Button("Move");
        actionButton.setOnAction(new actionButtonHandler());

        //Scene scene = new Scene(gridPane);
        Scene scene = new Scene(actionButton);
        primaryStage.setScene(scene);
        

        // Initialize game
        System.out.println("Welcome to CY-Path.\n");
        int nbPlayer = 0;
        Scanner sc = new Scanner(System.in);
        while(nbPlayer != 2 && nbPlayer != 4){
            System.out.println("How many player do you want ? (2 or 4)");
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

    /*public GridPane createBoard() {
        GridPane gPane = new GridPane();
        int sizeBoardRows = 9;
        int sizeBoardColumns = 9;

        return gPane;
    }*/

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