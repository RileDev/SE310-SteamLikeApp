package org.riledev.se310steamlikeapp.services.notification;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.riledev.se310steamlikeapp.services.events.Event;
import org.riledev.se310steamlikeapp.services.events.Observer;
import org.riledev.se310steamlikeapp.services.events.PurchaseEvent;

public class NotificationService implements Observer {
    @Override
    public void update(Event event) {
        if(event instanceof PurchaseEvent){
            PurchaseEvent purchase = (PurchaseEvent) event;
            String message = "Success! " + purchase.getGame().getTitle() + " is now in your library.";

            Platform.runLater(() -> showToast(message));
        }
    }

    private void showToast(String message) {
        Stage toastStage = new Stage();
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(message);
        label.setStyle("-fx-background-color: #171a21; -fx-text-fill: #66c0f4; -fx-padding: 20px; -fx-font-size: 16px; -fx-border-color: #66c0f4; -fx-border-width: 2px;");

        StackPane root = new StackPane(label);
        Scene scene = new Scene(root);
        scene.setFill(null);
        toastStage.setScene(scene);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        toastStage.setX(screenBounds.getMaxX() - 350);
        toastStage.setY(screenBounds.getMaxY() - 100);

        toastStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> toastStage.close());
        delay.play();
    }
}
