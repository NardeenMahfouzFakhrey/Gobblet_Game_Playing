package com.example.gobblet_game_playing;

import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.Event;
import javafx.scene.*;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.scene.control.Alert;
public class ControllerGUI {

    private ImageView draggable;
    private Double lastX = null;
    private Double lastY = null;
    Game game;
    boolean isMove = false;


    public ControllerGUI(Game game) {
        this.game = game;
    }

    public void InitUi(ImageView gobblet, String color, int i, int j) {

        this.draggable = gobblet;

        if (this.draggable != null) {

            // Set up drag-and-drop for the photo
            gobblet.setOnDragDetected(event -> {
                BoardGUI.moveState = false;
                if((game.getCurrentTurn() == Game.Turn.A && color =="B") || (game.getCurrentTurn() == Game.Turn.B && color =="W")) {
                    Dragboard dragboard = gobblet.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putImage(gobblet.getImage());
                    dragboard.setContent(content);
                    event.consume();
                    BoardGUI.oldX =-1;
                    BoardGUI.oldY = -1;
                    BoardGUI.stack = i;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning!");
                    alert.setHeaderText("Incorrect Move");
                    alert.setContentText("Not your turn!");
                    alert.show();
                    Duration duration = Duration.millis(1500);
                    PauseTransition pause = new PauseTransition(duration);
                    pause.setOnFinished(e -> alert.close());
                    pause.play();
                }
            });

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
