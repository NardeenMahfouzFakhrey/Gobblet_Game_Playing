package com.example.gobblet_game_playing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        resetGame();
    }

    public static void resetGame() {

        Game.PlayerType type1 = Game.PlayerType.COMPUTER;
        Game.PlayerType type2 = Game.PlayerType.COMPUTER;

        Game.PlayerType players[] = new Game.PlayerType[2];
        players[0] = type1;
        players[1] = type2;

       Game game = new Game(type1, "karine", Game.Difficulty.HARD, type2, "Tantawy", Game.Difficulty.NORMAL);

        BoardGUI.game = game;
        BoardGUI.type1=type1;
        BoardGUI.type2=type2;
        System.out.println(game.getCurrentTurn());
        ControllerGUI controllerGUI = new ControllerGUI(game);
        BoardGUI.DrawBorad(primaryStage);

        if(type1 != Game.PlayerType.COMPUTER) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    controllerGUI.InitUi(BoardGUI.blackImages[i][j], "B", i, j);
                }
            }
        }

        if(type2 != Game.PlayerType.COMPUTER) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    controllerGUI.InitUi(BoardGUI.whiteImages[i][j], "W", i, j);
                }
            }
        }

        if(type2== Game.PlayerType.COMPUTER && type1== Game.PlayerType.COMPUTER) {
            BoardGUI.computerVsComputer();
        }
    }

    public static void main(String[] args) {
        launch();
    } }

//
//    public static void main(String[] args) {
//
//        Scanner sc = new Scanner(System.in);
//
//        Game.PlayerType type1 = Game.PlayerType.COMPUTER;
//        Game.PlayerType type2 = Game.PlayerType.COMPUTER;
//
//        Game.PlayerType players[] = new Game.PlayerType[2];
//        players[0] = type1;
//        players[1] = type2;
//
//        Game game = new Game(type1, "karine", Game.Difficulty.HARD, type2, "Tantawy", Game.Difficulty.NORMAL);
//
//        System.out.println("Start Game");
//
//        while (!game.isGameEnded()) {
//            if (players[game.getCurrentTurn().ordinal()].ordinal() == Game.PlayerType.HUMAN.ordinal()) {
//                System.out.println("Enter you next Move: ");
//
//                System.out.print("x1 : ");
//                int x1 = sc.nextInt();
//                System.out.print("y1 : ");
//                int y1 = sc.nextInt();
//                int stackNo;
//                if (x1 == -1 && y1 == -1) {
//                    System.out.print("stackNo : ");
//                    stackNo = sc.nextInt();
//                } else {
//                    stackNo = -1;
//                }
//                System.out.print("x2 : ");
//                int x2 = sc.nextInt();
//                System.out.print("y2 : ");
//                int y2 = sc.nextInt();
//
//                if (stackNo<3 && game.setCurrentGameMove(x1, y1, x2, y2, stackNo))
//                    game.switchTurn();
//                else {
//                    System.out.println("invalid move try again");
//                }
//                game.getBoard().printBoard();
//            } else {
//                GameMove move = game.getComputerMove();
//                System.out.println("Computer Move: ");
//                System.out.println("Gobblet: " + move.getGobblet().getGobbletSize().ordinal() + " (" + move.getGobblet().getX() + "," + move.getGobblet().getY() + ") -> (" + move.getX() + "," + move.getY() + ")");
//                game.switchTurn();
//                game.getBoard().printBoard();
//            }
//        }
//        System.out.println("Winner is " + game.getWinner().name);
//    }
//}