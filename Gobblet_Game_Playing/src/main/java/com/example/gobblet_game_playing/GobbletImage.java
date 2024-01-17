package com.example.gobblet_game_playing;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GobbletImage {
    public static final String IMAGES_GOBBLETS = "G_%s_%d.png";

    private ImageView gobbletImageView;
    private final String color;
    private final int number;
    private int squarePos;

    public GobbletImage(String color, int number, boolean addImageView) {
        this.color = color;
        this.number = number;
        squarePos = -1;
        if (addImageView) {
            loadImageView();
        }
    }

    private void loadImageView() {
        gobbletImageView = new ImageView(new Image(BoardGUI.class.getResource(String.format(IMAGES_GOBBLETS, color, number))
                .toExternalForm()));
    }

    public ImageView getImgView() {
        return gobbletImageView;
    }

}
