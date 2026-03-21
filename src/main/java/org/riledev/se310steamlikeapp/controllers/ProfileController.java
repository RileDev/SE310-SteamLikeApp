package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ProfileController {

    @FXML
    private HBox profileHeaderBackground;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Label saveStatusLabel;

    @FXML
    public void initialize() {
        updatePreviewColor();
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
        Color selectedColor = colorPicker.getValue();
        String hexColor = String.format("#%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255));

        // TODO: Later, this is where you will call UserRepository.updateProfileColor(userId, hexColor);
        System.out.println("Mock Save: Saving color " + hexColor + " to database.");

        saveStatusLabel.setText("Profile updated successfully!");
        saveStatusLabel.setVisible(true);
    }
}