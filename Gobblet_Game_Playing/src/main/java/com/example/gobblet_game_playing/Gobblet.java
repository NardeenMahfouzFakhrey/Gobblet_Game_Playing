package com.example.gobblet_game_playing;

public class Gobblet {

    private GobbletColor gobbletColor;
    private GobbletSize gobbletSize;
    private int x;
    private int y;

    public Gobblet(GobbletColor gobbletColor, GobbletSize gobbletSize, int x, int y) {
        this.gobbletColor = gobbletColor;
        this.gobbletSize = gobbletSize;
        this.x = x;
        this.y = y;
    }

    public Gobblet(Gobblet gobblet) {
        this.gobbletColor = gobblet.getGobbletColor();
        this.gobbletSize = gobblet.getGobbletSize();
        this.x = gobblet.getX();
        this.y = gobblet.getY();
    }

    public GobbletColor getGobbletColor() {
        return gobbletColor;
    }

    public void setGobbletColor(GobbletColor gobbletColor) {
        this.gobbletColor = gobbletColor;
    }

    public GobbletSize getGobbletSize() {
        return gobbletSize;
    }

    public void setGobbletSize(GobbletSize gobbletSize) {
        this.gobbletSize = gobbletSize;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }


}
