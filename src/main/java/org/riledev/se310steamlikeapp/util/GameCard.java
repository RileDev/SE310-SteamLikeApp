package org.riledev.se310steamlikeapp.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.riledev.se310steamlikeapp.models.Game;

import java.io.InputStream;

public class GameCard {

    private static final double WIDTH_S = 145.0;
    private static final double HEIGHT_S = 220.0;

    private static final double WIDTH_L = 190.0;
    private static final double HEIGHT_L = 280.0;

    private static final double PREF_WIDTH_S = 170.0;
    private static final double PREF_WIDTH_L = 220.0;

    public static enum GameCardSize{
        SMALL,
        LARGE
    }

    public static VBox createGameCard(Game game, GameCardSize size, boolean showButton) {
        VBox card = new VBox();

        double width = (size == GameCardSize.SMALL) ? WIDTH_S : WIDTH_L;
        double height = (size == GameCardSize.SMALL) ? HEIGHT_S : HEIGHT_L;
        double prefW = (size == GameCardSize.SMALL) ? PREF_WIDTH_S : PREF_WIDTH_L;

        card.setPrefWidth(prefW);
        card.setSpacing(12.0);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("game-card");

        try{
            String imagePath = "/org/riledev/se310steamlikeapp" + game.getCoverImagePath();
            InputStream imageStream = GameCard.class.getResourceAsStream(imagePath);

            if (imageStream != null) {
                ImageView coverImage = new ImageView(new Image(imageStream));
                coverImage.setFitWidth(width);
                coverImage.setFitHeight(height);

                Rectangle clip = new Rectangle(width, height);
                clip.setArcWidth(10);
                clip.setArcHeight(10);
                coverImage.setClip(clip);

                card.getChildren().add(coverImage);
            } else {
                Rectangle placeholder = new Rectangle(width, height);
                placeholder.setArcWidth(10);
                placeholder.setArcHeight(10);
                placeholder.setFill(javafx.scene.paint.Color.web("#3b5a75"));
                card.getChildren().add(placeholder);
            }
        }catch (Exception e) {
            System.err.println("Failed to load image for game: " + game.getTitle());
        }

        Label titleLabel = new Label(game.getTitle());
        titleLabel.getStyleClass().add("game-title");
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-text-alignment: center;");

        Label timeLabel = new Label("0 hrs on record");
        timeLabel.getStyleClass().add("game-time");

        card.getChildren().addAll(titleLabel, timeLabel);


        if(showButton){
            Button actionBtn = new Button("PLAY");
            actionBtn.getStyleClass().add("play-button");

            actionBtn.setOnAction(e -> System.out.println("Launching game: " + game.getTitle()));
            card.getChildren().add(actionBtn);
        }

        return card;
    }
}
