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

/**
 * Kontroler za ekran korisnickog profila.
 * Omogucava pregled i uredjivanje profila ukljucujuci promenu
 * boje pozadine, profilne slike i prikazivanje vitrine igara.
 */
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

    /**
     * Inicijalizuje ekran profila.
     * Ucitava korisnicke podatke, profilnu sliku, sacuvanu boju
     * i vitrinu kupljenih igara.
     */
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) return;

        usernameLabel.setText(currentUser.getUsername());
        gamesOwnedLabel.setText("Games Owned: " + libraryRepository.getOwnedGamesCount(currentUser.getId()));

        // Ucitavanje sacuvane boje profila
        String savedColor = currentUser.getProfileColor();
        if (savedColor != null && !savedColor.isEmpty()) {
            profileHeaderBackground.setStyle("-fx-background-color: " + savedColor + ";");
            colorPicker.setValue(Color.web(savedColor));
        }

        // Ucitavanje profilne slike sa fallback mehanizmom
        try {
            String dbPath = currentUser.getProfilePicturePath();
            String resourcePath = "/org/riledev/se310steamlikeapp/" + dbPath;

            InputStream imageStream = getClass().getResourceAsStream(resourcePath);

            if (imageStream != null) {
                profileAvatar.setImage(new Image(imageStream));
            } else {
                // Fallback: ucitavanje iz fajl sistema ako resurs nije pronadjen
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

    /**
     * Azurira preview boje pozadine profila pri promeni u ColorPicker-u.
     */
    @FXML
    public void updatePreviewColor() {
        Color selectedColor = colorPicker.getValue();

        // Konverzija Color objekta u HEX string
        String hexColor = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255));

        profileHeaderBackground.setStyle("-fx-background-color: " + hexColor + ";");

        saveStatusLabel.setVisible(false);
    }

    /**
     * Cuva promenu boje profila u bazi podataka.
     *
     * @param event ActionEvent iz dugmeta za cuvanje
     */
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
            // Azuriranje boje u sesiji kako bi se zadrzala bez ponovne prijave
            currentUser.setProfileColor(hexColor);
            showStatusMessage("Profile updated successfully!", false);
        } else {
            showStatusMessage("Database error: Could not save profile.", true);
        }
    }

    /**
     * Obradjuje klik na avatar za upload nove profilne slike.
     * Otvara dijalog za izbor fajla sa filterom za slike.
     *
     * @param event MouseEvent od klika na avatar
     */
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

        if (fileSelected != null) {
            uploadProfilePicture(fileSelected);
        }
    }

    /**
     * Kopira izabranu sliku u direktorijum resursa i azurira putanju u bazi.
     *
     * @param sourceFile izabrani fajl sa diska
     */
    private void uploadProfilePicture(File sourceFile) {
        try {
            String targetDirPath = "src/main/resources/org/riledev/se310steamlikeapp/assets/profile/";
            File targetDir = new File(targetDirPath);

            // Kreiranje direktorijuma ako ne postoji
            if (!targetDir.exists())
                targetDir.mkdirs();

            // Imenovanje fajla sa ID-jem korisnika radi jedinstvenosti
            String fileName = currentUser.getId() + "_" + sourceFile.getName();
            File destFile = new File(targetDirPath, fileName);
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Cuvanje relativne putanje u bazu
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
        } catch (Exception e) {
            System.err.println("Error uploading profile picture.");
            e.printStackTrace();
            showStatusMessage("Error copying image file.", true);
        }
    }

    /**
     * Prikazuje vitrinu kupljenih igara na profilu korisnika.
     */
    private void displayGamesShowcase() {
        gamesShowcase.getChildren().clear();

        List<Game> ownedGames = libraryRepository.getOwnedGames(currentUser.getId());

        if (ownedGames.isEmpty()) {
            Label emptyLabel = new Label("Your library is empty. Visit the store!");
            emptyLabel.setStyle("-fx-text-fill: #8f98a0; -fx-font-size: 18px;");
            gamesShowcase.getChildren().add(emptyLabel);
            return;
        }

        // Male kartice bez Play dugmeta za vitrinu na profilu
        for (Game game : ownedGames) {
            VBox gameCard = GameCard.createGameCard(game, GameCard.GameCardSize.SMALL, false, true);
            gamesShowcase.getChildren().add(gameCard);
        }

    }

    /**
     * Prikazuje statusnu poruku na ekranu profila.
     *
     * @param message tekst poruke
     * @param isError true za crvenu boju (greska), false za zelenu (uspeh)
     */
    private void showStatusMessage(String message, boolean isError) {
        saveStatusLabel.setText(message);
        saveStatusLabel.setStyle(isError ? "-fx-text-fill: #ff5555; -fx-padding: 10 0 0 0;" : "-fx-text-fill: #5c7e10; -fx-padding: 10 0 0 0;");
        saveStatusLabel.setVisible(true);
    }
}