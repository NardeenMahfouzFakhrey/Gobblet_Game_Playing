package com.example.gobblet_game_playing;

import java.util.Stack;

public class HumanPlayer extends Player {

    private GameMove currentMove = null;
    public Game.Turn[] turns = Game.Turn.values();
    private Game.Turn myTurn = turns[playerColor.ordinal()];
    public HumanPlayer(String name, GobbletColor gobbletColor) {
        super(name, gobbletColor);
    }

    // Returns true if move was played and false otherwise
    public boolean playGobbletMove(GameMove move, Board board) {

        if(isValidMove(move, board, myTurn)){
            board.playRound(move, myTurn);
            return true;
        }
        return false;

    }




}
