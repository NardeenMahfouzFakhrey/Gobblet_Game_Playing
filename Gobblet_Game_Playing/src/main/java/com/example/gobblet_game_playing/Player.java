package com.example.gobblet_game_playing;

import java.util.Stack;

public abstract class Player {

    protected String name;

    protected Stack<Gobblet>[] gobblets;
    protected GobbletColor playerColor;

    public String getName() {
        return name;
    }

    public Stack<Gobblet>[] getGobblets() {
        return gobblets;
    }

    public Player(String name, GobbletColor gobbletColor) {
        this.name = name;
        this.gobblets = new Stack[4];
        this.playerColor = gobbletColor;

        for(int i = 0; i < gobblets.length; i++){
            this.gobblets[i] = new Stack<>();
        }


        for(int i = 0; i < gobblets.length; i++){

            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_1, -1, -1));
            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_2, -1, -1));
            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_3, -1, -1));
            gobblets[i].push(new Gobblet(gobbletColor, GobbletSize.SIZE_4, -1, -1));

        }
    }
}
