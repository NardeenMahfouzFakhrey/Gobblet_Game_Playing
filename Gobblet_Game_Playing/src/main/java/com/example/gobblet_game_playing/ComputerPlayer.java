package com.example.gobblet_game_playing;
import com.example.gobblet_game_playing.Game.Difficulty;
public class ComputerPlayer extends Player{

    private Difficulty difficulty;
    public ComputerPlayer(String name, GobbletColor gobbletColor, Difficulty difficulty)
    {
        super(name, gobbletColor);
        this.difficulty = difficulty;
    }

    // Returns move played by the computer
    public GameMove playGobbletMove() {
        return null;
    }


}
