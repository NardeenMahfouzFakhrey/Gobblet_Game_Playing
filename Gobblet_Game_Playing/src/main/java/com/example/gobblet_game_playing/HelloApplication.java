package com.example.gobblet_game_playing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

//    public static void main(String[] args) {
//        launch();
//    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Game.PlayerType type1 = Game.PlayerType.HUMAN;
        Game.PlayerType type2 = Game.PlayerType.HUMAN;

        Game.PlayerType players[] = new Game.PlayerType[2];
        players[0] = type1;
        players[1] = type2;
        //Game.Turn turn = Game.Turn.A;

        Game game = new Game(type1, "karine", type2, "Tantawy", null);

        System.out.println("Start Game");

        while(true){
            if(players[game.getCurrentTurn().ordinal()].ordinal() == Game.PlayerType.HUMAN.ordinal()){
                System.out.println("Enter you next Move: ");

                System.out.print("x1 : ");
                int x1 = sc.nextInt();
                System.out.print("y1 : ");
                int y1 = sc.nextInt();
                int stackNo;
                if(x1==-1 && y1==-1) {
                    System.out.print("stackNo : ");
                    stackNo = sc.nextInt();
                }else{
                    stackNo = -1;
                }
                System.out.print("x2 : ");
                int x2 = sc.nextInt();
                System.out.print("y2 : ");
                int y2 = sc.nextInt();

                if(game.setCurrentGameMove(x1, y1, x2, y2, stackNo))
                    game.switchTurn();
                else{
                    System.out.println("invalid move try again");
                }
                game.getBoard().printBoard();
            }else{

            }

        }

//        System.out.println("Winner is " + game.getWinner().name);
    }
}