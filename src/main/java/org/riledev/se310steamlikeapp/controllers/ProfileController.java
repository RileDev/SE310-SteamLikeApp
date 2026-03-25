package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.repositories.LibraryRepository;
import org.riledev.se310steamlikeapp.repositories.UserRepository;
import org.riledev.se310steamlikeapp.services.session.SessionManager;
import org.riledev.se310steamlikeapp.util.GameCard;

public class ProfileController {

    @FXML
    private HBox profileHeaderBackground;

    @FXML
    private FlowPane gamesShowcase;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Label saveStatusLabel;

    @FXML private Label usernameLabel;
    @FXML private ImageView profileAvatar;
    @FXML private Label gamesOwnedLabel;
    @FXML private Label postsLabel;

    private User currentUser;
    private final UserRepository userRepository = new UserRepository();
    private final LibraryRepository libraryRepository = new LibraryRepository();

    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();
        if(currentUser == null) return;

        usernameLabel.setText(currentUser.getUsername());
        gamesOwnedLabel.setText("Games Owned: " + libraryRepository.getOwnedGamesCount(currentUser.getId()));

        String savedColor = currentUser.getProfileColor();
        if (savedColor != null && !savedColor.isEmpty()) {
            profileHeaderBackground.setStyle("-fx-background-color: " + savedColor + ";");
            colorPicker.setValue(Color.web(savedColor));
        }

        try {
            String dbPath = currentUser.getProfilePicturePath();
            String resourcePath = "/org/riledev/se310steamlikeapp/" + dbPath;

            InputStream imageStream = getClass().getResourceAsStream(resourcePath);

            if(imageStream != null){
                profileAvatar.setImage(new Image(imageStream));
            }else{
                File srcFile = new File("src/main/resources/org/riledev/se310steamlikeapp/" + dbPath);
                if (srcFile.exists()) {
                    profileAvatar.setImage(new Image(srcFile.toURI().toString()));
                } else {
                    System.err.println("Notice: Custom avatar not found in target or src. Using fallback.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error while attempting to load avatar image.");
            e.printStackTrace();
        }

        displayGamesShowcase();
    }

    @FXML
    public void updatePreviewColor() {
        Color selectedColor = colorPicker.getValue();

        String hexColor = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255));

        profileHeaderBackground.setStyle("-fx-background-color: " + hexColor + ";");

        saveStatusLabel.setVisible(false);
    }

    @FXML
    public void saveProfileChanges(ActionEvent event) {
        if (currentUser == null) {
            showStatusMessage("Error: No active session.", true);
            return;
        }

        Color selectedColor = colorPicker.getValue();
        String hexColor = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255));

        boolean success = userRepository.updateProfileColor(currentUser.getId(), hexColor);

        if (success) {
            currentUser.setProfileColor(hexColor);
            showStatusMessage("Profile updated successfully!", false);
        } else {
            showStatusMessage("Database error: Could not save profile.", true);
        }
    }

    @FXML
    public void handleAvatarClick(MouseEvent event) {
        if (currentUser == null) return;

        FileChooser fc = new FileChooser();
        fc.setTitle("Select Profile Picture");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File fileSelected = fc.showOpenDialog(stage);

        if(fileSelected != null){
            uploadProfilePicture(fileSelected);
        }
    }

    private void uploadProfilePicture(File sourceFile) {
        try{
            String targetDirPath = "src/main/resources/org/riledev/se310steamlikeapp/assets/profile/";
            File targetDir = new File(targetDirPath);

            if(!targetDir.exists())
                targetDir.mkdirs();

            String fileName = currentUser.getId() + "_" + sourceFile.getName();
            File destFile = new File(targetDirPath, fileName);
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            String dbPath = "assets/profile/" + fileName;
            boolean success = userRepository.updateProfilePicturePath(currentUser.getId(), dbPath);

            if (success) {
                currentUser.setProfilePicturePath(dbPath);

                Image newAvatar = new Image(destFile.toURI().toString());
                profileAvatar.setImage(newAvatar);
                showStatusMessage("Profile picture updated!", false);
            } else {
                showStatusMessage("Failed to save picture to database.", true);
            }
        }catch (Exception e) {
            System.err.println("Error uploading profile picture.");
            e.printStackTrace();
            showStatusMessage("Error copying image file.", true);
        }
    }

    private void displayGamesShowcase(){
        gamesShowcase.getChildren().clear();

        List<Game> ownedGames = libraryRepository.getOwnedGames(currentUser.getId());

        if(ownedGames.isEmpty()){
            Label emptyLabel = new Label("Your library is empty. Visit the store!");
            emptyLabel.setStyle("-fx-text-fill: #8f98a0; -fx-font-size: 18px;");
            gamesShowcase.getChildren().add(emptyLabel);
            return;
        }

        for (Game game : ownedGames){
            VBox gameCard = GameCard.createGameCard(game, GameCard.GameCardSize.SMALL, false, true);
            gamesShowcase.getChildren().add(gameCard);
        }

    }

    private void showStatusMessage(String message, boolean isError) {
        saveStatusLabel.setText(message);
        saveStatusLabel.setStyle(isError ? "-fx-text-fill: #ff5555; -fx-padding: 10 0 0 0;" : "-fx-text-fill: #5c7e10; -fx-padding: 10 0 0 0;");
        saveStatusLabel.setVisible(true);
    }
}