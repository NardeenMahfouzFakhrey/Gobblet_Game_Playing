package com.example.gobblet_game_playing;

public class GameMove {
    private Gobblet gobblet;
    private int x;
    private int y;

    public GameMove(Gobblet gobblet, int x, int y) {
        this.gobblet = gobblet;
        this.x = x;
        this.y = y;

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

    
}
