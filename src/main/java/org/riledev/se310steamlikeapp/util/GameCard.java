package org.riledev.se310steamlikeapp.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.services.launch.Launcher;
import org.riledev.se310steamlikeapp.services.launch.LauncherFactory;

import java.io.InputStream;

/**
 * Pomocna klasa za kreiranje vizuelnih kartica igara u korisnickom interfejsu.
 * Generise VBox elemente sa naslovnom slikom, nazivom igre i opcionalnim
 * dugmetom za pokretanje. Koristi se u prodavnici, biblioteci i profilu.
 */
public class GameCard {

    // Dimenzije za malu karticu (prodavnica, profil)
    private static final double WIDTH_S = 145.0;
    private static final double HEIGHT_S = 220.0;

    // Dimenzije za veliku karticu (biblioteka)
    private static final double WIDTH_L = 190.0;
    private static final double HEIGHT_L = 280.0;

    // Preferovane sirine za oba formata
    private static final double PREF_WIDTH_S = 170.0;
    private static final double PREF_WIDTH_L = 220.0;

    /**
     * Enumeracija za velicinu kartice igre.
     */
    public static enum GameCardSize {
        SMALL,
        LARGE
    }

    /**
     * Kreira vizuelnu karticu za zadatu igru.
     *
     * @param game igra za koju se kreira kartica
     * @param size velicina kartice (SMALL ili LARGE)
     * @param showPlayButton da li se prikazuje dugme za pokretanje igre
     * @param displayPlaytime da li se prikazuje vreme igranja
     * @return VBox element koji predstavlja karticu igre
     */
    public static VBox createGameCard(Game game, GameCardSize size, boolean showPlayButton, boolean displayPlaytime) {
        VBox card = new VBox();

        // Odredjivanje dimenzija na osnovu velicine kartice
        double width = (size == GameCardSize.SMALL) ? WIDTH_S : WIDTH_L;
        double height = (size == GameCardSize.SMALL) ? HEIGHT_S : HEIGHT_L;
        double prefW = (size == GameCardSize.SMALL) ? PREF_WIDTH_S : PREF_WIDTH_L;

        card.setPrefWidth(prefW);
        card.setSpacing(12.0);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("game-card");

        // Ucitavanje naslovne slike igre iz resursa
        try {
            String imagePath = "/org/riledev/se310steamlikeapp" + game.getCoverImagePath();
            InputStream imageStream = GameCard.class.getResourceAsStream(imagePath);

            if (imageStream != null) {
                ImageView coverImage = new ImageView(new Image(imageStream));
                coverImage.setFitWidth(width);
                coverImage.setFitHeight(height);

                // Zaobljeni uglovi slike
                Rectangle clip = new Rectangle(width, height);
                clip.setArcWidth(10);
                clip.setArcHeight(10);
                coverImage.setClip(clip);

                card.getChildren().add(coverImage);
            } else {
                // Placeholder pravougaonik ako slika nije pronadjena
                Rectangle placeholder = new Rectangle(width, height);
                placeholder.setArcWidth(10);
                placeholder.setArcHeight(10);
                placeholder.setFill(javafx.scene.paint.Color.web("#3b5a75"));
                card.getChildren().add(placeholder);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image for game: " + game.getTitle());
        }

        // Naziv igre ispod slike
        Label titleLabel = new Label(game.getTitle());
        titleLabel.getStyleClass().add("game-title");
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-text-alignment: center;");

        card.getChildren().add(titleLabel);

        // Opcioni prikaz vremena igranja
        if (displayPlaytime) {
            Label timeLabel = new Label("0 hrs on record");
            timeLabel.getStyleClass().add("game-time");
            card.getChildren().add(timeLabel);
        }

        // Opciono dugme za pokretanje igre putem LauncherFactory (Factory obrazac)
        if (showPlayButton) {
            Button actionBtn = new Button("PLAY");
            actionBtn.getStyleClass().add("play-button");

            actionBtn.setOnAction(e -> {
                Launcher launcher = LauncherFactory.getLauncher();

                if (launcher != null) {
                    launcher.launch(game);
                } else {
                    System.out.println("Could not launch game: Unsupported OS.");
                }
            });
            card.getChildren().add(actionBtn);
        }

        return card;
    }
}
