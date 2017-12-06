package BoardDisplayer2048;

import AI.BaseAI;
import AI.Game;
import AI.PlayerAI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Duc Le on Dec 5th, 2017
 */
public class Main extends Application {

    // This map contains the color code for each cell value from 0 - 2048.
    private HashMap<Integer, String> colorMap;
    // This is the
    private VBox boardLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();

        colorMap = new HashMap<>();
        // Put the color to display for each value into this map to display on the board
        colorMap.put(0, "#ced8ab");
        colorMap.put(2, "#d8a982");
        colorMap.put(4, "#cc9047");
        colorMap.put(8, "#ccc547");
        colorMap.put(16, "#96cc47");
        colorMap.put(32, "#cc7547");
        colorMap.put(64, "#ea6767");
        colorMap.put(128, "#8bd854");
        colorMap.put(256, "#e0e055");
        colorMap.put(512, "#f2a324");
        colorMap.put(1024, "#d3c22a");
        colorMap.put(2048, "#f7e011");

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;

        // Quick way to stop all threads and the program when close the window.
        window.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        // Set the title for this stage
        window.setTitle("2048 AI Board display");

        // We are using only the center and the bottom of the BorderPane, the other area can be used for displaying other
        // information, such as timing, reset button,...
        BorderPane layout = new BorderPane(
                createBoardLayout(4, 4),
                null,
                null,
                createBeginGameButton(),
                null);

        // Set the scene to display for the window
        Scene mainScene = new Scene(layout);
        window.setScene(mainScene);
        window.show();
    }

    /**
     * The method to create a VBox as the layout for the board. This VBox contains numRows HBox representing each row of
     * the 2048 board game.
     *
     * @param numRows number of rows in a 2048 board game
     * @param numCols number of cols in a 2048 board game
     * @return a VBox with numRows HBox representing rows
     */
    private VBox createBoardLayout(int numRows, int numCols) {
        boardLayout = new VBox(10);
        for (int i = 0; i < numRows; i++) {
            boardLayout.getChildren().add(createARow(numCols));
        }
        BorderPane.setAlignment(boardLayout, Pos.CENTER);
        return boardLayout;
    }

    /**
     * The method to create a row for the board layout. A row is a HBox with numCols label to display the cell.
     *
     * @param numCols number of row for the board
     * @return a row represented by an HBox containing numCols Label as cell
     */
    private HBox createARow(int numCols) {
        // This random generator is used to assign an initial displayed value to the cell before the user begin the game
        Random randomGenerator = new Random();

        // Each cell in the row is a Label
        ArrayList<Label> rowElements = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            // Create a cell in a row
            Label aCell = new Label("0");

            // Setting the style of each cell.
            aCell.setPrefSize(100, 100);
            aCell.setAlignment(Pos.CENTER);
            aCell.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 30));

            // Testing the display
            int labelNumber = (int) Math.pow(2, randomGenerator.nextInt(12));
            aCell.setText(Integer.toString(labelNumber));

            // Set the background to be the background of the corresponding number
            aCell.setStyle("-fx-background-color: " + colorMap.get(labelNumber));

            rowElements.add(aCell);
        }

        // Putting all the cell into an HBox representing the row as whole
        HBox row = new HBox(10);
        row.getChildren().addAll(rowElements);

        return row;
    }

    /**
     * Method to create the button to start the game.
     *
     * @return the begin button to start the game
     */
    private Button createBeginGameButton() {
        Button begin = new Button("Begin game!");
        begin.setOnAction(event -> {
            // TODO: In this EventHandler, run the 2048 game, and get the new board after each move from computer or AI.
            // The 2048 game should be run in another new Thread so it will not freeze the GUI thread.
            // After each move, using Platform.runLater to set the new value to the display.
            System.out.println("Begin touched!");

            // Creating a thread to run the game
            new Thread(() -> {
                BaseAI playerAI = new PlayerAI();
                Game game = new Game(playerAI);

                // The button should be disabled until the game is finished
                begin.setDisable(true);

                // Using PlatForm.runLater to put the request from the thread to the queue for the GUI thread.
                // So, it will not interfere and freeze the GUI.
                Platform.runLater(() -> displayBoard(game.getBoard().getMap()));

                while (true) {
                    game.playerTurn();
                    Platform.runLater(() -> displayBoard(game.getBoard().getMap()));

                    game.computerTurn();
                    Platform.runLater(() -> displayBoard(game.getBoard().getMap()));

                    if (game.isOver()) {
                        Platform.runLater(() -> displayBoard(game.getBoard().getMap()));
                        // Enable the button to start the next game
                        begin.setDisable(false);
                        break;
                    }
                }
            }).start(); // Begin the thread right away
        });

        // Make the button to be centered for better displaying
        BorderPane.setAlignment(begin, Pos.CENTER);
        return begin;
    }

    /**
     * The helper method to display a board given by the game manager to the board layout.
     *
     * @param board the current board of the game
     */
    private void displayBoard(int[][] board) {
        // Loop through each row, since we are constructing the boardLayout row by row.
        for (int row = 0; row < board.length; row++) {
            int[] aRow = board[row];
            // In the boardLayout, each children is a HBox representing each row in top down order
            HBox aRowLayout = (HBox) boardLayout.getChildren().get(row);
            // Loop and assign value for each cell in a row
            for (int col = 0; col < aRow.length; col++) {
                // Get the cell and update its value
                Label aCell = (Label) aRowLayout.getChildren().get(col);
                aCell.setText(Integer.toString(board[row][col]));

                // The cell need to be updated with correct background color for new value
                aCell.setStyle("-fx-background-color: " + colorMap.get(board[row][col]));
            }
        }
    }
}
