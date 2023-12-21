package com.example.gobblet_game_playing;




public class Game {

    private Board board;
    private Player winner;
    private PlayerPair players;

    enum Turn{
        A,
        B
    };

    enum Difficulty{
        EASY,
        NORMAL,
        HARD
    };


    private long turnStartTime;

    private long turnTimeLimitMillis;

    private Turn currentTurn;

    private Difficulty gameDifficulty;

    /** setters and getters
     */

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }


    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }


    public PlayerPair getPlayers() {
        return players;
    }

    public void setPlayers(PlayerPair pair) {
        this.players = pair;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public void setDifficulty(Difficulty difficulty){
        gameDifficulty = difficulty;
    }

    public Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setTurnStartTime(long turnStartTime) {
        this.turnStartTime = turnStartTime;
    }


    /**
     * switch turns between A and B
     */
    public void switchTurn() {
        currentTurn = (currentTurn == Turn.A) ? Turn.B : Turn.A;
    }


    /**
     * check if game ends by winning
     * @return boolean
     */
    public boolean isGameEnded() {
        return board.isWinningState();
    }


    /**
     *  Set the time limit for each turn
     * @param seconds
     */
    public void setTurnTimeLimit(int seconds) {
        turnTimeLimitMillis = seconds * 1000L; // Convert seconds to milliseconds
    }

    /**
     * Check if the current turn has exceeded the time limit
     * @return boolean
     */
    public boolean isTurnTimeLimitExceeded() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - turnStartTime) > turnTimeLimitMillis;
    }
}
