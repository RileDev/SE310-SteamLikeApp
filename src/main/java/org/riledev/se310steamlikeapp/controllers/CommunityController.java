package org.riledev.se310steamlikeapp.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.riledev.se310steamlikeapp.models.CommunityPost;
import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.repositories.CommunityRepository;
import org.riledev.se310steamlikeapp.services.session.SessionManager;

import java.util.List;

/**
 * Kontroler za ekran zajednice (Community Feed).
 * Omogucava pregled svih objava i kreiranje novih objava.
 * Svaka objava se prikazuje kao kartica sa avatarom autora,
 * korisnickim imenom, datumom i sadrzajem.
 */
public class CommunityController {

    @FXML
    private TextArea postTextArea;
    @FXML
    private VBox postsFeedContainer;
    @FXML
    private ImageView currentUserAvatar;
    @FXML
    private Label postErrorLabel;

    private final CommunityRepository communityRepository = new CommunityRepository();
    private User currentUser;

    /**
     * Inicijalizuje ekran zajednice.
     * Ucitava avatar trenutnog korisnika i osvezava feed objava.
     */
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            Image avatar = loadImageSafely(currentUser.getProfilePicturePath());
            if (avatar != null)
                currentUserAvatar.setImage(avatar);
        }

        refreshFeed();
    }

    /**
     * Obradjuje objavljivanje nove objave.
     * Validira sadrzaj, poziva repozitorijum i osvezava feed.
     */
    @FXML
    public void handlePostUpload() {
        if (currentUser == null)
            return;

        String content = postTextArea.getText();

        // Validacija sadrzaja objave
        if (content == null || content.trim().isEmpty()) {
            postErrorLabel.setText("Post cannot be empty.");
            postErrorLabel.setVisible(true);
            return;
        }

        postErrorLabel.setVisible(false);

        boolean success = communityRepository.createPost(currentUser.getId(), content.trim());

        if (success) {
            postTextArea.clear();
            refreshFeed();
        } else {
            postErrorLabel.setText("Failed to publish post.");
            postErrorLabel.setVisible(true);
        }
    }

    /**
     * Osvezava feed objava ucitavanjem svih objava iz baze.
     */
    private void refreshFeed() {
        postsFeedContainer.getChildren().clear();

        List<CommunityPost> allPosts = communityRepository.getAllPosts();

        if (allPosts.isEmpty()) {
            Label emptyLabel = new Label("It's quiet here. Be the first to post something!");
            emptyLabel.setStyle("-fx-text-fill: #8f98a0; -fx-font-size: 16px;");
            postsFeedContainer.getChildren().add(emptyLabel);
            return;
        }

        // Kreiranje vizuelne kartice za svaku objavu
        for (CommunityPost post : allPosts) {
            HBox postUI = createPostCard(post);
            postsFeedContainer.getChildren().add(postUI);
        }
    }

    /**
     * Kreira vizuelnu karticu za pojedinacnu objavu u feed-u.
     *
     * @param post objava za koju se kreira kartica
     * @return HBox element koji predstavlja karticu objave
     */
    private HBox createPostCard(CommunityPost post) {
        HBox container = new HBox();
        container.getStyleClass().add("post-container");

        // Avatar autora objave
        VBox avatarFrame = new VBox();
        avatarFrame.getStyleClass().add("post-avatar-frame");
        avatarFrame.setMaxHeight(60.0);
        avatarFrame.setMaxWidth(60.0);

        ImageView avatarImage = new ImageView();
        avatarImage.setFitHeight(60.0);
        avatarImage.setFitWidth(60.0);
        Image img = loadImageSafely(post.getAuthorProfilePicture());
        if (img != null)
            avatarImage.setImage(img);

        avatarFrame.getChildren().add(avatarImage);

        // Sadrzaj objave sa zaglavljem
        VBox contentBox = new VBox();
        contentBox.setSpacing(8.0);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        // Zaglavlje: korisnicko ime i datum
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.BOTTOM_LEFT);
        headerBox.setSpacing(10.0);

        Label usernameLabel = new Label(post.getAuthorUsername());
        usernameLabel.getStyleClass().add("post-username");

        Label dateLabel = new Label("Posted on " + post.getPostedDate());
        dateLabel.getStyleClass().add("post-date");

        headerBox.getChildren().addAll(usernameLabel, dateLabel);

        // Tekst objave
        Label contentLabel = new Label(post.getContent());
        contentLabel.getStyleClass().add("post-content");
        contentLabel.setWrapText(true);

        contentBox.getChildren().addAll(headerBox, contentLabel);

        container.getChildren().addAll(avatarFrame, contentBox);

        return container;
    }

    /**
     * Bezbedno ucitava sliku iz resursa ili fajl sistema.
     * Pokusava najpre iz classpath resursa, zatim iz src direktorijuma.
     *
     * @param dbPath relativna putanja do slike iz baze podataka
     * @return Image objekat ili null ako slika nije pronadjena
     */
    private Image loadImageSafely(String dbPath) {
        if (dbPath == null)
            return null;
        try {
            // Pokusaj ucitavanja iz classpath resursa
            String resourcePath = "/org/riledev/se310steamlikeapp/" + dbPath;
            java.io.InputStream stream = getClass().getResourceAsStream(resourcePath);
            if (stream != null)
                return new Image(stream);

            // Fallback: ucitavanje direktno iz fajl sistema
            java.io.File srcFile = new java.io.File("src/main/resources/org/riledev/se310steamlikeapp/" + dbPath);
            if (srcFile.exists())
                return new Image(srcFile.toURI().toString());

        } catch (Exception e) {
            System.err.println("Failed to load avatar image for post.");
        }
        return null;
    }
}