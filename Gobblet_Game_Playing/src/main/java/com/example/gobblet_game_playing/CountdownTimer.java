package com.example.gobblet_game_playing;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class CountdownTimer extends VBox {

    private int secondsRemaining ; // Set your desired countdown time in seconds
    private Label label;
    private Timeline timeline;
    private boolean isCountdownDone = false;

    public CountdownTimer(int secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
        label = new Label(formatTime(secondsRemaining));
        label.setStyle("-fx-font-size: 24;");
        //isCountdownDone = false;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            this.secondsRemaining--;
            label.setText(formatTime(this.secondsRemaining));
            if (this.secondsRemaining <= 0) {
                // Countdown reached zero, you can perform actions here
                isCountdownDone = true;
                timeline.stop();
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        setAlignment(Pos.TOP_CENTER);
        getChildren().add(label);
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
    public void restartTimer(int secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
        isCountdownDone = false;
        label.setText(formatTime(secondsRemaining));
        timeline.playFromStart();
    }
    public boolean isCountdownDone() {
        return isCountdownDone;
    }
}
