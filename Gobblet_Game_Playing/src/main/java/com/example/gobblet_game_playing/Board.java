package com.example.gobblet_game_playing;

import java.util.Stack;

import static com.example.gobblet_game_playing.GobbletColor.*;

public class Board {

    /* 2d array of stack<Gobblet> */
    private Stack<Gobblet>[][] board;
    private Stack<Gobblet>[][] playersGobblets;
    private boolean isCriticalState;

    /* 4 stacks each has 4 gobblets*/
    public Board() {

        GobbletColor[] gobbletColors = GobbletColor.values();

        board = new Stack[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = new Stack<>();
            }
        }

        playersGobblets = new Stack[2][3];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                this.playersGobblets[i][j] = new Stack<>();
                this.playersGobblets[i][j].push(new Gobblet(gobbletColors[i], GobbletSize.SIZE_1, -1, -1));
                this.playersGobblets[i][j].push(new Gobblet(gobbletColors[i], GobbletSize.SIZE_2, -1, -1));
                this.playersGobblets[i][j].push(new Gobblet(gobbletColors[i], GobbletSize.SIZE_3, -1, -1));
                this.playersGobblets[i][j].push(new Gobblet(gobbletColors[i], GobbletSize.SIZE_4, -1, -1));
            }
        }

        this.isCriticalState = false;

    }

    public Board(Board board) {

        this.board = new Stack[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.board[i][j] = new Stack<>();
                for (int z = 0; z < board.getBoard()[i][j].size(); z++) {
                    Gobblet gobblet = new Gobblet(board.getBoard()[i][j].elementAt(z));
                    this.board[i][j].push(gobblet);
                }
//                this.board[i][j].addAll(board.getBoard()[i][j]);
            }
        }
        this.playersGobblets = new Stack[2][3];
        this.playersGobblets[0] = new Stack[3];
        this.playersGobblets[1] = new Stack[3];
        for (int i = 0; i < 3; i++) {
            this.playersGobblets[0][i] = new Stack<>();
            for (int j = 0; j < board.getPlayersGobblets()[0][i].size(); j++) {
                Gobblet gobblet = new Gobblet(board.getPlayersGobblets()[0][i].elementAt(j));
                this.playersGobblets[0][i].push(gobblet);
            }

            this.playersGobblets[1][i] = new Stack<>();
            for (int j = 0; j < board.getPlayersGobblets()[1][i].size(); j++) {
                Gobblet gobblet = new Gobblet(board.getPlayersGobblets()[1][i].elementAt(j));
                this.playersGobblets[1][i].push(gobblet);
            }

        }

        this.isCriticalState = false;

    }


//    public GobbletColor isWinningState(GobbletColor gobbletColor) {
//        if (checkWinner(BLACK) == BLACK && checkWinner(WHITE) == WHITE) {
//            switch (gobbletColor) {
//                case WHITE:
//                    return BLACK;
//                case BLACK:
//                    return WHITE;
//                default:
//                    return null;
//            }
//        }
//        GobbletColor winnerColor = checkWinner(gobbletColor);
//        if (winnerColor != null) {
//            return winnerColor;
//        }
//
//        return null;
//    }
//
//
//    /**
//     * to check for passed color if winner by completing a row, a column or a diagonal
//     * @param gobbletColor
//     * @return
//     */
//    private GobbletColor checkWinner(GobbletColor gobbletColor) {
//        /* rows checking */
//        GobbletColor winningColor = null;
//
//        for (int row = 0; row < 4; row++) {
//            winningColor = checkLine(row, 0, row, 3);
//            if (winningColor != null && winningColor == gobbletColor) {
//                return winningColor;
//            }
//        }
//
//        /* columns checking */
//        for (int col = 0; col < 4; col++) {
//            winningColor = checkLine(0, col, 3, col);
//            if (winningColor != null && winningColor == gobbletColor) {
//                return winningColor;
//            }
//        }
//
//        /*  2 diagonals checking */
//        winningColor = checkLine(0, 0, 3, 3);
//        if (winningColor != null && winningColor == gobbletColor) {
//            return winningColor;
//        }
//
//        winningColor = checkLine(0, 3, 3, 0);
//        if (winningColor != null && winningColor == gobbletColor) {
//            return winningColor;
//        }
//
//        return null;
//    }
//
//    /**
//     * Helper method to check if there is a line of four gobblets with the same color
//     *
//     * @param startX
//     * @param startY
//     * @param endX
//     * @param endY
//     * @return GobbletColor
//     */
//    private GobbletColor checkLine(int startX, int startY, int endX, int endY) {
//        Gobblet firstGobblet = getFront(startX, startY);
//
//        if (firstGobblet == null) {
//            return null;
//        }
//
//        Gobblet currentGobblet = null;
//        for (int i = startX; i <= endX; i++) {
//            for (int j = startY; j <= endY; j++) {
//                currentGobblet = getFront(i, j);
//                if (currentGobblet == null || (currentGobblet.getGobbletColor() != firstGobblet.getGobbletColor())) {
//                    return null;
//                }
//            }
//        }
//        if (currentGobblet == null || (currentGobblet.getGobbletColor() != firstGobblet.getGobbletColor())) {
//            return null;
//        }
//
//        return firstGobblet.getGobbletColor();
//    }
//


    GobbletColor isWinningState(GobbletColor myGobbletColor){
        GobbletColor opponentGobbletColor = (myGobbletColor == WHITE)? BLACK:WHITE;
        boolean winFlag = false;
        boolean loseFlag = false;
        int myScore;
        int opponentScore;

        for(int i = 0; i < 4; i++) {
            myScore = 4;
            opponentScore = 4;
            for (int j = 0; j < 4; j++) {
                if (this.getFront(i, j) != null){
                    if(this.getFront(i, j).getGobbletColor() == myGobbletColor) {
                        myScore--;
                    }else if(this.getFront(i, j).getGobbletColor() == opponentGobbletColor){
                        opponentScore--;
                    }
                }
            }

            if(myScore == 0)
                winFlag = true;
            if(opponentScore == 0)
                loseFlag = true;
        }

        if(winFlag && loseFlag){
            return opponentGobbletColor;
        }else if(loseFlag){
            return opponentGobbletColor;
        } else if (winFlag) {
            return  myGobbletColor;
        }

        for(int j = 0; j < 4; j++) {
            myScore = 4;
            opponentScore = 4;
            for (int i = 0; i < 4; i++) {
                if (this.getFront(i, j) != null){
                    if(this.getFront(i, j).getGobbletColor() == myGobbletColor) {
                        myScore--;
                    }else if(this.getFront(i, j).getGobbletColor() == opponentGobbletColor){
                        opponentScore--;
                    }
                }
            }

            if(myScore == 0)
                winFlag = true;
            if(opponentScore == 0)
                loseFlag = true;

        }

        if(winFlag && loseFlag){
            return opponentGobbletColor;
        }else if(loseFlag){
            return opponentGobbletColor;
        } else if (winFlag) {
            return  myGobbletColor;
        }

        myScore = 4;
        opponentScore = 4;
        for (int i = 0; i < 4; i++) {
            if (this.getFront(i, i) != null){
                if(this.getFront(i, i).getGobbletColor() == myGobbletColor) {
                    myScore--;
                }else if(this.getFront(i, i).getGobbletColor() == opponentGobbletColor){
                    opponentScore--;
                }
            }
        }

        if(myScore == 0)
            winFlag = true;
        if(opponentScore == 0)
            loseFlag = true;


        myScore = 4;
        opponentScore = 4;
        for (int i = 0; i < 4; i++) {
            if (this.getFront(i, 3 - i) != null){
                if(this.getFront(i, 3 - i).getGobbletColor() == myGobbletColor) {
                    myScore--;
                }else if(this.getFront(i, 3 - i).getGobbletColor() == opponentGobbletColor){
                    opponentScore--;
                }
            }
        }

        if(myScore == 0)
            winFlag = true;
        if(opponentScore == 0)
            loseFlag = true;


        if(winFlag && loseFlag){
            return opponentGobbletColor;
        }else if(loseFlag){
            return opponentGobbletColor;
        } else if (winFlag) {
            return  myGobbletColor;
        }

        return null;
    }



    /**
     * edit the 2d array in the board
     * move the gobblet from x1,y1 to x2,y2
     * -1,-1 means outside the board
     * should pop from the gobblet stacks
     *
     * @param move
     * @return void
     */
    void playRound(GameMove move, Game.Turn currentTurn) {
        // boolean onBoard = false;
        Gobblet gobblet = move.getGobblet();
        int x1 = gobblet.getX();
        int y1 = gobblet.getY();
        int x2 = move.getX();
        int y2 = move.getY();

        /* If moving an existing gobblet, pop it from the original position */
        if (x1 != -1 && y1 != -1) {
            // onBoard = true;
            if (getFront(x1, y1) == null) {
                System.out.println("null");
            }
            gobblet = board[x1][y1].pop();

        } else {
            playersGobblets[currentTurn.ordinal()][move.getStackNo()].pop();
        }

        /* Place the gobblet at the new position */
        gobblet.setX(x2);
        gobblet.setY(y2);
        board[x2][y2].push(gobblet);
        Game.turnStartTime =  System.currentTimeMillis();

    }

    /**
     * to get peek front gobblet at certain position
     *
     * @param x
     * @param y
     * @return Gobblet
     */
    Gobblet getFront(int x, int y) {
        if (x >= 0 && x < 4 && y >= 0 && y < 4) {
            if (board[x][y].isEmpty()) {
                return null;
            } else {
                return board[x][y].peek();
            }
        } else {
            System.out.println("Board: Invalid Position");
            return null;
        }
    }

    void printBoard() {

        for (int i = 0; i < board.length; i++) {

            if (i < 3 && !playersGobblets[0][i].isEmpty() && playersGobblets[0][i].peek().getGobbletColor().ordinal() == 0) {
                System.out.print("w" + playersGobblets[0][i].peek().getGobbletSize().ordinal() + "    ");
            } else {
                System.out.print("      ");
            }

            for (int j = 0; j < board.length; j++) {
                if (!board[i][j].isEmpty())
                    if (board[i][j].peek().getGobbletColor().ordinal() == WHITE.ordinal()) {
                        System.out.print("w" + board[i][j].peek().getGobbletSize().ordinal() + " ");

                    } else {
                        System.out.print("b" + board[i][j].peek().getGobbletSize().ordinal() + " ");
                    }
                else {
                    System.out.print("-1 ");
                }
            }

            if (i < 3 && !playersGobblets[1][i].isEmpty() && playersGobblets[1][i].peek().getGobbletColor().ordinal() == 1) {
                System.out.print("    b" + playersGobblets[1][i].peek().getGobbletSize().ordinal());
            } else {
                System.out.print("  ");
            }

            System.out.println();

        }
    }

    public Stack<Gobblet>[][] getBoard() {
        return board;
    }

    public Stack<Gobblet>[][] getPlayersGobblets() {
        return playersGobblets;
    }

    public boolean isCriticalState() {
        return isCriticalState;
    }

    public void setCriticalState(boolean criticalState) {
        isCriticalState = criticalState;
    }
}