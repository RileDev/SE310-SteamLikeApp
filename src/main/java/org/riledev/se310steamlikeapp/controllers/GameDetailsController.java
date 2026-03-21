package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class GameDetailsController {

    @FXML
    public void goBackToStore(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/views/store.fxml"));
            Node storeView = loader.load();

            Node clickedNode = (Node) event.getSource();
            BorderPane mainContainer = (BorderPane) clickedNode.getScene().getRoot();

            mainContainer.setCenter(storeView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}