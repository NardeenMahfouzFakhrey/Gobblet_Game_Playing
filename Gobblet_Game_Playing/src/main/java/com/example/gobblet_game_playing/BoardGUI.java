package com.example.gobblet_game_playing;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BoardGUI {
    static Button[][] button = new Button[4][4];
    static StackPane[][] buttonPanes = new StackPane[4][4];
    static VBox blackBox = new VBox();
    static VBox whiteBox = new VBox();
    static ImageView[][] blackImages = new ImageView[3][4];

    static ImageView[][] whiteImages = new ImageView[3][4];
    static HBox hbox;
    static int oldX = -1;
    static int oldY = -1;
    static int newX = -1;
    static int newY = -1;
    static int stack = -1;
    static GobbletImage[][] gobbletTransparent = new GobbletImage[2][3];

    static ImageView[][] transparentImage = new ImageView[2][3];



    public static void DrawBorad(Stage stage){
        String imagePath = "Gobblet_background.png";
        Image backgroundImage = new Image(BoardGUI.class.getResource(imagePath).toExternalForm());
        ImageView img = new ImageView(backgroundImage);

        img.fitHeightProperty().setValue(600);
        img.fitWidthProperty().setValue(750);

        BorderPane pane = new BorderPane();
        pane.setCenter(img);
        pane.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 0;");
        Pane buttonPane = placeButtons();
        pane.getChildren().add(buttonPane);

        DrawBlackGobblets();
        DrawWhiteGobblets();
        setTransparent();
        hbox = new HBox(blackBox,pane,whiteBox);


        // Create a Scene
        Scene scene = new Scene(hbox,1150,600);

        // Set the Scene for the Stage
        stage.setScene(scene);

        // Set the title of the Stage
        stage.setTitle("Gobblet AI Player");
        stage.setResizable(false);

        // Show the Stage
        stage.show();
    }

    public static void setTransparent(){
        for (int i=0; i<2 ; i++){
            for (int j=0; j<3 ; j++){
                gobbletTransparent[i][j] =new GobbletImage("Transparent",0,true);
                transparentImage[i][j] = BoardGUI.gobbletTransparent[i][j].getImgView();
            }
        }
    }
    public static Pane placeButtons(){

        for (int i=0 ; i<4;i++) {
            for (int j=0 ; j<4 ; j++) {
                button[i][j] = new Button();
                buttonPanes [i][j] = new StackPane();
            }
        }

        for (int i=0 ; i<4;i++){
            for (int j=0 ; j<4 ; j++) {
                buttonPanes[i][j].setTranslateX(55 + i*174);
                buttonPanes[i][j].setTranslateY(42 + j*140);
                buttonPanes[i][j].setPrefHeight(100);
                buttonPanes[i][j].setPrefWidth(115);
                button[i][j].setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                // Set up drag-and-drop for the button
                int finalI = i;
                int finalJ = j;

                ImageView imageView = new ImageView();
                buttonPanes[i][j].getChildren().add(button[i][j]);

                buttonPanes[i][j].setOnDragOver(event -> {
                    if (event.getGestureSource() != buttonPanes[finalI][finalJ] && event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }
                    event.consume();
                });

                buttonPanes[i][j].setOnDragDropped(event -> {
                    Dragboard dragboard = event.getDragboard();
                    boolean success = false;

                    if (dragboard.hasImage()) {
                        buttonPanes[finalI][finalJ].getChildren().remove(imageView);
                        imageView.setImage(dragboard.getImage());
                        buttonPanes[finalI][finalJ].getChildren().add(imageView);
                        System.out.println("Photo dropped on the button! " + "i = " + finalI + " j = " + finalJ);
                        success = true;
                        newX = finalI;
                        newY = finalJ;
                        if ((oldX == newX) && (oldY == newY) ){

                        }else {
                            Game.testGUI(oldX,oldY, newX, newY,stack);
                            stack = -1;
                        }
                        }

                    event.setDropCompleted(success);
                    event.consume();
                });

                imageView.setOnDragDetected(event -> {
                    System.out.println("Helloooo");
                    Dragboard dragboard = imageView.startDragAndDrop(TransferMode.ANY);

                    ClipboardContent content = new ClipboardContent();
                    content.putImage(imageView.getImage());

                    dragboard.setContent(content);
                    imageView.setImage(null);
                    event.consume();
                    oldX = finalI;
                    oldY = finalJ;
                    stack = -1;
                });

            }
        }


        Pane buttonPane = new Pane();
        for (int i=0 ; i<4 ; i++){
            buttonPane.getChildren().addAll(buttonPanes[i]);
        }
        return buttonPane;
    }

    public static void DrawBlackGobblets(){
        Label label = new Label("Player 1");
        label.setAlignment(Pos.TOP_CENTER);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        for(int i=0 ; i<3 ; i++){
            for (int j=0; j<4 ; j++){
                GobbletImage gobbletImage = new GobbletImage("B",j+1,true);
                blackImages[i][j] = gobbletImage.getImgView();
            }
        }

        blackBox.setBackground(Background.fill(Color.WHITE));
        blackBox.setPrefWidth(200);
        blackBox.setStyle("-fx-border-color: black; -fx-border-width: 1 1 0 0;"); // top right bottom left

        blackBox.getChildren().add(label);
        blackBox.setMargin(label, new Insets(10, 0,65,0));
        blackBox.getChildren().addAll(blackImages[0][3],blackImages[1][3],blackImages[2][3]);

        blackBox.setAlignment(Pos.TOP_CENTER);
        blackBox.setSpacing(25);
    }

    public static void DrawWhiteGobblets(){

        Label label = new Label("Player 2");
        label.setAlignment(Pos.TOP_CENTER);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");

        for(int i=0 ; i<3 ; i++){
            for (int j=0; j<4 ; j++){
                GobbletImage gobbletImage = new GobbletImage("W",j+1,true);
                whiteImages[i][j] = gobbletImage.getImgView();
            }
        }

        whiteBox.setBackground(Background.fill(Color.WHITE));
        whiteBox.setPrefWidth(200);
        whiteBox.setStyle("-fx-border-color: black; -fx-border-width: 1 0 0 1;");

        whiteBox.getChildren().add(label);
        whiteBox.setMargin(label, new Insets(10, 0,65,0));
        whiteBox.getChildren().addAll(whiteImages[0][3],whiteImages[1][3],whiteImages[2][3]);

        whiteBox.setAlignment(Pos.TOP_CENTER);
        whiteBox.setSpacing(25);
    }

    public static void replaceButton(VBox vbox, ImageView imageView, ImageView imageView1) {
        // Get the index of the oldButton in the VBox's children
        int index = vbox.getChildren().indexOf(imageView);

        // Replace the oldButton with the newButton at the same index
        if (index != -1) {
            vbox.getChildren().set(index, imageView1);
        }

    }
}
