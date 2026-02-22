package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
    private static final int rectangleSize = 70;
    private static final int xSize = 10;
    private static final int ySize = 8;
    private static final int nBombs = 10;
    private static int[][] map;
    private static List<List<Label>> labelList = new ArrayList<>();
    private static List<List<Rectangle>> rectangleList = new ArrayList<>();

    private void generateMap() {
        map = new int[xSize][ySize];

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                map[i][j] = 0;
            }
        }

        int bombPlaced = 0;

        while (bombPlaced != nBombs) {
            int x = (int) (Math.random() * xSize);
            int y = (int) (Math.random() * ySize);

            if (map[x][y] == 0) {
                map[x][y] = 1;
                bombPlaced++;
            }
        }
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
                    label.setTextFill(Color.YELLOW);

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
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    private void showLabel(int x, int y) {
        List<Label> list = labelList.get(x);
        list.get(y).setVisible(true);
    }

    private void reveal(int x, int y) {

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
    public void start(Stage stage) throws IOException {
        Pane pane = new Pane();
        pane.setPrefSize(xSize * rectangleSize, ySize * rectangleSize);
        scene = new Scene(pane);
        generateMap();
        printMap();

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
                            rectangle.setFill(Color.GREEN);
                        }
                        if (rectangle.getFill().equals(Color.DARKGRAY)) {
                            rectangle.setFill(Color.DARKRED);
                        } else if (rectangle.getFill().equals(Color.DARKRED)) {
                            rectangle.setFill(Color.DARKGREEN);
                        }
                    } else if (e.getButton().equals(MouseButton.PRIMARY)) {

                        if (!rectangle.getFill().equals(Color.DARKRED) && !rectangle.getFill().equals(Color.RED)) {

                            // showLabel((int) rectangle.getLayoutX() / rectangleSize,
                            // (int) rectangle.getLayoutY() / rectangleSize);

                            // rectangle.setFill(Color.WHITE);

                            int x = (int) rectangle.getLayoutX() / rectangleSize;
                            int y = (int) rectangle.getLayoutY() / rectangleSize;

                            reveal(x, y);

                            if (map[x][y] == 1) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Game Over");
                                alert.setHeaderText("💣 Ai pierdut!");
                                alert.setContentText("Vrei să vezi unde erau bombele?");

                                ButtonType showBombs = new ButtonType("Arată bombele");
                                ButtonType newGame = new ButtonType("Hartă nouă");

                                alert.getButtonTypes().setAll(showBombs, newGame);

                                Optional<ButtonType> result = alert.showAndWait();

                                if (result.isPresent() && result.get() == showBombs) {

                                } else {
                                    generateMap();
                                    generateLabelMap();

                                    refreshMap();
                                }

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
                label.setFont(Font.font(null, FontWeight.BOLD, 50));
                label.setAlignment(Pos.CENTER);
                label.setTextAlignment(TextAlignment.CENTER);
                label.setMouseTransparent(true);
                labelSublist.add(label);
                pane.getChildren().add(label);
            }
            labelList.add(labelSublist);
        }

        generateLabelMap();

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