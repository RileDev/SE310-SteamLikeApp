package org.riledev.se310steamlikeapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.repositories.OrderRepository;
import org.riledev.se310steamlikeapp.services.events.EventBus;
import org.riledev.se310steamlikeapp.services.events.PurchaseEvent;
import org.riledev.se310steamlikeapp.services.payment.MasterCard;
import org.riledev.se310steamlikeapp.services.payment.Payment;
import org.riledev.se310steamlikeapp.services.payment.Paypal;
import org.riledev.se310steamlikeapp.services.payment.SteamWallet;
import org.riledev.se310steamlikeapp.services.session.SessionManager;

import java.io.IOException;

/**
 * Kontroler za ekran placanja i kupovine igara.
 * Koristi Strategy obrazac za izbor metode placanja i
 * EventBus za obavestavanje sistema o uspehu kupovine.
 */
public class PaymentController {
    @FXML
    private Label gameTitleLabel;
    @FXML
    private Label gamePriceLabel;
    @FXML
    private Label statusLabel;

    @FXML
    private RadioButton paypalRadio;
    @FXML
    private RadioButton mastercardRadio;
    @FXML
    private RadioButton steamWalletRadio;

    /** Igra koja se kupuje. */
    private Game gameToBuy;
    private final OrderRepository orderRepository = new OrderRepository();

    /**
     * Postavlja igru koja se kupuje i azurira UI sa njenim podacima.
     *
     * @param game igra za kupovinu
     */
    public void setGame(Game game) {
        this.gameToBuy = game;
        gameTitleLabel.setText(game.getTitle());
        gamePriceLabel.setText("€ " + game.getPrice());
    }

    /**
     * Potvrdjuje kupovinu igre.
     * Odredjuje strategiju placanja na osnovu selektovanog radio dugmeta,
     * obradjuje transakciju i emituje PurchaseEvent pri uspehu.
     *
     * @param eventAction ActionEvent iz dugmeta za potvrdu
     */
    @FXML
    public void confirmPurchase(ActionEvent eventAction) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null || gameToBuy == null)
            return;

        // Izbor konkretne strategije placanja (Strategy obrazac)
        Payment paymentStrategy;
        if (paypalRadio.isSelected()) {
            paymentStrategy = new Paypal();
        } else if (mastercardRadio.isSelected()) {
            paymentStrategy = new MasterCard();
        } else {
            paymentStrategy = new SteamWallet();
        }

        String paymentMethod = paymentStrategy.makePayment();

        // Obrada transakcije kroz OrderRepository
        boolean success = orderRepository.processPurchase(currentUser.getId(), gameToBuy, paymentMethod);

        if (success) {
            // Emitovanje dogadjaja kupovine za NotificationService i SocialService
            PurchaseEvent event = new PurchaseEvent(currentUser, gameToBuy);
            EventBus.getInstance().publish(event);
            System.out.println("Purchase successful! Redirecting to Store.");
            navigateTo("/org/riledev/se310steamlikeapp/views/store.fxml", eventAction);
        } else {
            statusLabel.setText("Payment failed. Please try again.");
            statusLabel.setVisible(true);
        }
    }

    /**
     * Otkazuje kupovinu i vraca korisnika u prodavnicu.
     *
     * @param event ActionEvent iz dugmeta za otkazivanje
     */
    @FXML
    public void cancelPurchase(ActionEvent event) {
        navigateTo("/org/riledev/se310steamlikeapp/views/store.fxml", event);
    }

    /**
     * Navigira na zadati FXML pogled unutar glavnog kontejnera.
     *
     * @param fxmlPath putanja do FXML fajla
     * @param event ActionEvent za pristup sceni
     */
    private void navigateTo(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();

            // Zamena centralnog dela BorderPane-a sa novim pogledom
            Node source = (Node) event.getSource();
            BorderPane mainContainer = (BorderPane) source.getScene().getRoot();
            mainContainer.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
