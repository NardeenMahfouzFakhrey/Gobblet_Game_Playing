package com.example.gobblet_game_playing;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Duration;

public class BoardControllerGUI {

    /*
     * Function to add controllers in board GUI
     */
    public static void buttonsController() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                int finalI = i;
                int finalJ = j;

                // case of Gobblet is dragged from board
                BoardGUI.buttonPanes[i][j].setOnDragOver(event -> {
                    BoardGUI.moveState = false;
                    if (event.getGestureSource() != BoardGUI.buttonPanes[finalI][finalJ] && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }
                    event.consume();
                });

                // case of Gobblet is dropped on the board
                BoardGUI.buttonPanes[i][j].setOnDragDropped(event -> {

                    Dragboard dragboard = event.getDragboard();
                    boolean success = false;
                    BoardGUI.newX = finalI;
                    BoardGUI.newY = finalJ;

                    if (BoardGUI.game.setCurrentGameMove(BoardGUI.oldX, BoardGUI.oldY, BoardGUI.newX, BoardGUI.newY, BoardGUI.stack)) {
                        if (dragboard.hasImage()) {

                            ImageView imageView = new ImageView();
                            imageView.setImage(dragboard.getImage());
                            BoardGUI.buttonPanes[finalI][finalJ].getChildren().add(imageView);
                            ClipboardContent content = new ClipboardContent();
                            imageView.setOnDragDetected(Detectedevent -> {

                                Dragboard dragboard2 = imageView.startDragAndDrop(TransferMode.COPY);
                                content.putImage(imageView.getImage());
                                dragboard2.setContent(content);
                                imageView.setImage(null);
                                Detectedevent.consume();
                                BoardGUI.oldX = finalI;
                                BoardGUI.oldY = finalJ;
                                BoardGUI.stack = -1;
                            });

                            // case of Gobblet is dropped done on the board
                            imageView.setOnDragDone(doneEvent -> {

                                if (BoardGUI.moveState == false) {
                                    imageView.setImage(content.getImage());
                                }
                            });


                            success = true;
                            if ((BoardGUI.oldX == BoardGUI.newX) && (BoardGUI.oldY == BoardGUI.newY)) {

                            } else {

                                if (BoardGUI.game.isGameEnded()) {
                                    BoardGUI.moveState = true;
                                    dragboard.clear();
                                    PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                                    pause.setOnFinished(ev1 -> {
                                        Platform.runLater(() -> {

                                            BoardGUI.displayWinnerMessage(BoardGUI.game.getWinner());
                                        });
                                    });
                                    pause.play();
                                    return;
                                }
                                BoardGUI.stack = -1;
                            }
                        }
                        BoardGUI.moveState = true;
                        BoardGUI.timer.restartTimer(30);
                        event.setDropCompleted(success);
                        BoardGUI.game.switchTurn();
                        BoardGUI.game.setCurrentTurn(BoardGUI.game.getCurrentTurn());
                        event.consume();

                        //handle computer turn
                        if (BoardGUI.type2 == Game.PlayerType.COMPUTER) {
                            PauseTransition initialDelay1 = new PauseTransition(Duration.seconds(0.5));
                            initialDelay1.setOnFinished(m -> {
                                GameMove gameMove = BoardGUI.game.getComputerMove();
                                PauseTransition initialDelay = new PauseTransition(Duration.seconds(3));
                                initialDelay.setOnFinished(e2 -> {
                                    BoardGUI.computerWhiteTurn(gameMove);
                                    if (BoardGUI.game.isGameEnded()) {
                                        dragboard.clear();
                                        PauseTransition pause = new PauseTransition(Duration.seconds(1));
                                        pause.setOnFinished(ev2 -> {
                                            Platform.runLater(() -> {

                                                BoardGUI.displayWinnerMessage(BoardGUI.game.getWinner());
                                            });
                                        });
                                        pause.play();
                                    }

                                });
                                initialDelay.play();
                            });
                            initialDelay1.play();
                        }
                    } else {
                        dragboard.clear();
                        event.setDropCompleted(false);
                        BoardGUI.moveState = false;
                        BoardGUI.alertMessageWarning("incorrect Move");
                    }
                });
            }
        }
    }
    /*to handle restart of the game by restart button */
    public static void restartHandler(){
        BoardGUI.restartButton.setOnAction(event -> {
            GameGobbletApplication.startStage.close();
            GameGobbletApplication.primaryStage.close();
            BoardGUI.RestartGame =true;
            BoardGUI.timer.stopTimer();
            StartGameGUI.GameStart(GameGobbletApplication.startStage);
        });
    }
}