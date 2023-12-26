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
    @Override
    public void start(Stage stage) throws IOException {
        BoardGUI.DrawBorad(stage);
        ControllerGUI controllerGUI = new ControllerGUI();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                controllerGUI.InitUi(BoardGUI.whiteImages[i][j], "W", i, j);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                controllerGUI.InitUi(BoardGUI.blackImages[i][j], "B", i, j);
            }
        }
    }

//    public static void main(String[] args) {
//        launch();
//    }

    public static void main(String[] args) throws InterruptedException {

        Scanner sc = new Scanner(System.in);

        Game.PlayerType type1 = Game.PlayerType.COMPUTER;
        Game.PlayerType type2 = Game.PlayerType.COMPUTER;

        Game.PlayerType players[] = new Game.PlayerType[2];
        players[0] = type1;
        players[1] = type2;
        //Game.Turn turn = Game.Turn.A;

        Game game = new Game(type1, "karine", type2, "Tantawy", Game.Difficulty.HARD);
        System.out.println("Start Game");

        while (!game.isGameEnded()) {


            if (players[game.getCurrentTurn().ordinal()].ordinal() == Game.PlayerType.HUMAN.ordinal()) {
                System.out.println("Enter you next Move: ");

                System.out.print("x1 : ");
                int x1 = sc.nextInt();
                System.out.print("y1 : ");
                int y1 = sc.nextInt();
                int stackNo;
                if (x1 == -1 && y1 == -1) {
                    System.out.print("stackNo : ");
                    stackNo = sc.nextInt();
                } else {
                    stackNo = -1;
                }
                System.out.print("x2 : ");
                int x2 = sc.nextInt();
                System.out.print("y2 : ");
                int y2 = sc.nextInt();

                if (game.setCurrentGameMove(x1, y1, x2, y2, stackNo))
                    game.switchTurn();
                else {
                    System.out.println("invalid move try again");
                }
                game.getBoard().printBoard();
            } else {
                GameMove move = game.getComputerMove();
                game.switchTurn();
//                System.out.println("Computer Move: ");
//                System.out.println("Gobblet: " + move.getGobblet().getGobbletSize().ordinal() + " (" + move.getGobblet().getX() + "," + move.getGobblet().getY() + ") -> (" + move.getX() + "," + move.getY() + ")");
                game.getBoard().printBoard();
                TimeUnit.SECONDS.sleep(1);
            }
        }

        System.out.println("Winner is " + game.getWinner().getName());

    }
}


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
//        //Game.Turn turn = Game.Turn.A;
//
//        Game game = new Game(type1, "karine", type2, "Tantawy", Game.Difficulty.HARD);
//        System.out.println("Start Game");
//
//        while (!game.isGameEnded()) {
//
//            GameMove move = game.getComputerMove();
//                game.switchTurn();
//
//            if (players[game.getCurrentTurn().ordinal()].ordinal() == Game.PlayerType.HUMAN.ordinal()) {
//
//                game.getBoard().printBoard();
//            } else {
//
//            }
//        }
//    }


//}
//    public static void main(String[] args) {
//
//
//        Game g = new Game(Game.PlayerType.COMPUTER, "karine", Game.PlayerType.HUMAN, "Tantawy", Game.Difficulty.HARD);
//        ((ComputerPlayer) g.getPlayers().getPlayer1()).setSearchDepth(4);
//        ((ComputerPlayer) g.getPlayers().getPlayer1()).minMax(g.getBoard(), Game.Turn.A, 4, true);
//
//        System.out.println();
//        System.out.println(((ComputerPlayer) g.getPlayers().getPlayer1()).counter);
//
//    }
//
//}