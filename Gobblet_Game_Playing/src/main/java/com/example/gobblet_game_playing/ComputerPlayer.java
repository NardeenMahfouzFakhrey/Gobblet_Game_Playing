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

        if(difficulty.ordinal() == 0){
            searchDepth = 1;

        }
        else if(difficulty.ordinal() == 1){
            searchDepth = 2;
        }
        else{
            searchDepth = 5;
        }

    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    // Returns move played by the computer
    public GameMove playGobbletMove(Board board) {
        alphaBeta(board, turns[playerColor.ordinal()], Integer.MIN_VALUE, Integer.MAX_VALUE, searchDepth, true);
        return bestMove;
    }


    Board generateBoardState(Board board, GameMove possibleMove, Game.Turn turn){

        Board newBoardState = new Board(board);
        newBoardState.playRound(new GameMove(possibleMove), turn);
        return newBoardState;

    }

      ArrayList<GameMove> generatePossibleMoves(Board board, Game.Turn turn){

        ArrayList<GameMove> possibleMoves = new ArrayList<>();
        ArrayList<Boolean> flags = new ArrayList<>(4);
        Board myBoard = new Board(board);

        /* Initialize the flags ArrayList */
        for (int i = 0; i < 4; i++) {
            flags.add(false);
        }
        for (int i = 0; i < 3; i++) {

            if(myBoard.getPlayersGobblets()[turn.ordinal()][i].isEmpty()){
                continue;
            }

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

                        possibleMoves.add(new GameMove(possibleMove));

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

                            possibleMoves.add(new GameMove(possibleMove));

                        }
                    }
                }
            }
        }

        return possibleMoves;
    }


    public int alphaBeta(Board board, Game.Turn turn, int alpha, int beta, int depth, boolean isMaximizingPlayer){

        if (depth == 0  || board.isWinningState(gobbletColors[(turn.ordinal()+1)%2]) != null || board.isWinningState(gobbletColors[(turn.ordinal())]) != null) {
            counter++;
            int rating = evaluation(board, gobbletColors[(turn.ordinal()+1)%2], depth);
            return rating;
        }

        ArrayList<GameMove> possibleMoves = generatePossibleMoves(board, turn);

        Board boardState;

        if(isMaximizingPlayer){
            int maxVal = Integer.MIN_VALUE;
            int i = 0;
            for(GameMove move: possibleMoves){

                boardState = generateBoardState(board, move, turn);

                if((evaluation(boardState, gobbletColors[(turn.ordinal())], depth) == Integer.MIN_VALUE)){
                    return Integer.MAX_VALUE;
                }

//                System.out.println("Possible State");

//                if(depth == 0){
//                    boardState.printBoard();
//                    System.out.println("Evaluation: " + this.evaluation(boardState, gobbletColors[turn.ordinal()], depth));
//                }


                int val;
                if(turn.ordinal() == Game.Turn.A.ordinal()){
                    val = alphaBeta(boardState, Game.Turn.B, alpha, beta, depth-1, !isMaximizingPlayer);
                }else{
                    val = alphaBeta(boardState, Game.Turn.A, alpha, beta, depth-1, !isMaximizingPlayer);
                }

                if(val > maxVal){
                    if(depth == searchDepth) {
                        bestMove = possibleMoves.get(i);
                        System.out.println("here");
                    }
                    maxVal = val;
                }

                alpha = Math.max(alpha, val);

                if(beta <= alpha){
                    break;
                }

                i++;
            }
            return maxVal;
        }else{
            int minVal = Integer.MAX_VALUE;
            for(GameMove move: possibleMoves){

                boardState = generateBoardState(board, move, turn);

                if(evaluation(boardState, gobbletColors[(turn.ordinal())], depth) == (int) (Integer.MAX_VALUE * 0.1 * (depth + 1))){
                    return Integer.MIN_VALUE;
                }


//                if(depth == searchDepth){
//                    boardState.printBoard();
//                    System.out.println("Evaluation: " + this.evaluation(boardState, gobbletColors[turn.ordinal()], depth));
//                }

//                System.out.println("Possible State");
//                boardState.printBoard();
//                System.out.println("Evaluation: " + this.evaluation(boardState, gobbletColors[turn.ordinal()]));

                int val;
                if(turn.ordinal() == Game.Turn.A.ordinal()){
                    val = alphaBeta(boardState, Game.Turn.B, alpha, beta, depth-1, !isMaximizingPlayer);
                }else{
                    val = alphaBeta(boardState, Game.Turn.A, alpha, beta, depth-1, !isMaximizingPlayer);
                }
                minVal = Math.min(minVal, val);

                beta = Math.min(beta, val);

                if(beta <= alpha){
                    break;
                }
            }
            return minVal;
        }
    }



    /*
     * 1. How many of my gobblet in a line
     * 2. How many of opponents gobblets in that same line
     * 3. sizes
     * 4. winning state     inf
     * 5. loosing state    -inf
     */
    public int evaluation(Board board, GobbletColor myColor, int depth){

        int myScore = 0;
        int opponentScore = 0;

        for(int i = 0; i < 4; i++) {
            int myNewScore = 4;
            int opponentNewScore = 4;
            for (int j = 0; j < 4; j++) {
                if (board.getFront(i, j) != null){
                    if(board.getFront(i, j).getGobbletColor() == myColor) {
                        myNewScore--;
                    }else{
                        opponentNewScore--;
                    }
                }
            }

            if(myNewScore == 0)
                return (int) (Integer.MAX_VALUE * 0.1 * (depth + 1));


            if(opponentNewScore == 1 && myNewScore == 4)
                return (int) (Integer.MIN_VALUE * 0.1 * (depth + 1));


            myScore += myNewScore;
            opponentScore += opponentNewScore;

        }


        for(int j = 0; j < 4; j++) {
            int myNewScore = 4;
            int opponentNewScore = 4;
            for (int i = 0; i < 4; i++) {
                if (board.getFront(i, j) != null){
                    if(board.getFront(i, j).getGobbletColor() == myColor) {
                        myNewScore--;
                    }else{
                        opponentNewScore--;
                    }
                }
            }


            if(myNewScore == 0)
                return (int) (Integer.MAX_VALUE * 0.1 * (depth + 1));


            if(opponentNewScore == 1 && myNewScore == 4)
                return (int) (Integer.MIN_VALUE * 0.1 * (depth + 1));

            myScore += myNewScore;
            opponentScore += opponentNewScore;
        }

        int myNewScore = 4;
        int opponentNewScore = 4;
        for (int i = 0; i < 4; i++) {
            if (board.getFront(i, i) != null){
                if(board.getFront(i, i).getGobbletColor() == myColor) {
                    myNewScore--;
                }else{
                    opponentNewScore--;
                }
            }
        }

        if(myNewScore == 0)
            return (int) (Integer.MAX_VALUE * 0.1 * (depth + 1));

        if(opponentNewScore == 1 && myNewScore == 4)
            return (int) (Integer.MIN_VALUE * 0.1 * (depth + 1));


        myScore += myNewScore;
        opponentScore += opponentNewScore;


        myNewScore = 4;
        opponentNewScore = 4;
        for (int i = 0; i < 4; i++) {
            if (board.getFront(i, 3 - i) != null){
                if(board.getFront(i, 3 - i).getGobbletColor() == myColor) {
                    myNewScore--;
                }else{
                    opponentNewScore--;
                }
            }
        }


        if(myNewScore == 0)
            return (int) (Integer.MAX_VALUE * 0.1 * (depth + 1));

        if(opponentNewScore == 1 && myNewScore == 4)
            return (int) (Integer.MIN_VALUE * 0.1 * (depth + 1));


        myScore += myNewScore;
        opponentScore += opponentNewScore;


        return opponentScore - myScore;
    }
}

