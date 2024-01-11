package com.example.gobblet_game_playing;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.concurrent.CountDownLatch;

public class BoardGUI {
    static boolean RestartGame = false;
    static Game game;
    static Game.PlayerType type1;
    static Game.PlayerType type2;

    static Button[][] button = new Button[4][4];
    static StackPane[][] buttonPanes = new StackPane[4][4];
    static ImageView[][] blackImages = new ImageView[3][4];
    static GobbletImage[][] gobbletTransparent = new GobbletImage[2][3];
    static ImageView[][] transparentImage = new ImageView[2][3];
    static ImageView[][] whiteImages = new ImageView[3][4];

    static VBox blackBox;
    static VBox whiteBox;
    static HBox hbox=new HBox();

    static int oldX = -1;
    static int oldY = -1;
    static int newX = -1;
    static int newY = -1;
    static int stack = -1;
    static boolean moveState = false;
    static String player1_name;
    static String player2_name;
    static Button restartButton;

    static CountdownTimer timer;

    static Button drawPlayer1;
    static Button drawPlayer2;
    static boolean drawFlag1 = false;
    static boolean drawFlag2 = false;
    public static void DrawBoard(Stage stage) {
        int h = 0;
        drawFlag1 = false;
        drawFlag2 = false;
        BorderPane pane = new BorderPane();
        restartButton = new Button("Restart");
        /*draw game image on board*/
        String imagePath = "Gobblet_background.png";
        Image backgroundImage = new Image(BoardGUI.class.getResource(imagePath).toExternalForm());
        ImageView img = new ImageView(backgroundImage);

        img.fitHeightProperty().setValue(600);
        img.fitWidthProperty().setValue(750);

        pane.setCenter(img);
        pane.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 0;");
        VBox imgVbox = new VBox();
        HBox hboxNav = new HBox();

        timer = new CountdownTimer(30);
        if(type1 == Game.PlayerType.HUMAN || type2 == Game.PlayerType.HUMAN) {
            if(type1 == Game.PlayerType.HUMAN){
            drawPlayer1 = new Button("Draw");
            drawPlayer1.setBackground(Background.fill(Color.RED));
            hboxNav.getChildren().add(drawPlayer1);
            }
            h = 35;
            hboxNav.setSpacing(275);
            hboxNav.setAlignment(Pos.CENTER);
            hboxNav.getChildren().add(timer);
            //timer.setAlignment(Pos.CENTER);
            imgVbox.getChildren().addAll(hboxNav);
            if(type2 == Game.PlayerType.HUMAN){
                drawPlayer2 = new Button("Draw");
                drawPlayer2.setBackground(Background.fill(Color.RED));
                hboxNav.getChildren().add(drawPlayer2);
            }
            Timeline checkCountdownTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), event -> {
                        if (timer.isCountdownDone()) {
                            System.out.println("Countdown is done!");
                            showAlert();
                            game.switchTurn();
                            if(type1 == Game.PlayerType.COMPUTER){
                                GameMove gameMove = BoardGUI.game.getComputerMove();
                                BoardGUI.computerBlackTurn(gameMove);
                            }
                            else if(type2 == Game.PlayerType.COMPUTER){
                                GameMove gameMove = BoardGUI.game.getComputerMove();
                                BoardGUI.computerWhiteTurn(gameMove);
                            }
                            timer.restartTimer(30); // Restart the timer if needed
                        }
                    })
            );
            checkCountdownTimeline.setCycleCount(Timeline.INDEFINITE);
            checkCountdownTimeline.play();
        }
        imgVbox.getChildren().add(pane);
        /*place buttons on the Gobblet baord*/
        Pane buttonPane = placeButtons();
        pane.getChildren().add(buttonPane);

        /*draw Gobblet on the right and left of the baord */
        DrawBlackGobblets();
        DrawWhiteGobblets();
        setTransparent();


        hbox = new HBox(blackBox, imgVbox, whiteBox);
        // Create a Scene
        Scene scene = new Scene(hbox, 1150, 600+h);
        // Set the Scene for the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("Gobblet AI Player");
        stage.setResizable(false);
        // Show the Stage
        stage.show();
    }

    public static void setDrawPlayer1(){
        drawPlayer1.setOnAction(event -> {
            if(game.getCurrentTurn() == Game.Turn.A ){
                if(drawFlag1){
                    drawPlayer1.setBackground(Background.fill(Color.RED));
                    drawFlag1 = false;
                }else {
                    drawPlayer1.setBackground(Background.fill(Color.GREEN));
                    drawFlag1 = true;
                }
                if(drawFlag1 && drawFlag2 || (type2 == Game.PlayerType.COMPUTER && drawFlag1) ){
                    drawFlag1 = false;
                    drawFlag2 = false;
                    showDrawAlert();
                    HelloApplication.startStage.close();
                    HelloApplication.primaryStage.close();
                    StartGameGUI.GameStart(HelloApplication.startStage);
                }
            }
        });
    }
    public static void setDrawPlayer2(){
        drawPlayer2.setOnAction(event -> {
            if(game.getCurrentTurn() == Game.Turn.B ){
                if(drawFlag2){
                    drawPlayer2.setBackground(Background.fill(Color.RED));
                    drawFlag2 = false;
                }else {
                    drawPlayer2.setBackground(Background.fill(Color.GREEN));
                    drawFlag2 = true;
                }
                if(drawFlag1 && drawFlag2){
                    drawFlag1 = false;
                    drawFlag2 = false;
                    showDrawAlert();
                    HelloApplication.startStage.close();
                    HelloApplication.primaryStage.close();
                    StartGameGUI.GameStart(HelloApplication.startStage);
                }
            }
        });
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
                buttonPanes[i][j].getChildren().add(button[i][j]);
            }
        }

        Pane buttonPane = new Pane();
        for (int i = 0; i < 4; i++) {
            buttonPane.getChildren().addAll(buttonPanes[i]);
        }
        return buttonPane;
    }

    public static void DrawBlackGobblets() {
        blackBox = new VBox();
        Label label = new Label(player1_name);
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
        whiteBox = new VBox();
        Label label = new Label(player2_name);
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

        restartButton.setAlignment(Pos.BOTTOM_CENTER);
        whiteBox.getChildren().add(restartButton);
        whiteBox.setMargin(restartButton, new Insets(65, 0, 10, 0));

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

    public static void displayWinnerMessage(Player winner) {
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

        BoardGUI.hbox.getChildren().clear();
        BoardGUI.blackBox.getChildren().clear();
        BoardGUI.whiteBox.getChildren().clear();
        HelloApplication.startStage.close();
        HelloApplication.primaryStage.close();
        StartGameGUI.GameStart(HelloApplication.startStage);
    }

    public static void computerWhiteTurn(GameMove gameMove){
//        PauseTransition initialDelay = new PauseTransition(Duration.seconds(3)); //  seconds delay
//        initialDelay.setOnFinished(event -> {
        if(!game.isGameEnded()) {
            System.out.println("*****************WHITE TURN*****************");
            System.out.println("computer size " + gameMove.getGobblet().getGobbletSize() + " stackNo " + gameMove.getStackNo());
            if (gameMove.getStackNo() != -1) {

                if (gameMove.getGobblet().getGobbletSize().ordinal() == 0) {
                    replaceButton(BoardGUI.whiteBox, BoardGUI.whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()], transparentImage[0][gameMove.getStackNo()]);
                } else {
                    replaceButton(BoardGUI.whiteBox, BoardGUI.whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()], BoardGUI.whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal() - 1]);
                }
                buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(whiteImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()]);
            } else {
                System.out.println("computer old move " + " x " + gameMove.getGobblet().getX() + " y " + gameMove.getGobblet().getY());
                System.out.println("computer new  move  " + "x " + gameMove.getX() + " y " + gameMove.getY());
                System.out.println("old x = " + gameMove.getGobblet().getX());
                System.out.println("old y = " + gameMove.getGobblet().getY());
                ObservableList<Node> children = buttonPanes[gameMove.getGobblet().getX()][gameMove.getGobblet().getY()].getChildren();
                if (!children.isEmpty()) {
                    children.remove(children.size() - 1);
                }
                //ImageView i = children.get(children.size()-1);
                ImageView originalImageView = whiteImages[0][gameMove.getGobblet().getGobbletSize().ordinal()];
                Image originalImage = originalImageView.getImage();
                ImageView copyImageView = new ImageView(originalImage);
                copyImageView.setFitWidth(originalImageView.getFitWidth());
                copyImageView.setFitHeight(originalImageView.getFitHeight());
                // Add the copy to the new location
                buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(copyImageView);
//            buttonPanes[gameMove.getGobblet().getX()][gameMove.getGobblet().getY()].getChildren().remove(whiteImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
//            buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(whiteImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
            }
            game.switchTurn();
            timer.restartTimer(30);
//        });
//        initialDelay.play();
        }
    }

    public static void computerBlackTurn(GameMove gameMove) {
//        PauseTransition initialDelay = new PauseTransition(Duration.seconds(3)); //  seconds delay
//        initialDelay.setOnFinished(event -> {
        if (!game.isGameEnded()) {
            System.out.println("*****************BLACK TURN*****************");

            System.out.println("computer size " + gameMove.getGobblet().getGobbletSize() + " stackNo " + gameMove.getStackNo());
            if (gameMove.getStackNo() != -1) {

                if (gameMove.getGobblet().getGobbletSize().ordinal() == 0) {
                    replaceButton(BoardGUI.blackBox, BoardGUI.blackImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()], transparentImage[0][gameMove.getStackNo()]);
                } else {
                    replaceButton(BoardGUI.blackBox, BoardGUI.blackImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()], BoardGUI.blackImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal() - 1]);
                }
                buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(blackImages[gameMove.getStackNo()][gameMove.getGobblet().getGobbletSize().ordinal()]);

            } else {
                System.out.println("computer old move " + " x " + gameMove.getGobblet().getX() + " y " + gameMove.getGobblet().getY());
                System.out.println("computer new  move  " + "x " + gameMove.getX() + " y " + gameMove.getY());

                ObservableList<Node> children = buttonPanes[gameMove.getGobblet().getX()][gameMove.getGobblet().getY()].getChildren();
                if (!children.isEmpty()) {
                    children.remove(children.size() - 1);
                }
                //ImageView i = children.get(children.size()-1);
                ImageView originalImageView = blackImages[0][gameMove.getGobblet().getGobbletSize().ordinal()];
                Image originalImage = originalImageView.getImage();
                ImageView copyImageView = new ImageView(originalImage);
                copyImageView.setFitWidth(originalImageView.getFitWidth());
                copyImageView.setFitHeight(originalImageView.getFitHeight());
                // Add the copy to the new location
                buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(copyImageView);
//            buttonPanes[gameMove.getGobblet().getX()][gameMove.getGobblet().getY()].getChildren().remove(blackImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
//            buttonPanes[gameMove.getX()][gameMove.getY()].getChildren().add(blackImages[0][gameMove.getGobblet().getGobbletSize().ordinal()]);
            }
            game.switchTurn();
            timer.restartTimer(30);
//        });
//        initialDelay.play(); }
        }
    }
//    public static void computerVsComputer() {
//        Thread computerVsComputerThread = new Thread(() -> {
//            while (!game.isGameEnded()) {
//                CountDownLatch blackTurnLatch = new CountDownLatch(1);
//                CountDownLatch whiteTurnLatch = new CountDownLatch(1);
//                // Schedule computerBlackTurn to run on the JavaFX Application Thread
//                Platform.runLater(() -> {
//                    GameMove gameMove = game.getComputerMove();
//                    computerBlackTurn(gameMove);
//                    blackTurnLatch.countDown();
//                });
//                // Wait for black turn to complete
//                try {
//                    blackTurnLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                // Schedule computerWhiteTurn to run on the JavaFX Application Thread
//                Platform.runLater(() -> {
//                    GameMove gameMove = game.getComputerMove();
//                    computerWhiteTurn(gameMove);
//                    whiteTurnLatch.countDown();
//                });
//                // Wait for white turn to complete
//                try {
//                    whiteTurnLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            // Run UI updates on the JavaFX Application Thread
//            Platform.runLater(() -> {
//                displayWinnerMessage(game.getWinner());
//                BoardGUI.hbox.getChildren().clear();
//                BoardGUI.blackBox.getChildren().clear();
//                BoardGUI.whiteBox.getChildren().clear();
//                HelloApplication.resetGame();
//            });
//        });
//        computerVsComputerThread.start();
//    }

    public static void computerVsComputer() {
        BoardGUI.RestartGame=false;
        Thread computerVsComputerThread = new Thread(() -> {
            while (!game.isGameEnded() && !RestartGame) {
                GameMove gameMove = game.getComputerMove();
                GameMove finalGameMove = gameMove;
                Platform.runLater(() -> computerBlackTurn(finalGameMove));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameMove = game.getComputerMove();
                GameMove finalGameMove1 = gameMove;
                Platform.runLater(() -> computerWhiteTurn(finalGameMove1));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Platform.runLater(() -> {
                if(!RestartGame) {
                displayWinnerMessage(game.getWinner());
                BoardGUI.hbox.getChildren().clear();
                BoardGUI.blackBox.getChildren().clear();
                BoardGUI.whiteBox.getChildren().clear();
                HelloApplication.startStage.close();
                HelloApplication.primaryStage.close();
                StartGameGUI.GameStart(HelloApplication.startStage); }
            });
        });

        computerVsComputerThread.start();
    }


    public static void  alertMessageWarning(String s ) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText("Incorrect Move");
        alert.setContentText(s);
        alert.show();
        Duration duration = Duration.millis(1500);
        PauseTransition pause = new PauseTransition(duration);
        pause.setOnFinished(e -> alert.close());
        pause.play();
    }
    private static void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Time Out!");
        alert.show();

        // Set up a Timeline to automatically close the alert after 5 seconds
        Timeline closeAlertTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> alert.setResult(ButtonType.OK))
        );
        closeAlertTimeline.setCycleCount(1);
        closeAlertTimeline.play();
    }
    private static void showDrawAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Ended");
        alert.setHeaderText("Draw!");
        alert.setContentText("Game Ended with Draw!");
        alert.showAndWait();
        Duration duration = Duration.millis(5000);
        PauseTransition pause = new PauseTransition(duration);
        pause.setOnFinished(e -> alert.close());
        pause.play();
    }

    public static void setTransparent() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                gobbletTransparent[i][j] = new GobbletImage("Transparent", 0, true);
                transparentImage[i][j] = BoardGUI.gobbletTransparent[i][j].getImgView();
            }
        }
    }
}
