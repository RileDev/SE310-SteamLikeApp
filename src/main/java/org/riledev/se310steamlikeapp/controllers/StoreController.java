package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.repositories.GameRepository;
import org.riledev.se310steamlikeapp.util.GameCard;

import java.io.IOException;
import java.util.List;

public class StoreController {
    @FXML private FlowPane gamesGrid;
    @FXML private TextField searchField;
    @FXML private Label sectionTitle;

    private final GameRepository gameRepository = new GameRepository();

    @FXML
    public void initialize() {
        loadGames(gameRepository.getAllGames());
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            loadGames(gameRepository.getAllGames());
            sectionTitle.setText("Newly Added to Store");
        } else {
            loadGames(gameRepository.searchGames(keyword.trim()));
            sectionTitle.setText("Search results for: " + keyword.trim());
        }
    }

    private void loadGames(List<Game> games) {
        gamesGrid.getChildren().clear();

        if (games.isEmpty()) {
            Label noGames = new Label("No games found.");
            noGames.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            gamesGrid.getChildren().add(noGames);
            return;
        }

        for (Game game : games) {
            VBox gameCard = GameCard.createGameCard(game, GameCard.GameCardSize.SMALL, false, false);
            gameCard.setOnMouseClicked(event -> openGameDetails(event, game));
            gamesGrid.getChildren().add(gameCard);
        }
    }


    private void openGameDetails(MouseEvent event, Game game) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/views/game-details.fxml"));
            Node gameDetailsView = loader.load();

            GameDetailsController controller = loader.getController();
            controller.setGame(game);

            Node source = (Node) event.getSource();
            BorderPane mainContainer = (BorderPane) source.getScene().getRoot();
            mainContainer.setCenter(gameDetailsView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}