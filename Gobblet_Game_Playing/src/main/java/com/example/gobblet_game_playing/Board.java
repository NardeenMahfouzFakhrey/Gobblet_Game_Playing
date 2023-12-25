package com.example.gobblet_game_playing;

import java.util.Stack;

public class Board {

    /* 2d array of stack<Gobblet> */
    private Stack<Gobblet>[][] board;

    /* 4 stacks each has 4 gobblets*/
    public Board() {

        board = new Stack[4][4];
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                board[i][j] = new Stack<>();
            }
        }
    }

    /**
     * to check for any winner by completing a row, a column or a diagonal
     * @return boolean
     */
    boolean isWinningState() {
        /*rows checking */
        for (int row = 0; row < 4; row++) {
            if (checkLine(row, 0, row, 3)) {
                return true;
            }
        }

        /*columns checking */
        for (int col = 0; col < 4; col++) {
            if (checkLine(0, col, 3, col)) {
                return true;
            }
        }

        /*  2 diagonals checking */
        if (checkLine(0, 0, 3, 3) || checkLine(0, 3, 3, 0)) {
            return true;
        }

        return false;
    }


    /**
     * Helper method to check if there is a line of four gobblets with the same color
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    private boolean checkLine(int startX, int startY, int endX, int endY) {
        Gobblet firstGobblet = getFront(startX, startY);

        if (firstGobblet == null) {
            return false;
        }

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                Gobblet currentGobblet = getFront(i, j);

                if (currentGobblet == null || (currentGobblet.getGobbletColor()!=firstGobblet.getGobbletColor())) {
                    return false;
                }
            }
        }

        return true;
    }



    /**
     * edit the 2d array in the board
     * move the gobblet from x1,y1 to x2,y2
     * -1,-1 means outside the board
     * should pop from the gobblet stacks
     * @param move
     * @return void
     */
    void playRound(GameMove move) {
        // boolean onBoard = false;
        Gobblet gobblet = move.getGobblet();
        int x1 = gobblet.getX();
        int y1 = gobblet.getY();
        int x2 = move.getX();
        int y2 = move.getY();

        /* If moving an existing gobblet, pop it from the original position */
        if (x1 != -1 && y1 != -1) {
            // onBoard = true;
            gobblet = board[x1][y1].pop();
            System.out.println(x1);
            System.out.println(y1);
        }

        /* Place the gobblet at the new position */
        gobblet.setX(x2);
        gobblet.setY(y2);
        board[x2][y2].push(gobblet);

        /* todo if pop from player's stack is to be handled here:
        if(!onBoard){
            return gobblet;
        }
        return null;
    */
    }

    /**
     * to get peek front gobblet at certain position
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

    void printBoard(){

        for(int i = 0; i < board.length; i++){

            for(int j = 0; j < board.length; j++){
                if(!board[i][j].isEmpty())
                    System.out.print(board[i][j].peek().getGobbletSize().ordinal() + " ");
                else{
                    System.out.print("-1 ");
                }
            }
            System.out.println();

        }
    }

    public Stack<Gobblet>[][] getBoard() {
        return board;
    }

    public void setBoard(Stack<Gobblet>[][] board) {
        this.board = board;
    }
}
