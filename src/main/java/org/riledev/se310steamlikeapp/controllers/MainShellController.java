package org.riledev.se310steamlikeapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import java.io.IOException;

public class MainShellController {

    @FXML
    private BorderPane mainContainer;

    @FXML
    private Button storeButton;
    @FXML
    private Button libraryButton;
    @FXML
    private Button communityButton;
    @FXML
    private Button profileButton;

    @FXML
    public void initialize() {
        storeButton.setOnAction(e -> switchView("views/store.fxml", storeButton));
        libraryButton.setOnAction(e -> switchView("views/library.fxml", libraryButton));
        communityButton.setOnAction(e -> switchView("views/community.fxml", communityButton));
        profileButton.setOnAction(e -> switchView("views/profile.fxml", profileButton));
    }

    private void switchView(String fxmlPath, Button activeBtn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/" + fxmlPath));
            Node view = loader.load();

            mainContainer.setCenter(view);

            resetNavStyles();
            activeBtn.getStyleClass().add("active-nav");

        } catch (IOException e) {
            System.err.println("Notice: View " + fxmlPath + " not created yet.");
        }
    }

    private void resetNavStyles() {
        storeButton.getStyleClass().remove("active-nav");
        libraryButton.getStyleClass().remove("active-nav");
        communityButton.getStyleClass().remove("active-nav");
        profileButton.getStyleClass().remove("active-nav");
    }
}