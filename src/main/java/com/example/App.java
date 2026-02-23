package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static int rectangleSize = 50; // 70 // 50
    private static int xSize = 16; // 10 // 16
    private static int ySize = 14; // 8 // 14
    private static int nBombs = 40; // 10 // 40
    private static int rectanglesRevealed = 0;
    private static int[][] map;
    public static boolean mapGenerated = false;
    private static List<List<Label>> labelList = new ArrayList<>();
    private static List<List<Rectangle>> rectangleList = new ArrayList<>();
    private static String gameState = "ongoing";// failed , won, ongoing
    public static String gameDifficulty = "easy"; // easy, medium. hard

    private void generateMap(int x, int y) {
        map = new int[xSize][ySize];

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                map[i][j] = 0;
            }
        }

        int bombPlaced = 0;

        while (bombPlaced != nBombs) {
            int genX = (int) (Math.random() * xSize);
            int genY = (int) (Math.random() * ySize);

            if (genX == x && genY == y)
                continue;
            if (genX == x - 1 && genY == y - 1)
                continue;
            if (genX == x - 1 && genY == y)
                continue;
            if (genX == x && genY == y - 1)
                continue;
            if (genX == x - 1 && genY == y + 1)
                continue;
            if (genX == x + 1 && genY == y - 1)
                continue;
            if (genX == x + 1 && genY == y + 1)
                continue;
            if (genX == x + 1 && genY == y)
                continue;
            if (genX == x && genY == y + 1)
                continue;

            if (map[genX][genY] == 0) {

                map[genX][genY] = 1;
                bombPlaced++;
            }
        }

        mapGenerated = true;

    }

    private void generateLabelMap() {
        for (int i = 0; i < xSize; i++) {
            List<Label> list = labelList.get(i);
            for (int j = 0; j < ySize; j++) {
                Label label = list.get(j);

                int k = 0;

                if (i >= 1 && j >= 1 && map[i - 1][j - 1] == 1)
                    k++;
                if (i >= 1 && map[i - 1][j] == 1)
                    k++;
                if (j >= 1 && map[i][j - 1] == 1)
                    k++;

                if (i < xSize - 1 && j < ySize - 1 && map[i + 1][j + 1] == 1)
                    k++;
                if (i < xSize - 1 && map[i + 1][j] == 1)
                    k++;
                if (j < ySize - 1 && map[i][j + 1] == 1)
                    k++;

                if (i < xSize - 1 && j >= 1 && map[i + 1][j - 1] == 1)
                    k++;
                if (i >= 1 && j < ySize - 1 && map[i - 1][j + 1] == 1)
                    k++;

                if (k == 1)
                    label.setTextFill(Color.BLUE);
                if (k == 2)
                    label.setTextFill(Color.GREEN);
                if (k == 3)
                    label.setTextFill(Color.RED);
                if (k == 4)
                    label.setTextFill(Color.PURPLE);
                if (k == 5)
                    label.setTextFill(Color.GOLD);

                label.setText(k + "");

            }
        }
        for (int i = 0; i < xSize; i++) {
            List<Label> list = labelList.get(i);
            for (int j = 0; j < ySize; j++) {
                Label label = list.get(j);

                if (map[i][j] == 1) {
                    label.setText("-1");
                    label.setTextFill(Color.RED);
                }
            }
        }
    }

    private void printMap() {
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                System.out.print(map[j][i]);
            }
            System.out.println();
        }
    }

    private void showBombs() {
        for (int i = 0; i < xSize; i++) {
            List<Label> list = labelList.get(i);
            for (int j = 0; j < ySize; j++) {
                Label label = list.get(j);

                if (map[i][j] == 1) {
                    label.setVisible(true);
                    label.setTextFill(Color.BLACK);
                    label.setText("💥");
                }
            }
        }
    }

    private void reveal(int x, int y) {
        if (gameState.equals("failed"))
            return;

        // verificare limite
        if (x < 0 || x >= xSize || y < 0 || y >= ySize)
            return;

        Rectangle rectangle = rectangleList.get(x).get(y);
        Label label = labelList.get(x).get(y);

        // dacă deja e deschis, ieșim
        if (rectangle.getFill().equals(Color.WHITE))
            return;

        // dacă este bombă, nu continuăm
        if (map[x][y] == 1)
            return;

        // deschidem pătratul
        rectangle.setFill(Color.WHITE);
        rectanglesRevealed++;

        // TODO: sout debug
        // System.out.println("Locuri descoperite: " + rectanglesRevealed);
        // System.out.println("Nr locuri care pot fi descoperite: " + ((xSize * ySize) -
        // nBombs));

        if ((xSize * ySize) - nBombs == rectanglesRevealed) {

            Alert alert = new Alert(AlertType.INFORMATION);

            alert.setTitle("Game Over !");
            alert.setHeaderText(null);
            alert.setContentText("Ai câștigat !");
            alert.showAndWait();

            mapGenerated = false;
            refreshMap();

            rectanglesRevealed = 0;

            return;
        }

        if (!label.getText().equals("0"))
            label.setVisible(true);

        // dacă nu este 0, ne oprim (prima cifră)
        if (!label.getText().equals("0"))
            return;

        // mergem pe toți vecinii
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (dx == 0 && dy == 0)
                    continue;

                reveal(x + dx, y + dy);
            }
        }

    }

    private void refreshMap() {
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {

                Rectangle r = rectangleList.get(i).get(j);

                if (j % 2 == 0 && i % 2 == 0)
                    r.setFill(Color.GREEN);
                else if (j % 2 == 0)
                    r.setFill(Color.DARKGREEN);
                else if (i % 2 == 0)
                    r.setFill(Color.DARKGREEN);
                else
                    r.setFill(Color.GREEN);

                labelList.get(i).get(j).setVisible(false);
            }
        }
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {

        switch (gameDifficulty) {
            case "easy":
                rectangleSize = 70;
                xSize = 10;
                ySize = 8;
                nBombs = 10;
                break;
            case "medium":
                rectangleSize = 50;
                xSize = 16;
                ySize = 14;
                nBombs = 40;
                break;
            case "hard":
                rectangleSize = 30;
                xSize = 24;
                ySize = 20;
                nBombs = 99;
                break;

            default:
                break;
        }

        Pane pane = new Pane();
        pane.setPrefSize(xSize * rectangleSize, ySize * rectangleSize);
        pane.setOnMouseClicked(e -> {
            // TODO: click debug
            // System.out.println("CLICKED");

            if (gameState.equals("failed")) {
                // generateMap();
                mapGenerated = false;
                refreshMap();

                rectanglesRevealed = 0;
                gameState = "ongoing";
            }
        });
        scene = new Scene(pane);
        // generateMap();

        for (int i = 0; i < xSize; i++) {

            List<Rectangle> subList = new ArrayList<>();

            for (int j = 0; j < ySize; j++) {
                Rectangle rectangle = new Rectangle(rectangleSize, rectangleSize);
                rectangle.setLayoutX(i * rectangleSize);
                rectangle.setLayoutY(j * rectangleSize);

                subList.add(rectangle);

                if (j % 2 == 0 && i % 2 == 0) {
                    rectangle.setFill(Color.GREEN);
                } else if (j % 2 == 0 && i % 2 != 0) {
                    rectangle.setFill(Color.DARKGREEN);

                } else if (j % 2 != 0 && i % 2 == 0) {

                    rectangle.setFill(Color.DARKGREEN);

                } else if (j % 2 != 0 && i % 2 != 0) {
                    rectangle.setFill(Color.GREEN);
                }

                rectangle.setOnMouseEntered(e -> {

                    if (rectangle.getFill().equals(Color.GREEN)) {
                        rectangle.setFill(Color.GRAY);
                    }
                    if (rectangle.getFill().equals(Color.DARKGREEN)) {
                        rectangle.setFill(Color.DARKGRAY);
                    }

                });

                rectangle.setOnMouseExited(e -> {

                    if (rectangle.getFill().equals(Color.GRAY)) {
                        rectangle.setFill(Color.GREEN);
                    }
                    if (rectangle.getFill().equals(Color.DARKGRAY)) {
                        rectangle.setFill(Color.DARKGREEN);
                    }

                });

                rectangle.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.SECONDARY)) {

                        if (rectangle.getFill().equals(Color.GRAY)) {
                            rectangle.setFill(Color.RED);
                        } else if (rectangle.getFill().equals(Color.RED)) {
                            rectangle.setFill(Color.GRAY);
                        }
                        if (rectangle.getFill().equals(Color.DARKGRAY)) {
                            rectangle.setFill(Color.DARKRED);
                        } else if (rectangle.getFill().equals(Color.DARKRED)) {
                            rectangle.setFill(Color.DARKGRAY);
                        }
                    } else if (e.getButton().equals(MouseButton.PRIMARY)) {
                        int x = (int) rectangle.getLayoutX() / rectangleSize;
                        int y = (int) rectangle.getLayoutY() / rectangleSize;
                        if (!mapGenerated) {
                            generateMap(x, y);
                            generateLabelMap();
                            refreshMap();
                            // printMap();
                        }

                        if (!rectangle.getFill().equals(Color.DARKRED) && !rectangle.getFill().equals(Color.RED)) {

                            reveal(x, y);

                            if (map[x][y] == 1 && gameState != "failed") {
                                e.consume();
                                gameState = "failed";
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Game Over");
                                alert.setHeaderText("💣 Ai pierdut!");
                                alert.setContentText("Vrei să vezi unde erau bombele?");

                                ButtonType showBombs = new ButtonType("Arată bombele");
                                ButtonType newGame = new ButtonType("Hartă nouă");

                                alert.getButtonTypes().setAll(showBombs, newGame);

                                Optional<ButtonType> result = alert.showAndWait();

                                if (result.isPresent() && result.get() == showBombs) {
                                    showBombs();

                                } else {
                                    // generateMap(x, y);
                                    mapGenerated = false;
                                    refreshMap();

                                    rectanglesRevealed = 0;

                                    gameState = "ongoing";

                                }
                            } else if (map[x][y] == 1 && gameState == "failed") {
                                mapGenerated = false;
                                refreshMap();

                                rectanglesRevealed = 0;

                                gameState = "ongoing";

                            }

                        }
                    }

                });

                pane.getChildren().add(rectangle);

            }

            rectangleList.add(subList);

        }

        for (int i = 0; i < xSize; i++) {

            List<Label> labelSublist = new ArrayList<>();

            for (int j = 0; j < ySize; j++) {
                Label label = new Label("1");
                label.setVisible(false);
                label.setPrefSize(rectangleSize, rectangleSize);
                label.setLayoutX(i * rectangleSize);
                label.setLayoutY(j * rectangleSize);
                label.setFont(Font.font(null, FontWeight.BOLD, rectangleSize - rectangleSize / 3));
                label.setAlignment(Pos.CENTER);
                label.setTextAlignment(TextAlignment.CENTER);
                label.setMouseTransparent(true);
                labelSublist.add(label);
                pane.getChildren().add(label);
            }
            labelList.add(labelSublist);
        }

        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}