package com.example.gobblet_game_playing;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BoardGUI {
    static Button[][] button = new Button[4][4];
    static StackPane[][] buttonPanes = new StackPane[4][4];
    static VBox blackBox = new VBox();
    static VBox whiteBox = new VBox();
    static ImageView[][] blackImages = new ImageView[3][4];

    static ImageView[][] whiteImages = new ImageView[3][4];
    static HBox hbox;
    static int oldX = -1;
    static int oldY = -1;
    static int newX = -1;
    static int newY = -1;
    static int stack = -1;
    static boolean moveState = false;
    static Game game;

    static GobbletImage[][] gobbletTransparent = new GobbletImage[2][3];

    static ImageView[][] transparentImage = new ImageView[2][3];

    static Game.PlayerType type1;
    static Game.PlayerType type2;


    public static void DrawBorad(Stage stage) {

        String imagePath = "Gobblet_background.png";
        Image backgroundImage = new Image(BoardGUI.class.getResource(imagePath).toExternalForm());
        ImageView img = new ImageView(backgroundImage);

        img.fitHeightProperty().setValue(600);
        img.fitWidthProperty().setValue(750);

        BorderPane pane = new BorderPane();
        pane.setCenter(img);
        pane.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 0;");
        Pane buttonPane = placeButtons();
        pane.getChildren().add(buttonPane);

        DrawBlackGobblets();
        DrawWhiteGobblets();
        setTransparent();
        hbox = new HBox(blackBox, pane, whiteBox);


        // Create a Scene
        Scene scene = new Scene(hbox, 1150, 600);

        // Set the Scene for the Stage
        stage.setScene(scene);

        // Set the title of the Stage
        stage.setTitle("Gobblet AI Player");
        stage.setResizable(false);

        // Show the Stage
        stage.show();


    }

    public static void setTransparent() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                gobbletTransparent[i][j] = new GobbletImage("Transparent", 0, true);
                transparentImage[i][j] = BoardGUI.gobbletTransparent[i][j].getImgView();
            }
        }
    }

    public static Pane placeButtons() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                button[i][j] = new Button();
                buttonPanes[i][j] = new StackPane();
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttonPanes[i][j].setTranslateX(55 + i * 174);
                buttonPanes[i][j].setTranslateY(42 + j * 140);
                buttonPanes[i][j].setPrefHeight(100);
                buttonPanes[i][j].setPrefWidth(115);
                button[i][j].setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                // Set up drag-and-drop for the button
                int finalI = i;
                int finalJ = j;

                ImageView imageView = new ImageView();
                buttonPanes[i][j].getChildren().add(button[i][j]);

                if (type1 == Game.PlayerType.HUMAN || type2 == Game.PlayerType.HUMAN) {

                    buttonPanes[i][j].setOnDragOver(event -> {
                        moveState = false;
                        if (event.getGestureSource() != buttonPanes[finalI][finalJ] && event.getDragboard().hasImage()) {
                            event.acceptTransferModes(TransferMode.COPY);
                        }
                        event.consume();
                    });

                    buttonPanes[i][j].setOnDragDropped(event -> {
                        System.out.println("Button Drag Dropped");
                        Dragboard dragboard = event.getDragboard();
                        boolean success = false;
                        newX = finalI;
                        newY = finalJ;
                        if (game.setCurrentGameMove(BoardGUI.oldX, BoardGUI.oldY, BoardGUI.newX, BoardGUI.newY, BoardGUI.stack)) {
                            if (dragboard.hasImage()) {
                                buttonPanes[finalI][finalJ].getChildren().remove(imageView);
                                imageView.setImage(dragboard.getImage());
                                buttonPanes[finalI][finalJ].getChildren().add(imageView);
                                System.out.println("Photo dropped on the button! " + "i = " + finalI + " j = " + finalJ);
                                success = true;
                                if ((oldX == newX) && (oldY == newY)) {

                                } else {
                                    //Game.testGUI(oldX, oldY, newX, newY, stack);
                                    if (game.isGameEnded()) {
                                        dragboard.clear();
                                        displayWinnerMessage(game.getWinner());
                                        BoardGUI.hbox.getChildren().clear();
                                        BoardGUI.blackBox.getChildren().clear();
                                        BoardGUI.whiteBox.getChildren().clear();
                                        HelloApplication.resetGame();
                                        return;
                                    }
                                    stack = -1;
                                }
                            }
                            moveState = true;
                            event.setDropCompleted(success);
                            game.switchTurn();
                            event.consume();
                            if (type1 == Game.PlayerType.COMPUTER) {
                                computerBlackTurn();
                            } else if (type2 == Game.PlayerType.COMPUTER) {
                                computerWhiteTurn();
                            }
                            if (game.isGameEnded()) {
                                dragboard.clear();
                                displayWinnerMessage(game.getWinner());
                                BoardGUI.hbox.getChildren().clear();
                                BoardGUI.blackBox.getChildren().clear();
                                BoardGUI.whiteBox.getChildren().clear();
                                HelloApplication.resetGame();
                                return;
                            }
                        } else {
                            dragboard.clear();
                            event.setDropCompleted(false);
                            moveState = false;
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning!");
                            alert.setHeaderText("Incorrect Move");
                            alert.setContentText("Incorrect Move");
                            alert.show();
                            Duration duration = Duration.millis(1500);
                            PauseTransition pause = new PauseTransition(duration);
                            pause.setOnFinished(e -> alert.close());
                            pause.play();
                        }

                    });

                    ClipboardContent content = new ClipboardContent();
                    imageView.setOnDragDetected(event -> {
                        System.out.println("Image Drag Detected");
                        Dragboard dragboard = imageView.startDragAndDrop(TransferMode.COPY);
                        content.putImage(imageView.getImage());
                        dragboard.setContent(content);
                        imageView.setImage(null);
                        event.consume();
                        oldX = finalI;
                        oldY = finalJ;
                        stack = -1;
                    });

                    // Add DRAG_DONE event handler
                    imageView.setOnDragDone(doneEvent -> {
                        System.out.println("Image Drag Done");
                        if (moveState == false) {
                            imageView.setImage(content.getImage());
                        }
                    });
                }
            }
        }

        Pane buttonPane = new Pane();
        for (int i = 0; i < 4; i++) {
            buttonPane.getChildren().addAll(buttonPanes[i]);
        }
        return buttonPane;
    }

    public static void DrawBlackGobblets() {
        Label label = new Label("Player 1");
        label.setAlignment(Pos.TOP_CENTER);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                GobbletImage gobbletImage = new GobbletImage("B", j + 1, true);
                blackImages[i][j] = gobbletImage.getImgView();
            }
        }

        blackBox.setBackground(Background.fill(Color.WHITE));
        blackBox.setPrefWidth(200);
        blackBox.setStyle("-fx-border-color: black; -fx-border-width: 1 1 0 0;"); // top right bottom left

        blackBox.getChildren().add(label);
        blackBox.setMargin(label, new Insets(10, 0, 65, 0));
        blackBox.getChildren().addAll(blackImages[0][3], blackImages[1][3], blackImages[2][3]);

        blackBox.setAlignment(Pos.TOP_CENTER);
        blackBox.setSpacing(25);
    }

    public static void DrawWhiteGobblets() {

        Label label = new Label("Player 2");
        label.setAlignment(Pos.TOP_CENTER);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                GobbletImage gobbletImage = new GobbletImage("W", j + 1, true);
                whiteImages[i][j] = gobbletImage.getImgView();
            }
        }

        whiteBox.setBackground(Background.fill(Color.WHITE));
        whiteBox.setPrefWidth(200);
        whiteBox.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 1;");

        whiteBox.getChildren().add(label);
        whiteBox.setMargin(label, new Insets(10, 0, 65, 0));
        whiteBox.getChildren().addAll(whiteImages[0][3], whiteImages[1][3], whiteImages[2][3]);

        whiteBox.setAlignment(Pos.TOP_CENTER);
        whiteBox.setSpacing(25);
    }

    public static void replaceButton(VBox vbox, ImageView imageView, ImageView imageView1) {
        // Get the index of the oldButton in the VBox's children
        int index = vbox.getChildren().indexOf(imageView);

        // Replace the oldButton with the newButton at the same index
        if (index != -1) {
            vbox.getChildren().set(index, imageView1);
        }

    }

    private static void displayWinnerMessage(Player winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText("Congratulations, " + winner.getName() + "!");
        alert.setContentText("You are the winner!");

        // Add an image to the alert
        Image winnerImage = new Image(BoardGUI.class.getResource("winner.png").toExternalForm());
        ImageView imageView = new ImageView(winnerImage);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        alert.setGraphic(imageView);

        alert.showAndWait();
    }

    public static void computerWhiteTurn() {
        GameMove gameMove = game.getComputerMove();
        System.out.println("*****************WHITE TURN*****************");

        System.out.println("computer size " + gameMove.getGobblet().getGobbletSize() + " stackNo " + gameMove.getStackNo());
        if (gameMove.getStackNo() != -1) {

            if(gameMove.getGobblet().getGobbletSize().ordinal() == 0){
                replaceButton(BoardGUI.whiteBox, BoardGUI.whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()], transparentImage[0][gameMove.getStackNo()]);
            }else {
                replaceButton(BoardGUI.whiteBox, BoardGUI.whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()], BoardGUI.whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal() - 1]);
            }
            buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()]);

        } else {
            System.out.println("computer old move " + " x " + gameMove.getGobblet().getX() + " y " + gameMove.getGobblet().getY());
            System.out.println("computer new  move  " + "x " + gameMove.getX() + " y " + gameMove.getY());
            System.out.println("old x = " + gameMove.getGobblet().getX());
            System.out.println("old y = " + gameMove.getGobblet().getY());
            buttonPanes[gameMove.getGobblet().getX()][gameMove.getGobblet().getY()].getChildren().remove(whiteImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
            buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(whiteImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
        }
        game.switchTurn();
    }

    public static void computerBlackTurn() {
        GameMove gameMove = game.getComputerMove();
        System.out.println("*****************BLACK TURN*****************");

        System.out.println("computer size " + gameMove.getGobblet().getGobbletSize() + " stackNo " + gameMove.getStackNo());
        if (gameMove.getStackNo() != -1) {

            replaceButton(BoardGUI.blackBox, BoardGUI.blackImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()], BoardGUI.blackImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal() - 1]);
            buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(blackImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()]);

        } else {
            System.out.println("computer old move " + " x " + gameMove.getGobblet().getX() + " y " + gameMove.getGobblet().getY());
            System.out.println("computer new  move  " + "x " + gameMove.getX() + " y " + gameMove.getY());

            buttonPanes[gameMove.getGobblet().getX()][gameMove.getGobblet().getY()].getChildren().remove(blackImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
            buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(blackImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
        }
        game.switchTurn();
    }

    public static void computerVsComputer() {
        Thread computerVsComputerThread = new Thread(() -> {
            while (!game.isGameEnded()) {
                Platform.runLater(() -> computerBlackTurn());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> computerWhiteTurn());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Platform.runLater(() -> {
                displayWinnerMessage(game.getWinner());
                BoardGUI.hbox.getChildren().clear();
                BoardGUI.blackBox.getChildren().clear();
                BoardGUI.whiteBox.getChildren().clear();
                HelloApplication.resetGame();
            });
        });

        computerVsComputerThread.start();
    }
}
