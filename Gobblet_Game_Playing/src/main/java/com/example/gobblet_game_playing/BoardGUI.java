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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    static HBox hbox = new HBox();

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
        if (type1 == Game.PlayerType.HUMAN || type2 == Game.PlayerType.HUMAN) {
            hboxNav.setAlignment(Pos.CENTER);
            if (type1 == Game.PlayerType.HUMAN) {
                drawPlayer1 = new Button("Draw");
                drawPlayer1.setBackground(Background.fill(Color.RED));
                drawPlayer1.setShape(new Circle(4));
                drawPlayer1.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");
                setSymbol(drawPlayer1,"✘");

                hboxNav.getChildren().add(drawPlayer1);
                HBox.setHgrow(drawPlayer1, Priority.NEVER);
                HBox.setMargin(drawPlayer1, new Insets(0, 5, 0, 0));
            }
            hboxNav.getChildren().add(timer);
            HBox.setHgrow(timer, Priority.ALWAYS);
            timer.setAlignment(Pos.TOP_CENTER);
            if (type2 == Game.PlayerType.HUMAN) {
                drawPlayer2 = new Button("Draw");
                drawPlayer2.setBackground(Background.fill(Color.RED));
                drawPlayer2.setShape(new Circle(4));
                drawPlayer2.setStyle("-fx-background-color:#8B0000; -fx-text-fill: white;");
                setSymbol(drawPlayer2,"✘");
                hboxNav.getChildren().add(drawPlayer2);
                HBox.setHgrow(drawPlayer2, Priority.NEVER);
                HBox.setMargin(drawPlayer2, new Insets(0, 0, 0, 5));
            } else
                HBox.setMargin(timer, new Insets(0, 45, 0, 0));
            h = 40;
            hboxNav.setPadding(new Insets(0, 5, 0, 5));
            //hboxNav.setSpacing(275);
            hboxNav.setPrefHeight(40);
            imgVbox.getChildren().addAll(hboxNav);
            Timeline checkCountdownTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), event -> {
                        if (timer.isCountdownDone()) {
                            System.out.println("Countdown is done!");
                            showAlert();
                            game.switchTurn();
                            game.setCurrentTurn(game.getCurrentTurn());
                            if (type1 == Game.PlayerType.COMPUTER) {
                                GameMove gameMove = BoardGUI.game.getComputerMove();
                                BoardGUI.computerBlackTurn(gameMove);
                            } else if (type2 == Game.PlayerType.COMPUTER) {
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
        DrawGobblets();

        setTransparent();

        StackPane pane2 = new StackPane();
        pane2.setBackground(Background.fill(Color.WHITE));
        imgVbox.getChildren().add(pane2);
        pane2.getChildren().add(restartButton);
        pane2.setAlignment(Pos.CENTER);
        restartButton.setPrefWidth(100);
        restartButton.setFont(new Font(12));


        hbox = new HBox(blackBox, imgVbox, whiteBox);
        // Create a Scene
        Scene scene = new Scene(hbox, 1150, 628 + h);
        // Set the Scene for the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("Gobblet AI Player");
        stage.setResizable(false);
        // Show the Stage
        stage.show();
    }

    private static void setSymbol(Button button, String symbol) {
        Text text = new Text(symbol);
        text.setFont(Font.font(20));
        button.setGraphic(text);
    }

    public static void setDrawPlayer1() {
        setDrawPlayer(drawPlayer1, Game.Turn.A,1);
    }

    public static void setDrawPlayer2() {
        setDrawPlayer(drawPlayer2, Game.Turn.B,2);
    }


    private static void setDrawPlayer(Button drawButton,  Game.Turn turn,int player) {
        drawButton.setOnAction(event -> {
            if (game.getCurrentTurn() == turn && player == 1) {
                if (drawFlag1) {
                    drawFlag1 = false;
                } else {
                    drawFlag1 = true;
                }
                setDrawButtonStyle(drawButton, drawFlag1);
            }
            if (game.getCurrentTurn() == turn && player == 2) {
                if (drawFlag2) {
                    drawFlag2 = false;
                } else {
                    drawFlag2 = true;
                }

                setDrawButtonStyle(drawButton, drawFlag2);
            }
            checkAndHandleDrawConditions();

        });
    }



    private static void setDrawButtonStyle(Button drawButton, boolean drawFlag) {
        String backgroundColor = drawFlag ? "#2E8B57" : "#8B0000"; // Darker green or red
        drawButton.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: white;");
        String symbol = drawFlag ? "✔" : "✘";
        setSymbol(drawButton, symbol);
    }

    private static void checkAndHandleDrawConditions() {
        if ((drawFlag1 && drawFlag2) || (type2 == Game.PlayerType.COMPUTER && drawFlag1)) {
            drawFlag1 = false;
            drawFlag2 = false;
            showDrawAlert();
            closeStagesAndRestartGame();
        }
    }

    private static void closeStagesAndRestartGame() {
        HelloApplication.startStage.close();
        HelloApplication.primaryStage.close();
        StartGameGUI.GameStart(HelloApplication.startStage);
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

    public static void DrawGobblets() {
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

        whiteBox = new VBox();
        Label label1 = new Label(player2_name);
        label1.setAlignment(Pos.TOP_CENTER);
        label1.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                GobbletImage gobbletImage = new GobbletImage("W", j + 1, true);
                whiteImages[i][j] = gobbletImage.getImgView();
            }
        }

        whiteBox.setBackground(Background.fill(Color.WHITE));
        whiteBox.setPrefWidth(200);
        whiteBox.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 1;");

        whiteBox.getChildren().add(label1);

        whiteBox.setMargin(label1, new Insets(10, 0, 65, 0));
        whiteBox.getChildren().addAll(whiteImages[0][3], whiteImages[1][3], whiteImages[2][3]);

        whiteBox.setAlignment(Pos.TOP_CENTER);
        whiteBox.setSpacing(25);
        whiteBox.setBackground(Background.fill(Color.LIGHTGRAY));  // Use a light gray color for shading
        whiteBox.setOpacity(0.7);

        game.currentTurnProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Turn changed to: " + newValue);

            if (newValue == Game.Turn.A) {


                blackBox.setBackground(Background.fill(Color.WHITE));
                blackBox.setOpacity(1.0);

                whiteBox.setBackground(Background.fill(Color.LIGHTGRAY));  // Use a light gray color for shading
                whiteBox.setOpacity(0.7);

            } else {
                whiteBox.setBackground(Background.fill(Color.WHITE));
                whiteBox.setOpacity(1.0);

                blackBox.setBackground(Background.fill(Color.LIGHTGRAY));  // Use a light gray color for shading
                blackBox.setOpacity(0.7);
            }

    });
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
        timer.stopTimer();
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
            game.setCurrentTurn(game.getCurrentTurn());
            timer.restartTimer(30);
//        });
//        initialDelay.play();

    }

    public static void computerBlackTurn(GameMove gameMove) {
//        PauseTransition initialDelay = new PauseTransition(Duration.seconds(3)); //  seconds delay
//        initialDelay.setOnFinished(event -> {

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
           game.setCurrentTurn(game.getCurrentTurn());
            timer.restartTimer(30);
//        });
//        initialDelay.play(); }

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
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!game.isGameEnded() && !RestartGame) {

                GameMove gameMove = game.getComputerMove();
                GameMove finalGameMove = gameMove;

                Platform.runLater(() -> computerBlackTurn(finalGameMove));
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                 if(game.isGameEnded()) {
                     break;
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
