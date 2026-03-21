package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class AuthController {

    @FXML
    private VBox loginForm;

    @FXML
    private VBox registerForm;

    @FXML
    public void showRegisterForm() {
        loginForm.setVisible(false);
        loginForm.setManaged(false);

        registerForm.setVisible(true);
        registerForm.setManaged(true);
    }

    @FXML
    public void showLoginForm() {
        registerForm.setVisible(false);
        registerForm.setManaged(false);

        loginForm.setVisible(true);
        loginForm.setManaged(true);
    }

    @FXML
    public void bypassLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/views/main-shell.fxml"));
            Scene mainScene = new Scene(loader.load(), 1080, 720);

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            stage.setScene(mainScene);
            stage.centerOnScreen();
            stage.setTitle("Steam");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load main-shell.fxml from AuthController.");
        }
    }
}