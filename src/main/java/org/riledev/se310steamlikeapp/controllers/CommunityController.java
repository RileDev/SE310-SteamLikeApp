package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class CommunityController {

    @FXML
    private TextArea postTextArea;

    @FXML
    public void handlePostUpload(ActionEvent event) {
        String content = postTextArea.getText();

        if (content != null && !content.trim().isEmpty()) {
            // TODO: Later, call CommunityRepository.createPost(userId, content) here
            System.out.println("Mock Uploading Post: " + content);

            // Clear the text area after "posting"
            postTextArea.clear();
        }
    }
}