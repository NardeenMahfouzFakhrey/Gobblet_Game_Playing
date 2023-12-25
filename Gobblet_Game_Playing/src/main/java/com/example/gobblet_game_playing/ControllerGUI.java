package com.example.gobblet_game_playing;

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
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.util.Pair;

public class ControllerGUI {

    private ImageView draggable;
    private Double lastX = null;
    private Double lastY = null;

    public void InitUi(ImageView gobblet,String color, int i, int j) {

        this.draggable = gobblet;

        if (this.draggable != null) {

            // Set up drag-and-drop for the photo
            gobblet.setOnDragDetected(event -> {
                Dragboard dragboard = gobblet.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putImage(gobblet.getImage());
                dragboard.setContent(content);
                event.consume();
                BoardGUI.oldX = -1;
                BoardGUI.oldY = -1;
                BoardGUI.stack = i;

            });

            gobblet.setOnDragDone(event -> {
                if (j == 0){
                    if (color == "W") {
                        BoardGUI.whiteBox.getChildren().remove(BoardGUI.whiteImages[i][j]);
                    } else if (color == "B") {
                        BoardGUI.blackBox.getChildren().remove(BoardGUI.blackImages[i][j]);
                    }
                }else{
                    if (color == "W") {
                        BoardGUI.replaceButton(BoardGUI.whiteBox, BoardGUI.whiteImages[i][j], BoardGUI.whiteImages[i][j - 1]);
                    } else if (color == "B") {
                        BoardGUI.replaceButton(BoardGUI.blackBox, BoardGUI.blackImages[i][j], BoardGUI.blackImages[i][j - 1]);
                    }
                }

                event.consume();
            });
        }
    }

    private synchronized void HandleMouseMovement(double sceneX, double sceneY) {
        double deltaX = sceneX - lastX;
        double deltaY = sceneY - lastY;

        lastX = sceneX;
        lastY = sceneY;

        double currentXLayout = draggable.getX();
        double currentYLayout = draggable.getY();

        draggable.setX(currentXLayout + deltaX * 1.5);
        draggable.setY(currentYLayout + deltaY*1.5);
    }

    private Pair<Integer, Integer> getSquare(int lastX, int lastY){
        Pair pair = new Pair<>(null,null);
        if (lastX < 5 && lastY > 3 ){
            pair = new Pair<>(5,6);
        }

       return pair;
    }

}
