package com.example.gobblet_game_playing;

import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class baordControllerGUI {

    public static void buttonsContoller() {

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
                                        BoardGUI.displayWinnerMessage(BoardGUI.game.getWinner());
                                        return;
                                    }
                                    BoardGUI.stack = -1;
                                }
                            }
                            BoardGUI.moveState = true;
                            event.setDropCompleted(success);
                            BoardGUI.game.switchTurn();
                            event.consume();
                            if (BoardGUI.type1 == Game.PlayerType.COMPUTER) {
                                GameMove gameMove = BoardGUI.game.getComputerMove();
                                BoardGUI.computerBlackTurn(gameMove);
                            } else if (BoardGUI.type2 == Game.PlayerType.COMPUTER) {
                                GameMove gameMove = BoardGUI.game.getComputerMove();
                                BoardGUI.computerWhiteTurn(gameMove);
                            }
                            if (BoardGUI.game.isGameEnded()) {
                                dragboard.clear();
                                BoardGUI.displayWinnerMessage(BoardGUI.game.getWinner());
                                return;
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
}
