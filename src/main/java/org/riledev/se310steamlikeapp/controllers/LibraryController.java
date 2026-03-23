package org.riledev.se310steamlikeapp.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.repositories.LibraryRepository;
import org.riledev.se310steamlikeapp.services.session.SessionManager;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.riledev.se310steamlikeapp.util.GameCard;

import java.io.InputStream;
import java.util.List;

public class LibraryController {
    @FXML
    private FlowPane gamesGrid;

    private LibraryRepository libraryRepository = new LibraryRepository();

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if(currentUser != null)
            loadLibraryGames(currentUser.getId());
        else
            System.out.println("No active session found for Library.");
    }

    private void loadLibraryGames(int id) {
        gamesGrid.getChildren().clear();

        List<Game> ownedGames = libraryRepository.getOwnedGames(id);

        if(ownedGames.isEmpty()){
            Label emptyLabel = new Label("Your library is empty. Visit the store!");
            emptyLabel.setStyle("-fx-text-fill: #8f98a0; -fx-font-size: 18px;");
            gamesGrid.getChildren().add(emptyLabel);
            return;
        }

        for (Game game : ownedGames){
            VBox gameCard = GameCard.createGameCard(game, GameCard.GameCardSize.LARGE, true, true);
            gamesGrid.getChildren().add(gameCard);
        }
    }


}
