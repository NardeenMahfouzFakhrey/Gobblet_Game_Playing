package com.example.gobblet_game_playing;

public class HumanPlayer extends Player {

    public Game.Turn[] turns = Game.Turn.values();

    /**
     * HumanPlayer
     * constructor
     */
    public HumanPlayer(String name, GobbletColor gobbletColor) {
        super(name, gobbletColor);
        myTurn = turns[playerColor.ordinal()];
    }


    /**
     * playGobbletMove
     * test and verify the passed move and play it on the board
     * @return boolean
     * true if the move was valid
     * false otherwise
     */
    public boolean playGobbletMove(GameMove move, Board board) {

        if(isValidMove(move, board, myTurn)){
            board.playRound(move, myTurn);
            return true;
        }
        return false;
    }

}
