package com.example.gobblet_game_playing;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StartGameGUI {
    static VBox startBox = new VBox();
    static Button nextButton = new Button("Next");
    static Button previousButton = new Button("Previous");
    private static Stage startStage;
    static Game.PlayerType[] players = new Game.PlayerType[2];
    static Game.Difficulty[] difficulties = new Game.Difficulty[2];
    static String[] players_name = new String[2];

    public static Game.Difficulty[] getDifficulties() {
        return difficulties;
    }

    public static Game.PlayerType[] getPlayers() {
        return players;
    }

    public static String[] getPlayers_name() {
        return players_name;
    }

    public static void GameStart(Stage stage) {
        //startStage = new Stage();
        startStage = stage;

        startBox.setPrefHeight(300);
        startBox.setPrefWidth(550);
        // vbox.setPadding(new Insets(50,60,50,60));
        startBox.setAlignment(Pos.CENTER);
        startBox.setBackground(Background.fill(Color.WHITE));

        RadioButton HPlayerVSHPlayer = new RadioButton("Human vs Human");
        RadioButton HPlayerVSCPlayer = new RadioButton("Human vs Computer");
        RadioButton CPlayerVSCPlayer = new RadioButton("Computer vs Computer");
        HPlayerVSHPlayer.setOnAction(event -> playerVSplayer());
        CPlayerVSCPlayer.setOnAction(event -> CompVSComp());
        HPlayerVSCPlayer.setOnAction(event -> playerVScomp());

        // Set spacing between radio buttons
        startBox.setSpacing(20);
        // Create a ToggleGroup to ensure only one radio button can be selected at a time
        ToggleGroup toggleGroup = new ToggleGroup();
        HPlayerVSHPlayer.setToggleGroup(toggleGroup);
        HPlayerVSCPlayer.setToggleGroup(toggleGroup);
        CPlayerVSCPlayer.setToggleGroup(toggleGroup);

        startBox.getChildren().clear();
        startBox.getChildren().addAll(HPlayerVSHPlayer, HPlayerVSCPlayer, CPlayerVSCPlayer);
        FlowPane root = new FlowPane();
        root.setAlignment(Pos.CENTER); // Center the content
        root.getChildren().add(startBox);

        Scene startScene = new Scene(root, 550, 300);
        stage.setScene(startScene);
        stage.setTitle("Start Stage");
        stage.show();
    }

    public static void playerVSplayer() {
        Label Title = new Label("Enter players name");
        Title.setAlignment(Pos.CENTER);

        Label player1Label = new Label("Player 1");
        player1Label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField player1 = new TextField("");
        player1.setPromptText("Player 1");
        HBox player1Box = new HBox(10);
        player1Box.getChildren().addAll(player1Label, player1);
        player1Box.setAlignment(Pos.CENTER);

        Label player2Label = new Label("Player 2");
        player2Label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField player2 = new TextField("");
        player2.setPromptText("Player 2");
        HBox player2Box = new HBox(10);
        player2Box.getChildren().addAll(player2Label, player2);
        player2Box.setAlignment(Pos.CENTER);


        HBox buttonBox = new HBox(100);
        previousButton.setOnAction(event -> GameStart(startStage));
        nextButton.setOnAction(e -> {
            players[0] = Game.PlayerType.HUMAN;
            players[1] = Game.PlayerType.HUMAN;
            difficulties[0] = null;
            difficulties[1] = null;
            players_name[0] = player1.getText();
            players_name[1] = player2.getText();
            HelloApplication.openPrimaryStage();
        });

        buttonBox.getChildren().addAll(previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Clear and add the new layout
        startBox.getChildren().clear();
        startBox.getChildren().addAll(Title, player1Box, player2Box, buttonBox);
    }

    public static void playerVScomp() {
        Label Title = new Label("Enter player name and Computer difficulty level");
        Title.setAlignment(Pos.CENTER);

        Label HumanPlayerLabel = new Label("Player");
        HumanPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField HumanPlayer = new TextField("");
        HumanPlayer.setPromptText("Player");
        HBox HumanPlayerBox = new HBox(10);
        HumanPlayerBox.getChildren().addAll(HumanPlayerLabel, HumanPlayer);
        HumanPlayerBox.setAlignment(Pos.CENTER);

        Label compLabel = new Label("Computer");
        compLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        ChoiceBox<Game.Difficulty> difficultyChoiceBox = new ChoiceBox<>();
        difficultyChoiceBox.getItems().addAll(Game.Difficulty.EASY, Game.Difficulty.NORMAL, Game.Difficulty.HARD);
        difficultyChoiceBox.setValue(Game.Difficulty.NORMAL); // Set default value
        HBox ComputerPlayerBox = new HBox(10);
        ComputerPlayerBox.getChildren().addAll(compLabel, difficultyChoiceBox);
        ComputerPlayerBox.setAlignment(Pos.CENTER);


        HBox buttonBox = new HBox(100);
        previousButton.setOnAction(event -> GameStart(startStage));
        nextButton.setOnAction(e -> {
            players[0] = Game.PlayerType.HUMAN;
            players[1] = Game.PlayerType.COMPUTER;
            difficulties[0] = null;
            difficulties[1] = difficultyChoiceBox.getValue();
            players_name[0] = HumanPlayer.getText();
            players_name[1] = compLabel.getText();
            HelloApplication.openPrimaryStage();
        });

        buttonBox.getChildren().addAll(previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Clear and add the new layout
        startBox.getChildren().clear();
        startBox.getChildren().addAll(Title, HumanPlayerBox, ComputerPlayerBox, buttonBox);
    }

    public static void CompVSComp() {
        Label Title = new Label("Choose difficulty level for each computer player");
        Title.setAlignment(Pos.CENTER);

        Label ComputerALabel = new Label("Computer A");
        ComputerALabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        ChoiceBox<Game.Difficulty> difficultyChoiceBoxA = new ChoiceBox<>();
        difficultyChoiceBoxA.getItems().addAll(Game.Difficulty.EASY, Game.Difficulty.NORMAL, Game.Difficulty.HARD);
        difficultyChoiceBoxA.setValue(Game.Difficulty.NORMAL); // Set default value
        HBox ComputerABox = new HBox(10);
        ComputerABox.getChildren().addAll(ComputerALabel, difficultyChoiceBoxA);
        ComputerABox.setAlignment(Pos.CENTER);

        Label ComputerBLabel = new Label("Computer B");
        ComputerBLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        ChoiceBox<Game.Difficulty> difficultyChoiceBoxB = new ChoiceBox<>();
        difficultyChoiceBoxB.getItems().addAll(Game.Difficulty.EASY, Game.Difficulty.NORMAL, Game.Difficulty.HARD);
        difficultyChoiceBoxB.setValue(Game.Difficulty.NORMAL); // Set default value
        HBox ComputerBBox = new HBox(10);
        ComputerBBox.getChildren().addAll(ComputerBLabel, difficultyChoiceBoxB);
        ComputerBBox.setAlignment(Pos.CENTER);


        HBox buttonBox = new HBox(100);
        previousButton.setOnAction(event -> GameStart(startStage));
        nextButton.setOnAction(e -> {
            players[0] = Game.PlayerType.COMPUTER;
            players[1] = Game.PlayerType.COMPUTER;
            difficulties[0] = difficultyChoiceBoxA.getValue();
            difficulties[1] = difficultyChoiceBoxB.getValue();
            players_name[0] = ComputerALabel.getText();
            players_name[1] = ComputerBLabel.getText();
            HelloApplication.openPrimaryStage();
        });


        buttonBox.getChildren().addAll(previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Clear and add the new layout
        startBox.getChildren().clear();
        startBox.getChildren().addAll(Title, ComputerABox, ComputerBBox, buttonBox);
    }
}

