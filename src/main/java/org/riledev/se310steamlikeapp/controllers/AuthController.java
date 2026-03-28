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

/**
 * Kontroler za ekran prijave i registracije korisnika.
 * Upravlja prebacivanjem izmedju formi za prijavu i registraciju,
 * validacijom unosa i pokretanjem sesije kroz SessionManager.
 */
public class AuthController {

    // UI kontejneri za forme
    @FXML
    private VBox loginForm;
    @FXML
    private VBox registerForm;
    @FXML
    private Label authMessageLabel;

    // Polja za prijavu
    @FXML
    private TextField loginUsernameField;
    @FXML
    private PasswordField loginPasswordField;

    // Polja za registraciju
    @FXML
    private TextField regUsernameField;
    @FXML
    private PasswordField regPasswordField;
    @FXML
    private PasswordField regConfirmPasswordField;

    private final UserRepository userRepository = new UserRepository();

    /**
     * Obradjuje pokusaj prijave korisnika.
     * Validira unos, poziva repozitorijum i zapocinje sesiju.
     *
     * @param event ActionEvent iz dugmeta za prijavu
     */
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        // DEBUG ALAT: Prikaz sirovih vrednosti pre prosledjivanja repozitorijumu
        System.out.println("[DEBUG] AuthController.handleLogin -> Pokusaj prijave za username: '" + username + "'");

        // Provera da li su sva polja popunjena
        if (InputValidator.isAuthFieldEmpty(username, password, null)) {
            System.out.println("[DEBUG] AuthController.handleLogin -> Validacija pala: Polja su prazna.");
            showMessage("Please fill in all fields.", true);
            return;
        }

        User loggedInUser = userRepository.login(username, password);

        if (loggedInUser != null) {
            System.out.println("[DEBUG] AuthController.handleLogin -> Prijava uspesna, objekat User: " + loggedInUser.getId());
            // Pokretanje sesije i prelazak na glavni ekran
            SessionManager.getInstance().loginUser(loggedInUser);
            loadMainShell(event);
        } else {
            System.out.println("[DEBUG] AuthController.handleLogin -> Prijava pala: Pogresni podaci ili neaktivan nalog.");
            showMessage("Invalid username or password.", true);
        }
    }

    /**
     * Obradjuje pokusaj registracije novog korisnika.
     * Validira unos, registruje korisnika i vraca na formu za prijavu.
     */
    @FXML
    public void handleRegister() {
        String username = regUsernameField.getText();
        String password = regPasswordField.getText();
        String confirmPassword = regConfirmPasswordField.getText();

        System.out.println("[DEBUG] AuthController.handleRegister -> Pokrenut proces za korisnika: " + username);

        if (InputValidator.isAuthFieldEmpty(username, password, confirmPassword)) {
            System.out.println("[DEBUG] AuthController.handleRegister -> Prekinuto (Input validator okinuo grešku)");
            showMessage("Please fill in all fields.", true);
            return;
        }

        boolean success = userRepository.register(username, password, confirmPassword);

        if (success) {
            System.out.println("[DEBUG] AuthController.handleRegister -> Registracija vracena kao uspesna iz baze.");
            showMessage("Registration successful! Please sign in.", false);
            clearRegForm();
            showLoginForm();
        } else {
            showMessage("Registration failed. Username may exist or passwords do not match.", true);
        }
    }

    /** Brise sadrzaj polja za registraciju. */
    private void clearRegForm() {
        regUsernameField.clear();
        regPasswordField.clear();
        regConfirmPasswordField.clear();
    }

    /**
     * Prikazuje poruku na ekranu za autentifikaciju.
     *
     * @param message tekst poruke
     * @param isError true za crvenu boju (greska), false za zelenu (uspeh)
     */
    private void showMessage(String message, boolean isError) {
        authMessageLabel.setText(message);
        authMessageLabel.setStyle(isError ? "-fx-text-fill: #ff5555;" : "-fx-text-fill: #5c7e10;");
        authMessageLabel.setVisible(true);
        authMessageLabel.setManaged(true);
    }

    /** Prikazuje formu za registraciju i sakriva formu za prijavu. */
    @FXML
    public void showRegisterForm() {
        loginForm.setVisible(false);
        loginForm.setManaged(false);
        registerForm.setVisible(true);
        registerForm.setManaged(true);
        authMessageLabel.setVisible(false);
        authMessageLabel.setManaged(false);
    }

    /** Prikazuje formu za prijavu i sakriva formu za registraciju. */
    @FXML
    public void showLoginForm() {
        registerForm.setVisible(false);
        registerForm.setManaged(false);
        loginForm.setVisible(true);
        loginForm.setManaged(true);

        // Sakrivanje poruke o gresci ukoliko postoji
        if (authMessageLabel.getStyle().contains("#ff5555")) {
            authMessageLabel.setVisible(false);
            authMessageLabel.setManaged(false);
        }
    }

    /**
     * Ucitava glavni ekran aplikacije nakon uspesne prijave.
     *
     * @param event ActionEvent za dobijanje trenutnog prozora
     */
    private void loadMainShell(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/riledev/se310steamlikeapp/views/main-shell.fxml"));
            Scene mainScene = new Scene(loader.load(), 1080, 720);

            // Zamena scene na istom prozoru
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            stage.setScene(mainScene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}