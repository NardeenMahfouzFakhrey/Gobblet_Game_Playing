package com.example.gobblet_game_playing;

import java.util.Stack;

public class Board {

    //2d array of stack<Gobblet>
    private Stack<Gobblet>[][] board;

    public Board() {
        board = new Stack[4][4];
    }

    //boolean isWinningState(){}
    void playRound(GameMove move){
        //edit the 2d array in the board
        //move the gobblet from x1,y1 to x2,y2
        //-1,-1 means outside the board
    }

    Gobblet getFront(int x, int y){
        if(x>=0 && x<4 && y>=0 && y<4){
            if(board[x][y].isEmpty()){
                return null;
            }else{
                return board[x][y].peek();
            }
        }else{
            System.out.println("Board: Invalid Position");
            return null;
        }
    }
}
