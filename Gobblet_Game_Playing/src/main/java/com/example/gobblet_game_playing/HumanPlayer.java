package com.example.gobblet_game_playing;

public class HumanPlayer extends Player {

    private GameMove currentMove = null;

    public HumanPlayer(String name, GobbletColor gobbletColor) {
        super(name, gobbletColor);
    }

    // Returns true if move was played and false otherwise
    public boolean playGobbletMove(GameMove move, Board board) {

        if(isValidMove(move, board)){

            board.playRound(move);
            return true;

        }
        return false;

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
        int x = move.getGobblet().getX();
        int y = move.getGobblet().getY();

        int counter = 0;

        for (int j = 0; j < 4; j++) {

            if (board.getFront(x, j).getGobbletColor() != gobbletColor) {
                counter++;
            }
            if (counter == 3) {
                return true;
            }
        }

        counter = 0;

        for (int i = 0; i < 4; i++) {

            if (board.getFront(i, y).getGobbletColor() != gobbletColor) {
                counter++;
            }
            if (counter == 3) {
                return true;
            }
        }


        counter = 0;

        if (x == y) {

            for (int i = 0; i < 4; i++) {

                if (board.getFront(i, i).getGobbletColor() != gobbletColor) {
                    counter++;
                }
                if (counter == 3) {
                    return true;
                }

            }

            counter = 0;

            if (x == (3 - y)) {

                for (int i = 0; i < 4; i++) {

                    System.out.println(i + " " + (3 - i));

                    if (board.getFront(i, 3 - i).getGobbletColor() != gobbletColor) {
                        counter++;
                    }
                    if (counter == 3) {
                        return true;
                    }

                }

            }


        }

        return false;
    }

    public boolean isValidMove(GameMove move, Board board){

        boolean isValid = true;
        boolean isOffBoard = false;

        Gobblet substitutedGobblet = board.getFront(move.getX(), move.getY());

        if (substitutedGobblet == null) {
            return isValid;
        }

        if (move.getGobblet().getGobbletSize().compareTo(substitutedGobblet.getGobbletSize()) <= 0) {
            isValid = false;
            return isValid;

        }

        if (move.getGobblet().getX() == -1 && move.getGobblet().getY() == -1) {
            isOffBoard = true;
        }

        if (isOffBoard) {

            isValid = checkNeighboringGobblets(board, move);

        }

        return isValid;

    }

}
