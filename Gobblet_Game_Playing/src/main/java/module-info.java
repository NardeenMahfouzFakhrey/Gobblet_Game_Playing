module com.example.gobblet_game_playing {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gobblet_game_playing to javafx.fxml;
    exports com.example.gobblet_game_playing;
}