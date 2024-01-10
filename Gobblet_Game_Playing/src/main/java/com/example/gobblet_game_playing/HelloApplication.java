package com.example.gobblet_game_playing;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    static Stage primaryStage;
    static Stage startStage;
    @Override
    public void start(Stage primaryStage) {
        startStage = primaryStage;
        StartGameGUI.GameStart(startStage);
    }
    public static void openPrimaryStage() {
        primaryStage = new Stage();
        startStage.close();
        resetGame();
        primaryStage.show();

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
        BoardGUI.DrawBoard(primaryStage);
        BoardControllerGUI.restartHandler();

        if(type1 != Game.PlayerType.COMPUTER) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    controllerGUI.InitUi(BoardGUI.blackImages[i][j], "B", i, j);
                }
            }
            BoardControllerGUI.buttonsController();
        }

        if(type2 != Game.PlayerType.COMPUTER) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    controllerGUI.InitUi(BoardGUI.whiteImages[i][j], "W", i, j);
                }
            }
            BoardControllerGUI.buttonsController();
        }

        if(type2== Game.PlayerType.COMPUTER && type1== Game.PlayerType.COMPUTER) {
            BoardGUI.computerVsComputer();
        }
    }

    public static void main(String[] args) {
        launch();
    } }


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
//        Game game = new Game(type1, "karine", Game.Difficulty.NORMAL, type2, "Tantawy", Game.Difficulty.HARD);
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