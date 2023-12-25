package com.example.gobblet_game_playing;
import com.example.gobblet_game_playing.Game.Difficulty;

import java.util.ArrayList;
import java.util.Stack;

public class ComputerPlayer extends Player{

    private Difficulty difficulty;
    private GobbletColor[] gobbletColors = GobbletColor.values();
    private Game.Turn[] turns = Game.Turn.values();
    private GameMove bestMove;
    private int searchDepth;
    public int counter = 0;
    public ComputerPlayer(String name, GobbletColor gobbletColor, Difficulty difficulty)
    {
        super(name, gobbletColor);
        this.difficulty = difficulty;
        searchDepth = 4;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    // Returns move played by the computer
    public GameMove playGobbletMove(Board board) {
        minMax(board, turns[playerColor.ordinal()], searchDepth, true);
        return bestMove;
    }


    public int minMax(Board board, Game.Turn turn, int depth, boolean isMaximizingPlayer){

        if (depth == 0 /* || board.isWinningState() */) {
            counter++;
            return evaluation(board, gobbletColors[(turn.ordinal()+1)%2]);
        }

        Board myBoard = new Board(board);

        ArrayList<Board> possibleBoardStates = new ArrayList<>();
        ArrayList<GameMove> possibleMoves = new ArrayList<>();


        ArrayList<Boolean> flags = new ArrayList<>(4);
        /* Initialize the flags ArrayList */
        for (int i = 0; i < 4; i++) {
            flags.add(false);
        }
        for (int i = 0; i < 3; i++) {
            if (flags.get(myBoard.getPlayersGobblets()[turn.ordinal()][i].peek().getGobbletSize().ordinal())) {
                continue;
            } else {
                flags.set(myBoard.getPlayersGobblets()[turn.ordinal()][i].peek().getGobbletSize().ordinal(), true);
            }

            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    Gobblet gobblet = new Gobblet(myBoard.getPlayersGobblets()[turn.ordinal()][i].peek());
                    GameMove possibleMove = new GameMove(gobblet, x, y, i);

                    if (this.isValidMove(possibleMove, myBoard, turn)) {

                        Board newBoardState = new Board(myBoard);
                        possibleMoves.add(new GameMove(possibleMove));
                        newBoardState.playRound(possibleMove, turn);
                        possibleBoardStates.add(newBoardState);

//                        System.out.println(depth);
//                        newBoardState.printBoard();
//                        System.out.println();
//
//                        if(turn.ordinal() == Game.Turn.A.ordinal()){
//                            minMax(newBoardState, Game.Turn.B ,depth-1, !isMaximizingPlayer);
//                        }else{
//                            minMax(newBoardState, Game.Turn.A ,depth-1, !isMaximizingPlayer);
//                        }
                    }
                }
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                /* There is no Gobblet to move or its the other Player's Gobblet */
                if(myBoard.getFront(x, y) == null || myBoard.getFront(x, y).getGobbletColor().ordinal() != gobbletColors[turn.ordinal()].ordinal()){
                    continue;
                }


                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {

                        if(x == i && y == j){
                            continue;
                        }
                        Gobblet gobblet = new Gobblet(myBoard.getFront(x, y));


                        GameMove possibleMove = new GameMove(gobblet, i, j, -1);

                        if (this.isValidMove(possibleMove, myBoard, turn)) {

                            Board newBoardState = new Board(myBoard);
                            possibleMoves.add(new GameMove(possibleMove));
                            newBoardState.playRound(possibleMove, turn);

//                            System.out.println(depth);
//                            newBoardState.printBoard();
//                            System.out.println();
//
//                            if(turn.ordinal() == Game.Turn.A.ordinal()){
//
//                                minMax(newBoardState, Game.Turn.B ,depth-1, !isMaximizingPlayer);
//                            }else{
//
//                                minMax(newBoardState, Game.Turn.A ,depth-1, !isMaximizingPlayer);
//                            }
                            possibleBoardStates.add(newBoardState);

                        }
                    }
                }
            }
        }

        if(isMaximizingPlayer){
            int maxVal = Integer.MIN_VALUE;
            int i = 0;
            for(Board boardState: possibleBoardStates){
//            System.out.println(depth);
//            boardState.printBoard();
//            System.out.println();
                int val;
                if(turn.ordinal() == Game.Turn.A.ordinal()){
                    val = minMax(boardState, Game.Turn.B ,depth-1, !isMaximizingPlayer);
                }else{
                    val = minMax(boardState, Game.Turn.A ,depth-1, !isMaximizingPlayer);
                }

                if(val>maxVal){
                    if(depth == searchDepth)
                        bestMove = possibleMoves.get(i);
                    maxVal = val;
                }
                i++;
            }
            return maxVal;
        }else{
            int minVal = Integer.MAX_VALUE;
            for(Board boardState: possibleBoardStates){
//            System.out.println(depth);
//            boardState.printBoard();
//            System.out.println();
                int val;
                if(turn.ordinal() == Game.Turn.A.ordinal()){
                    val = minMax(boardState, Game.Turn.B ,depth-1, !isMaximizingPlayer);
                }else{
                    val = minMax(boardState, Game.Turn.A ,depth-1, !isMaximizingPlayer);
                }
                minVal = Math.min(minVal, val);
            }
            return minVal;
        }
    }
    public int evaluation(Board board, GobbletColor myColor){

        int myScore = 0;
        int OpponentScore = 0;

        for(int i = 0; i < 4; i++ ) {
            int myNewScore = 4;
            int OpponentNewScore = 4;
            for (int j = 0; j < 4; j++) {
                if (board.getFront(i, j) != null){
                    if(board.getFront(i, j).getGobbletColor() == myColor) {
                        myNewScore--;
                    }else{
                        OpponentNewScore--;
                    }
                }
            }
//            if(myNewScore == 0)
//                return Integer.MAX_VALUE;
//            if(OpponentNewScore == 1)
//                return Integer.MIN_VALUE;
            myScore += myNewScore;
            OpponentScore += OpponentNewScore;
        }

        for(int j =0; j<4; j++) {
            int myNewScore = 4;
            int OpponentNewScore = 4;
            for (int i = 0; i < 4; i++) {
                if (board.getFront(i, j) != null){
                    if(board.getFront(i, j).getGobbletColor() == myColor) {
                        myNewScore--;
                    }else{
                        OpponentNewScore--;
                    }
                }
            }
            //            if(myNewScore == 0)
//                return Integer.MAX_VALUE;
//            if(OpponentNewScore == 1)
//                return Integer.MIN_VALUE;
            myScore += myNewScore;
            OpponentScore += OpponentNewScore;
        }

        int myNewScore = 4;
        int OpponentNewScore = 4;
        for (int i = 0; i < 4; i++) {
            if (board.getFront(i, i) != null){
                if(board.getFront(i, i).getGobbletColor() == myColor) {
                    myNewScore--;
                }else{
                    OpponentNewScore--;
                }
            }
        }
//            if(myNewScore == 0)
//                return Integer.MAX_VALUE;
//            if(OpponentNewScore == 1)
//                return Integer.MIN_VALUE;
        myScore += myNewScore;
        OpponentScore += OpponentNewScore;


        myNewScore = 4;
        OpponentNewScore = 4;
        for (int i = 0; i < 4; i++) {
            if (board.getFront(i, 3 - i) != null){
                if(board.getFront(i, 3 - i).getGobbletColor() == myColor) {
                    myNewScore--;
                }else{
                    OpponentNewScore--;
                }
            }
        }
        //            if(myNewScore == 0)
//                return Integer.MAX_VALUE;
//            if(OpponentNewScore == 1)
//                return Integer.MIN_VALUE;
        myScore += myNewScore;
        OpponentScore += OpponentNewScore;


        return OpponentScore-myScore;
    }
}

