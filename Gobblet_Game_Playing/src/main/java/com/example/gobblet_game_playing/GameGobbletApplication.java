package com.example.gobblet_game_playing;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameGobbletApplication extends Application {

    static Stage primaryStage;
    static Stage startStage;

    /*start Game*/
    @Override
    public void start(Stage primaryStage) {
        startStage = primaryStage;
        StartGameGUI.GameStart(startStage);

    }

    public static void main(String[] args) {
        launch();
    } }

