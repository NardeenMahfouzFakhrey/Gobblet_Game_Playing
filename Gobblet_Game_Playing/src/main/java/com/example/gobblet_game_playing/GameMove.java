package com.example.gobblet_game_playing;

public class GameMove {
    private Gobblet gobblet;
    private int x;
    private int y;
    private int stackNo;

    public GameMove(Gobblet gobblet, int x, int y, int stackNo) {
        this.gobblet = gobblet;
        this.x = x;
        this.y = y;
        this.stackNo = stackNo;
    }

    public GameMove(GameMove move) {
        this.gobblet = new Gobblet(move.getGobblet());
        this.x = move.getX();
        this.y = move.getY();
        this.stackNo = move.getStackNo();
    }

    public Gobblet getGobblet() {
        return gobblet;
    }

    public void setGobblet(Gobblet gobblet) {
        this.gobblet = gobblet;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getStackNo() {
        return stackNo;
    }

    public void setStackNo(int stackNo) {
        this.stackNo = stackNo;
    }
}
