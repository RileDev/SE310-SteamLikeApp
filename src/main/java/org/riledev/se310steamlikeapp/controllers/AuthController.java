package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.repositories.UserRepository;
import org.riledev.se310steamlikeapp.services.session.SessionManager;
import org.riledev.se310steamlikeapp.util.InputValidator;

import java.io.IOException;

public class AuthController {

    // UI Containers
    @FXML private VBox loginForm;
    @FXML private VBox registerForm;
    @FXML private Label authMessageLabel;

    // Login Fields
    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;

    // Register Fields
    @FXML private TextField regUsernameField;
    @FXML private PasswordField regPasswordField;
    @FXML private PasswordField regConfirmPasswordField;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (InputValidator.isAuthFieldEmpty(username, password, null)) {
            showMessage("Please fill in all fields.", true);
            return;
        }

        User loggedInUser = userRepository.login(username, password);

        if (loggedInUser != null) {
            SessionManager.getInstance().loginUser(loggedInUser);

            loadMainShell(event);
        } else {
            showMessage("Invalid username or password.", true);
        }
    }

    @FXML
    public void handleRegister() {
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String confirmPassword = regConfirmPasswordField.getText();

        if (InputValidator.isAuthFieldEmpty(username, password, confirmPassword)) {
            showMessage("Please fill in all fields.", true);
            return;
        }

        boolean success = userRepository.register(username, password, confirmPassword);

        if (success) {
            showMessage("Registration successful! Please sign in.", false);

            clearRegForm();
            showLoginForm();
        } else {
            showMessage("Registration failed. Username may exist or passwords do not match.", true);
        }
    }

    private void clearRegForm() {
        regUsernameField.clear();
        regPasswordField.clear();
        regConfirmPasswordField.clear();
    }

    private void showMessage(String message, boolean isError) {
        authMessageLabel.setText(message);
        authMessageLabel.setStyle(isError ? "-fx-text-fill: #ff5555;" : "-fx-text-fill: #5c7e10;");
        authMessageLabel.setVisible(true);
        authMessageLabel.setManaged(true);
    }

    @FXML
    public void showRegisterForm() {
        loginForm.setVisible(false);
        loginForm.setManaged(false);
        registerForm.setVisible(true);
        registerForm.setManaged(true);
        authMessageLabel.setVisible(false);
        authMessageLabel.setManaged(false);
    }

    @FXML
    public void showLoginForm() {
        registerForm.setVisible(false);
        registerForm.setManaged(false);
        loginForm.setVisible(true);
        loginForm.setManaged(true);
        if (authMessageLabel.getStyle().contains("#ff5555")) {
            authMessageLabel.setVisible(false);
            authMessageLabel.setManaged(false);
        }
    }

    private void loadMainShell(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/riledev/se310steamlikeapp/views/main-shell.fxml"));
            Scene mainScene = new Scene(loader.load(), 1080, 720);

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            stage.setScene(mainScene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}