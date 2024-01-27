package com.example.gobblet_game_playing;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class StartGameGUI {
    static VBox startBox = new VBox();
    static HBox startBox1 = new HBox();
    static Button nextButton = new Button();
    static Button previousButton = new Button();
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
        startStage = stage;

        startBox.setPrefHeight(300);
        startBox.setPrefWidth(550);
        startBox.setAlignment(Pos.CENTER);

        // Load the image and set it as background
        Image backgroundImage = new Image(StartGameGUI.class.getResource("GameStart_background.png").toExternalForm());
        ImageView imageView = new ImageView(backgroundImage);
        imageView.fitHeightProperty().setValue(350);
        imageView.fitWidthProperty().setValue(600);
        startBox.setAlignment(Pos.CENTER);

        ToggleButton HPlayerVSHPlayer = createRadioButtonWithImage("H_vs_H.png");
        ToggleButton HPlayerVSCPlayer = createRadioButtonWithImage("H_vs_C.png");
        ToggleButton CPlayerVSCPlayer = createRadioButtonWithImage("C_vs_C.png");
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

        // Set Style for buttons
        HPlayerVSHPlayer.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: black; -fx-padding: 0px;");
        HPlayerVSCPlayer.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: black; -fx-padding: 0px;");
        CPlayerVSCPlayer.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: black; -fx-padding: 0px;");

        startBox.getChildren().clear();
        startBox1.getChildren().clear();
        startBox1.getChildren().addAll(HPlayerVSHPlayer, HPlayerVSCPlayer, CPlayerVSCPlayer);
        startBox1.setAlignment(Pos.CENTER);
        startBox1.setSpacing(20);
        startBox.getChildren().add(startBox1);

        Image image = new Image(StartGameGUI.class.getResource("next_button.png").toExternalForm());
        ImageView imageView2 = new ImageView(image);
        imageView2.setFitWidth(40);
        imageView2.setFitHeight(40);

        Image image1 = new Image(StartGameGUI.class.getResource("prev_button.png").toExternalForm());
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(40);
        imageView1.setFitHeight(40);

        previousButton.setGraphic(imageView1);
        nextButton.setGraphic(imageView2);
        previousButton.setStyle("-fx-background-color:transparent ; -fx-padding: 2px;");
        nextButton.setStyle("-fx-background-color:transparent ; -fx-padding: 2px;");

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(imageView,startBox);
        Scene startScene = new Scene(root, 550, 300);
        stage.setScene(startScene);
        stage.setTitle("Start Stage");
        stage.show();
    }
    public static ToggleButton createRadioButtonWithImage(String imagePath) {
        //Create buttons with images
        ToggleButton radioButton = new ToggleButton();
        Image image = new Image(StartGameGUI.class.getResource(imagePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(140);
        imageView.setFitHeight(80);

        radioButton.setGraphic(imageView);

        return radioButton;
    }

    public static void playerVSplayer() {

        Label player1Label = new Label("Player 1");
        player1Label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField player1 = new TextField("");
        player1.setPromptText("Enter player 1 name");
        player1.setFocusTraversable(false);
        HBox player1Box = new HBox(10);
        player1Box.getChildren().addAll(player1Label, player1);
        player1Box.setAlignment(Pos.CENTER);

        Label player2Label = new Label("Player 2");
        player2Label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField player2 = new TextField("");
        player2.setPromptText("Enter player 2 name");
        player2.setFocusTraversable(false);
        HBox player2Box = new HBox(10);
        player2Box.getChildren().addAll(player2Label, player2);
        player2Box.setAlignment(Pos.CENTER);


        HBox buttonBox = new HBox(130);
        previousButton.setOnAction(event -> GameStart(startStage));
        nextButton.setOnAction(e -> {
            players[0] = Game.PlayerType.HUMAN;
            players[1] = Game.PlayerType.HUMAN;
            difficulties[0] = null;
            difficulties[1] = null;
            players_name[0] = player1.getText();
            players_name[1] = player2.getText();
            if(player1.getText().isEmpty()){
                players_name[0] = "Player 1";
            }
            if(player2.getText().isEmpty()){
                players_name[1] = "Player 2";
            }
            openPrimaryStage();
        });

        buttonBox.getChildren().addAll(previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Clear and add the new layout
        startBox.getChildren().clear();
        startBox.getChildren().addAll(player1Box, player2Box, buttonBox);;
        startBox.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ENTER) {
                players[0] = Game.PlayerType.HUMAN;
                players[1] = Game.PlayerType.HUMAN;
                difficulties[0] = null;
                difficulties[1] = null;
                players_name[0] = player1.getText();
                players_name[1] = player2.getText();
                if(player1.getText().isEmpty()){
                    players_name[0] = "Player 1";
                }
                if(player2.getText().isEmpty()){
                    players_name[1] = "Player 2";
                }
                openPrimaryStage();
            }}));
    }

    public static void playerVScomp() {

        Label HumanPlayerLabel = new Label("Player");
        HumanPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField HumanPlayer = new TextField("");
        HumanPlayer.setPromptText("Enter player name");
        HumanPlayer.setFocusTraversable(false);
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
        difficultyChoiceBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius : 2px; -fx-background-color:transparent ; -fx-padding: 2px;");

        HBox buttonBox = new HBox(130);
        previousButton.setOnAction(event -> GameStart(startStage));
        nextButton.setOnAction(e -> {
            players[0] = Game.PlayerType.HUMAN;
            players[1] = Game.PlayerType.COMPUTER;
            difficulties[0] = null;
            difficulties[1] = difficultyChoiceBox.getValue();
            players_name[0] = HumanPlayer.getText();
            players_name[1] = compLabel.getText();
            if(HumanPlayer.getText().isEmpty()){
                players_name[0] = "Player";
            }
            openPrimaryStage();
        });


        buttonBox.getChildren().addAll(previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Clear and add the new layout
        startBox.getChildren().clear();
        startBox.getChildren().addAll( HumanPlayerBox, ComputerPlayerBox, buttonBox);
        startBox.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ENTER) {
                players[0] = Game.PlayerType.HUMAN;
                players[1] = Game.PlayerType.COMPUTER;
                difficulties[0] = null;
                difficulties[1] = difficultyChoiceBox.getValue();
                players_name[0] = HumanPlayer.getText();
                players_name[1] = compLabel.getText();
                if(HumanPlayer.getText().isEmpty()){
                    players_name[0] = "Player";
                }
                openPrimaryStage();
            }}));
    }

    public static void CompVSComp() {
        Label ComputerALabel = new Label("Computer A");
        ComputerALabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        ChoiceBox<Game.Difficulty> difficultyChoiceBoxA = new ChoiceBox<>();
        difficultyChoiceBoxA.getItems().addAll(Game.Difficulty.EASY, Game.Difficulty.NORMAL, Game.Difficulty.HARD);
        difficultyChoiceBoxA.setValue(Game.Difficulty.NORMAL); // Set default value
        difficultyChoiceBoxA.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius : 2px; -fx-background-color:transparent ; -fx-padding: 2px;");
        HBox ComputerABox = new HBox(10);
        ComputerABox.getChildren().addAll(ComputerALabel, difficultyChoiceBoxA);
        ComputerABox.setAlignment(Pos.CENTER);

        Label ComputerBLabel = new Label("Computer B");
        ComputerBLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        ChoiceBox<Game.Difficulty> difficultyChoiceBoxB = new ChoiceBox<>();
        difficultyChoiceBoxB.getItems().addAll(Game.Difficulty.EASY, Game.Difficulty.NORMAL, Game.Difficulty.HARD);
        difficultyChoiceBoxB.setValue(Game.Difficulty.NORMAL); // Set default value
        difficultyChoiceBoxB.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius : 2px; -fx-background-color:transparent ; -fx-padding: 2px;");
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
            openPrimaryStage();
        });


        buttonBox.getChildren().addAll(previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Clear and add the new layout
        startBox.getChildren().clear();
        startBox.getChildren().addAll( ComputerABox, ComputerBBox, buttonBox);
        startBox.setOnKeyPressed((e -> {
            if (e.getCode() == KeyCode.ENTER) {
                players[0] = Game.PlayerType.COMPUTER;
                players[1] = Game.PlayerType.COMPUTER;
                difficulties[0] = difficultyChoiceBoxA.getValue();
                difficulties[1] = difficultyChoiceBoxB.getValue();
                players_name[0] = ComputerALabel.getText();
                players_name[1] = ComputerBLabel.getText();
                openPrimaryStage();
            }}));
    }

    public static void openPrimaryStage() {
        GameGobbletApplication.primaryStage = new Stage();
        startStage.close();
        resetGame();
        GameGobbletApplication.primaryStage.show();

    }
    public static void resetGame() {
        /*handling start Game Stage Here*/
        Game.PlayerType type1 = StartGameGUI.getPlayers()[0];
        Game.PlayerType type2 = StartGameGUI.getPlayers()[1];
        String player1_name = StartGameGUI.getPlayers_name()[0];
        String player2_name = StartGameGUI.getPlayers_name()[1];

        Game.PlayerType players[] = new Game.PlayerType[2];
        players[0] = type1;
        players[1] = type2;

        // Game game = new Game(type1, player1_name, null, type2, player2_name, null);
        Game game = new Game(type1, player1_name, StartGameGUI.getDifficulties()[0], type2, player2_name, StartGameGUI.getDifficulties()[1]);

        /*initialize game*/
        BoardGUI.game = game;
        BoardGUI.type1=type1;
        BoardGUI.type2=type2;
        BoardGUI.player1_name = player1_name;
        BoardGUI.player2_name = player2_name;
        GobbletControllerGUI controllerGUI = new GobbletControllerGUI(game);
        BoardGUI.DrawBoard( GameGobbletApplication.primaryStage);
        BoardControllerGUI.restartHandler();

        if(type1 != Game.PlayerType.COMPUTER) {
            BoardGUI.setDrawPlayer1();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    controllerGUI. initGobbletsController(BoardGUI.blackImages[i][j], "B", i, j);
                }
            }
            BoardControllerGUI.buttonsController();
        }

        if(type2 != Game.PlayerType.COMPUTER) {
            BoardGUI.setDrawPlayer2();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    controllerGUI. initGobbletsController(BoardGUI.whiteImages[i][j], "W", i, j);
                }
            }
            BoardControllerGUI.buttonsController();
        }

        if(type2== Game.PlayerType.COMPUTER && type1== Game.PlayerType.COMPUTER) {
            BoardGUI.computerVsComputer();
        }
        BoardGUI.game.setCurrentTurn(BoardGUI.game.getCurrentTurn());
    }
}

