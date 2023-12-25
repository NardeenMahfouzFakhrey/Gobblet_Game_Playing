package com.example.gobblet_game_playing;




public class Game {

    private Board board;
    private Player winner;
    private PlayerPair players;
    private long turnStartTime;
    private long turnTimeLimitMillis;
    private Turn currentTurn;
    private GameMove currentGameMove;
    private Difficulty gameDifficulty;

    public enum Turn{
        A,
        B
    };

    public enum Difficulty{
        EASY,
        NORMAL,
        HARD
    };

    public enum PlayerType{
        HUMAN,
        COMPUTER
    };




    public Game(PlayerType t1, String name1, PlayerType t2, String name2, Difficulty gameDifficulty) {
        board = new Board();
        Player p1 = null;
        if(t1 == PlayerType.HUMAN){
            p1 = new HumanPlayer(name1, GobbletColor.WHITE);
        }else if(t1 == PlayerType.COMPUTER){
            this.gameDifficulty = gameDifficulty;
            p1 = new ComputerPlayer(name1, GobbletColor.WHITE, this.gameDifficulty);
        }

        Player p2 = null;
        if(t2 == PlayerType.HUMAN){
            p2 = new HumanPlayer(name2, GobbletColor.BLACK);
        }else if(t2 == PlayerType.COMPUTER){
            this.gameDifficulty = gameDifficulty;
            p2 = new ComputerPlayer(name2, GobbletColor.BLACK, this.gameDifficulty);
        }
        if(p1 != null && p2 != null){
            players = new PlayerPair(p1, p2);
        }else{
            System.out.println("ERROR: fail to create the players");
        }

        currentTurn = Turn.A;

    }



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

    public GameMove getCurrentGameMove() {
        return currentGameMove;
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

    public boolean setCurrentGameMove(int x1, int y1, int x2, int y2, int stackNo) {

        Gobblet gobblet;
        if(x1==-1 && y1==-1){
            if(currentTurn.ordinal()==0){
                 gobblet = board.getPlayersGobblets()[0][stackNo].peek();
            }else{
                 gobblet = board.getPlayersGobblets()[1][stackNo].peek();
            }
        }else if(!board.getBoard()[x1][y1].isEmpty()){
            gobblet = board.getBoard()[x1][y1].peek();
        }else{
            return false;
        }

        GameMove move = new GameMove(gobblet, x2, y2, stackNo);

        if(currentTurn.ordinal()==0){
            boolean flag = ((HumanPlayer) players.getPlayer1()).playGobbletMove(move, board);
            if(isGameEnded()){
                if(currentTurn.ordinal()==0){
                    winner =  players.getPlayer1();
                }else{
                    winner =  players.getPlayer2();
                }
            }
            return flag;
        }else{
            boolean flag = ((HumanPlayer) players.getPlayer2()).playGobbletMove(move, board);
            if(isGameEnded()){
                if(currentTurn.ordinal()==0){
                    winner =  players.getPlayer1();
                }else{
                    winner =  players.getPlayer2();
                }
            }
            return flag;
        }


    }

    public GameMove getComputerMove(){
        if(currentTurn.ordinal()==0){
            GameMove move = ((ComputerPlayer) players.getPlayer1()).playGobbletMove(this.board);
            if(isGameEnded()){
                if(currentTurn.ordinal()==0){
                    winner =  players.getPlayer1();
                }else{
                    winner =  players.getPlayer2();
                }
            }
            board.playRound(move, currentTurn);
            return move;
        }else{
            GameMove move = ((ComputerPlayer) players.getPlayer2()).playGobbletMove(this.board);
            if(isGameEnded()){
                if(currentTurn.ordinal()==0){
                    winner =  players.getPlayer1();
                }else{
                    winner =  players.getPlayer2();
                }
            }
            System.out.println(move.getGobblet().getX());
            System.out.println(move.getGobblet().getY());
            System.out.println(move.getX());
            System.out.println(move.getY());
            board.playRound(move, currentTurn);
            return move;
        }
    }
    public static void testGUI(int x1, int y1, int x2, int y2, int stackNo) {
        System.out.println("x1 = " + x1);
        System.out.println("y1 = " + y1);
        System.out.println("x2 = " + x2);
        System.out.println("y2 = " + y2);
        System.out.println("stack no = " + stackNo);
    }

}
