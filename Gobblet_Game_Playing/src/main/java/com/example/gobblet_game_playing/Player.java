package com.example.gobblet_game_playing;

import java.util.Stack;

public abstract class Player {

    protected String name;
//
//    protected Stack<Gobblet>[] gobblets;
    protected GobbletColor playerColor;
    public Game.Turn[] turns = Game.Turn.values();
    private GobbletColor[] gobbletColors = GobbletColor.values();
    private Game.Turn myTurn;

    public String getName() {
        return name;
    }

//    public Stack<Gobblet>[] getGobblets() {
//        return gobblets;
//    }

    public Player(String name, GobbletColor gobbletColor) {
        this.name = name;
//        this.gobblets = new Stack[4];
        this.playerColor = gobbletColor;
        myTurn = turns[playerColor.ordinal()];

//        for(int i = 0; i < gobblets.length; i++){
//            this.gobblets[i] = new Stack<>();
//        }
//
//
//        for(int i = 0; i < gobblets.length; i++){
//
//            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_1, -1, -1));
//            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_2, -1, -1));
//            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_3, -1, -1));
//            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_4, -1, -1));
//
//        }
    }


    // Valid Move Rules
    // 1. Empty cell -> move right away
    // 2. Not Empty cell && Gobblet is on the board -> Check only the size
    // 3. Not Empty cell && Gobblet is off the board and if the opposite Gobblet color is included in a row of 3 -> valid move
    //
    //                   0 1 2 3
    //
    //                0  3 2 2 3
    //                1  2 3 3 2
    //                2  2 3 3 2
    //                3  3 2 2 3

    private boolean checkNeighboringGobblets(Board board, GameMove move) {

        GobbletColor gobbletColor = move.getGobblet().getGobbletColor();
        int x = move.getX();
        int y = move.getY();

        int counter = 0;

        for (int j = 0; j < 4; j++) {

            if (board.getFront(x, j)!= null && board.getFront(x, j).getGobbletColor() != gobbletColor) {
                counter++;
            }
            if (counter == 3) {
                return true;
            }
        }
        counter = 0;

        for (int i = 0; i < 4; i++) {

            if (board.getFront(i, y) != null && board.getFront(i, y).getGobbletColor() != gobbletColor) {
                counter++;
            }
            if (counter == 3) {
                return true;
            }
        }


        counter = 0;

        if (x == y) {

            for (int i = 0; i < 4; i++) {

                if (board.getFront(i, i) != null && board.getFront(i, i).getGobbletColor() != gobbletColor) {
                    counter++;
                }
                if (counter == 3) {
                    return true;
                }

            }

            counter = 0;
        }
        if (x == (3 - y)) {

            for (int i = 0; i < 4; i++) {
                if (board.getFront(i, 3 - i) != null && board.getFront(i, 3 - i).getGobbletColor() != gobbletColor) {
                    counter++;
                }
                if (counter == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidMove(GameMove move, Board board, Game.Turn turn){

        boolean isValid = true;
        boolean isOffBoard = false;

        if(move.getGobblet().getGobbletColor().ordinal() != gobbletColors[turn.ordinal()].ordinal()){
            return false;
        }

        Gobblet substitutedGobblet = board.getFront(move.getX(), move.getY());

        if (substitutedGobblet == null) {
            return isValid;
        }

        if (move.getGobblet().getGobbletSize().compareTo(substitutedGobblet.getGobbletSize()) <= 0) {
            isValid = false;
            return isValid;

        }

        if (move.getGobblet().getX() == -1 && move.getGobblet().getY() == -1) {

            if(!board.getPlayersGobblets()[myTurn.ordinal()][move.getStackNo()].isEmpty() && board.getPlayersGobblets()[myTurn.ordinal()][move.getStackNo()].peek().getGobbletSize() == move.getGobblet().getGobbletSize()){
                isOffBoard = true;
            }else{
                return false;
            }
        }

        if (isOffBoard) {

            isValid = checkNeighboringGobblets(board, move);

        }

        return isValid;

    }

}
