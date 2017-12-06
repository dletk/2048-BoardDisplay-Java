package BoardDisplayer2048;

import javafx.application.Application;
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

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private HashMap<Integer, String> colorMap;

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
        colorMap.put(64, "#e2b095");
        colorMap.put(128, "#8bd854");
        colorMap.put(256, "#e0e055");
        colorMap.put(512, "#f2a324");
        colorMap.put(1024, "#d3c22a");
        colorMap.put(2048, "#f7e011");

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage window = primaryStage;

        window.setTitle("2048 AI Board display");
        BorderPane layout = new BorderPane(createMainLayout(4,4), null, null, createBeginGameButton(), null);
        Scene mainScene = new Scene(layout);
        window.setScene(mainScene);
        window.show();
    }

    private VBox createMainLayout(int numRows, int numCols) {
        VBox mainLayout = new VBox(10);
        for (int i = 0; i < numRows; i++) {
            mainLayout.getChildren().add(createARow(numCols));
        }
        BorderPane.setAlignment(mainLayout, Pos.CENTER);
        return mainLayout;
    }

    private HBox createARow(int numCols) {
        Random randomGenerator = new Random();
        ArrayList<Label> rowElements = new ArrayList<>();
        for (int i=0; i < numCols; i++) {
            // Create the a cell in a row
            Label aCell = new Label("0");
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

        HBox row = new HBox(10);
        row.getChildren().addAll(rowElements);

        return row;
    }

    private Button createBeginGameButton() {
        Button begin = new Button("Begin game!");
        begin.setOnAction(event -> {
            // TODO: In this EventHandler, run the 2048 game, and get the new board after each move from computer or AI.
            // The 2048 game should be run in another new Thread so it will not freeze the GUI thread.
            // After each move, using Platform.runLater to set the new value to the display
            System.out.println("Begin touched!");
        });
        BorderPane.setAlignment(begin, Pos.CENTER);
        return begin;
    }
}
