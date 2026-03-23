package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javafx.scene.shape.Rectangle;
import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.repositories.LibraryRepository;
import org.riledev.se310steamlikeapp.services.session.SessionManager;

import java.io.IOException;

public class GameDetailsController {

    @FXML private Label gameTitleLabel;
    @FXML private Label gameDescLabel;
    @FXML private Label gamePriceLabel;
    @FXML private ImageView gameCoverImage;
    @FXML private Button purchaseButton;

    private Game currentGame;
    private final LibraryRepository libraryRepository = new LibraryRepository();

    public void setGame(Game game) {
        this.currentGame = game;

        gameTitleLabel.setText(game.getTitle());
        gameDescLabel.setText(game.getDescription());

        if (game.getPrice() == 0.0) {
            gamePriceLabel.setText("Free to Play");
            gamePriceLabel.getStyleClass().add("price-tag-free");
        } else {
            gamePriceLabel.setText("€ " + String.format("%.2f", game.getPrice()));
        }

        try {
            String imagePath = "/org/riledev/se310steamlikeapp" + game.getCoverImagePath();
            java.io.InputStream imageStream = getClass().getResourceAsStream(imagePath);

            if (imageStream != null) {
                gameCoverImage.setImage(new Image(imageStream));

                Rectangle clip = new Rectangle(300, 450);
                clip.setArcWidth(15);
                clip.setArcHeight(15);
                gameCoverImage.setClip(clip);
            } else {
                System.err.println("Notice: Could not find cover image for " + game.getTitle());
            }
        } catch (Exception e) {
            System.err.println("Error loading game cover image.");
        }

        checkOwnershipAndDisableButton();
    }

    private void checkOwnershipAndDisableButton() {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null || currentGame == null) return;

        boolean ownsGame = libraryRepository.doesUserOwnGame(currentUser.getId(), currentGame.getId());

        if (ownsGame) {
            purchaseButton.setText("ALREADY IN LIBRARY");
            purchaseButton.setDisable(true);
            purchaseButton.setStyle("-fx-background-color: #4c5a66; -fx-text-fill: #8f98a0;");
        }
    }

    @FXML
    public void handlePurchaseClick(ActionEvent event) {

    }

    @FXML
    public void goBackToStore(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/views/store.fxml"));
            Node storeView = loader.load();

            Node source = (Node) event.getSource();
            BorderPane mainContainer = (BorderPane) source.getScene().getRoot();
            mainContainer.setCenter(storeView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}