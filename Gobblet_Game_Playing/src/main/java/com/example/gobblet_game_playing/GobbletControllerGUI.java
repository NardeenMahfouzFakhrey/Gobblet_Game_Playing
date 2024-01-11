package com.example.gobblet_game_playing;

import javafx.scene.image.ImageView;
import javafx.scene.input.*;

public class GobbletControllerGUI {
    private ImageView draggable;
    Game game;


    public GobbletControllerGUI(Game game) {
        this.game = game;
    }

    public void InitUi(ImageView gobblet, String color, int i, int j) {

        this.draggable = gobblet;


        if (this.draggable != null) {

            // Set up drag-and-drop for the Gobblet image on the stack
            gobblet.setOnDragDetected(event -> {
                BoardGUI.moveState = false;
                if((game.getCurrentTurn() == Game.Turn.A && color =="B") || (game.getCurrentTurn() == Game.Turn.B && color =="W")) {
                    Dragboard dragboard = gobblet.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(gobblet.getImage());
                    dragboard.setContent(content);
                    BoardGUI.oldX =-1;
                    BoardGUI.oldY = -1;
                    BoardGUI.stack = i;
                }
                else {
                    BoardGUI.alertMessageWarning("it's not your turn");
                }
                event.consume();
            });

            /*if image is done dropped replace Gobblets from stack to less size one*/
            gobblet.setOnDragDone(event -> {
                if(BoardGUI.moveState) {
                    if (j == 0) {
                        if (color == "W") {
                            int index = BoardGUI.whiteBox.getChildren().indexOf(BoardGUI.whiteImages[i][j]);
                            BoardGUI.whiteBox.getChildren().set(index, BoardGUI.transparentImage[0][i]);
                            BoardGUI.whiteBox.getChildren().remove(BoardGUI.whiteImages[i][j]);
                        } else if (color == "B") {
                            int index = BoardGUI.blackBox.getChildren().indexOf(BoardGUI.blackImages[i][j]);
                            BoardGUI.blackBox.getChildren().set(index, BoardGUI.transparentImage[1][i]);
                            BoardGUI.blackBox.getChildren().remove(BoardGUI.blackImages[i][j]);
                        }
                    } else {
                        if (color == "W") {
                            BoardGUI.replaceButton(BoardGUI.whiteBox, BoardGUI.whiteImages[i][j], BoardGUI.whiteImages[i][j - 1]);
                        } else if (color == "B") {
                            BoardGUI.replaceButton(BoardGUI.blackBox, BoardGUI.blackImages[i][j], BoardGUI.blackImages[i][j - 1]);
                        }

                    }
                    event.consume();
                }
            });
        }
    }
}
