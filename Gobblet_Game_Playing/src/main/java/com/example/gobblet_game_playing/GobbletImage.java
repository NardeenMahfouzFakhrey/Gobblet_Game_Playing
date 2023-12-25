package com.example.gobblet_game_playing;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GobbletImage {
    public static final String IMAGES_GOBBLETS = "G_%s_%d.png";

    private ImageView gobbletImageView;
    private final String color;
    private final int number;
    private int squarePos;

    public GobbletImage(String color, int number) {
        this(color, number, false);
    }

    public GobbletImage(String color, int number, boolean addImageView) {
        this.color = color;
        this.number = number;
        squarePos = -1;
        if (addImageView) {
            loadImageView();
        }
    }

    private void loadImageView() {
        gobbletImageView = new ImageView(new Image(BoardGUI.class.getResource(String.format(IMAGES_GOBBLETS, color, number)).toExternalForm()));
    }

    public String getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    public void setImage(ImageView VGobbletImageView) {
        this.gobbletImageView = VGobbletImageView;
    }

    public ImageView getImgView() {
        return gobbletImageView;
    }

    public int getSquarePos() {
        return squarePos;
    }

    public void setSquarePos(int squarePos) {
        this.squarePos = squarePos;
    }
}
