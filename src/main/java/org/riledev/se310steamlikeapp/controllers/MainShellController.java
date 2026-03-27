package org.riledev.se310steamlikeapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import java.io.IOException;

/**
 * Glavni kontroler za navigacioni okvir aplikacije (Shell).
 * Upravlja navigacionom trakom i dinamickim ucitavanjem pogleda
 * u centralni deo BorderPane kontejnera.
 */
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

    /**
     * Inicijalizuje navigacione dogadjaje za svako dugme u meniju.
     */
    @FXML
    public void initialize() {
        storeButton.setOnAction(e -> switchView("views/store.fxml", storeButton));
        libraryButton.setOnAction(e -> switchView("views/library.fxml", libraryButton));
        communityButton.setOnAction(e -> switchView("views/community.fxml", communityButton));
        profileButton.setOnAction(e -> switchView("views/profile.fxml", profileButton));
    }

    /**
     * Ucitava zadati FXML pogled u centralni deo glavnog kontejnera
     * i azurira aktivno stanje navigacionog dugmeta.
     *
     * @param fxmlPath relativna putanja do FXML fajla
     * @param activeBtn dugme koje treba oznaciti kao aktivno
     */
    private void switchView(String fxmlPath, Button activeBtn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/" + fxmlPath));
            Node view = loader.load();

            mainContainer.setCenter(view);

            // Azuriranje vizuelnog stanja navigacione trake
            resetNavStyles();
            activeBtn.getStyleClass().add("active-nav");

        } catch (IOException e) {
            System.err.println("Notice: View " + fxmlPath + " not created yet.");
        }
    }

    /** Uklanja "active-nav" stilsku klasu sa svih navigacionih dugmadi. */
    private void resetNavStyles() {
        storeButton.getStyleClass().remove("active-nav");
        libraryButton.getStyleClass().remove("active-nav");
        communityButton.getStyleClass().remove("active-nav");
        profileButton.getStyleClass().remove("active-nav");
    }
}