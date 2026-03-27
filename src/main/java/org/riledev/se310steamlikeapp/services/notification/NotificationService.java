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

/**
 * Servis za prikaz pop-up obavestenja korisniku.
 * Implementira Observer interfejs i reaguje na PurchaseEvent
 * prikazivanjem toast poruke u donjem desnom uglu ekrana.
 *
 * @see Observer
 * @see PurchaseEvent
 */
public class NotificationService implements Observer {

    /**
     * Obradjuje primljeni dogadjaj.
     * Ako je u pitanju PurchaseEvent, prikazuje toast obavestenje.
     *
     * @param event primljeni dogadjaj iz EventBus-a
     */
    @Override
    public void update(Event event) {
        if (event instanceof PurchaseEvent) {
            PurchaseEvent purchase = (PurchaseEvent) event;
            String message = "Success! " + purchase.getGame().getTitle() + " is now in your library.";

            // Prebacivanje na JavaFX nit jer se UI elementi mogu menjati samo odatle
            Platform.runLater(() -> showToast(message));
        }
    }

    /**
     * Prikazuje privremeni toast prozor sa porukom.
     * Prozor se automatski zatvara nakon 4 sekunde.
     *
     * @param message tekst poruke za prikaz
     */
    private void showToast(String message) {
        Stage toastStage = new Stage();
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(message);
        label.setStyle("-fx-background-color: #171a21; -fx-text-fill: #66c0f4; -fx-padding: 20px; -fx-font-size: 16px; -fx-border-color: #66c0f4; -fx-border-width: 2px;");

        StackPane root = new StackPane(label);
        Scene scene = new Scene(root);
        scene.setFill(null);
        toastStage.setScene(scene);

        // Pozicioniranje u donji desni ugao ekrana
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        toastStage.setX(screenBounds.getMaxX() - 350);
        toastStage.setY(screenBounds.getMaxY() - 100);

        toastStage.show();

        // Automatsko zatvaranje nakon 4 sekunde
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> toastStage.close());
        delay.play();
    }
}
