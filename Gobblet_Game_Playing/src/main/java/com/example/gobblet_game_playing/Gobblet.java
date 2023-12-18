package com.example.gobblet_game_playing;

public class Gobblet {

    private GobbletColor gobbletColor;
    private GobbletSize gobbletSize;

    public Gobblet(GobbletColor gobbletColor, GobbletSize gobbletSize) {
        this.gobbletColor = gobbletColor;
        this.gobbletSize = gobbletSize;
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

}
