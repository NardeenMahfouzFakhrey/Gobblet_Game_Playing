package com.example.gobblet_game_playing;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Duration;
import javafx.stage.Stage;

public class BoardControllerGUI {

    public static void buttonsController() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                int finalI = i;
                int finalJ = j;

                BoardGUI.buttonPanes[i][j].setOnDragOver(event -> {
                    BoardGUI.moveState = false;
                    if (event.getGestureSource() != BoardGUI.buttonPanes[finalI][finalJ] && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }
                    event.consume();
                });

                BoardGUI.buttonPanes[i][j].setOnDragDropped(event -> {

                    Dragboard dragboard = event.getDragboard();
                    boolean success = false;
                    BoardGUI.newX = finalI;
                    BoardGUI.newY = finalJ;

                    if (BoardGUI.game.setCurrentGameMove(BoardGUI.oldX, BoardGUI.oldY, BoardGUI.newX, BoardGUI.newY, BoardGUI.stack)) {
                        if (dragboard.hasImage()) {
//                                buttonPanes[finalI][finalJ].getChildren().remove(imageView);
                            ImageView imageView = new ImageView();
                            imageView.setImage(dragboard.getImage());
                            BoardGUI.buttonPanes[finalI][finalJ].getChildren().add(imageView);
                            ClipboardContent content = new ClipboardContent();
                            imageView.setOnDragDetected(Detectedevent -> {
                                System.out.println("Image Drag Detected");
                                Dragboard dragboard2 = imageView.startDragAndDrop(TransferMode.COPY);
                                content.putImage(imageView.getImage());
                                dragboard2.setContent(content);
                                imageView.setImage(null);
                                Detectedevent.consume();
                                BoardGUI.oldX = finalI;
                                BoardGUI.oldY = finalJ;
                                BoardGUI.stack = -1;
                            });

                            // Add DRAG_DONE event handler
                            imageView.setOnDragDone(doneEvent -> {
                                System.out.println("Image Drag Done");
                                if (BoardGUI.moveState == false) {
                                    imageView.setImage(content.getImage());
                                }
                            });

                            System.out.println("Photo dropped on the button! " + "i = " + finalI + " j = " + finalJ);
                            success = true;
                            if ((BoardGUI.oldX == BoardGUI.newX) && (BoardGUI.oldY == BoardGUI.newY)) {

                            } else {
                                //Game.testGUI(oldX, oldY, newX, newY, stack);
                                if (BoardGUI.game.isGameEnded()) {
                                    dragboard.clear();
                                    PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                                    pause.setOnFinished(ev1 -> {
                                        Platform.runLater(() -> {
                                            System.out.println("Delay finished");
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
                        if (BoardGUI.type1 == Game.PlayerType.COMPUTER) {
                            GameMove gameMove = BoardGUI.game.getComputerMove();
                            PauseTransition initialDelay = new PauseTransition(Duration.seconds(3)); //  seconds delay
                            initialDelay.setOnFinished(e1 -> {
                            BoardGUI.computerBlackTurn(gameMove);
                            });
                            initialDelay.play();

                        } else if (BoardGUI.type2 == Game.PlayerType.COMPUTER) {
                            GameMove gameMove = BoardGUI.game.getComputerMove();
                            if(BoardGUI.game.isGameEnded()) {
                                BoardGUI.computerWhiteTurn(gameMove);
                            }
                            else {
                            PauseTransition initialDelay = new PauseTransition(Duration.seconds(3)); //  seconds delay
                            initialDelay.setOnFinished(e2 -> {
                            BoardGUI.computerWhiteTurn(gameMove);
                            });
                            initialDelay.play();
                        }
                        }
                        if (BoardGUI.game.isGameEnded()) {
                            dragboard.clear();
                            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                            pause.setOnFinished(ev2 -> {
                                Platform.runLater(() -> {
                                    System.out.println("Delay finished");
                                    BoardGUI.displayWinnerMessage(BoardGUI.game.getWinner());
                                });
                            });
                            pause.play();
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

    public static void restartHandler(){
        BoardGUI.restartButton.setOnAction(event -> {
            HelloApplication.startStage.close();
            HelloApplication.primaryStage.close();
            BoardGUI.RestartGame =true;
            BoardGUI.timer.restartTimer(30);
            StartGameGUI.GameStart(HelloApplication.startStage);
        });
    }
}