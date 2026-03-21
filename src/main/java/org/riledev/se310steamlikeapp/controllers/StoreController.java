package org.riledev.se310steamlikeapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class StoreController {

    @FXML
    public void openGameDetails(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/views/game-details.fxml"));
            Node gameDetailsView = loader.load();

            Node clickedNode = (Node) event.getSource();
            BorderPane mainContainer = (BorderPane) clickedNode.getScene().getRoot();

            mainContainer.setCenter(gameDetailsView);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Could not load game-details.fxml");
        }
    }
}