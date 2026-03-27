package org.riledev.se310steamlikeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.riledev.se310steamlikeapp.services.events.EventBus;
import org.riledev.se310steamlikeapp.services.notification.NotificationService;
import org.riledev.se310steamlikeapp.services.notification.SocialService;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;

import java.io.IOException;

/**
 * Glavna JavaFX Application klasa.
 * Inicijalizuje sistem (EventBus observer-e), ucitava pocetni ekran
 * za prijavu i upravlja zivotnim ciklusom aplikacije.
 */
public class SteamApplication extends Application {

    /**
     * Inicijalizacija sistema pre prikaza korisnickog interfejsa.
     * Registruje Observer servise na EventBus magistralu.
     */
    @Override
    public void init() throws Exception {
        super.init();

        // Registracija observer servisa za obavestenja i drustvene objave
        EventBus.getInstance().subscribe(new NotificationService());
        EventBus.getInstance().subscribe(new SocialService());

        System.out.println("System Initialized: EventBus observers registered.");
    }

    /**
     * Pokrece JavaFX aplikaciju i prikazuje ekran za prijavu.
     *
     * @param stage primarni Stage prozor aplikacije
     * @throws IOException ako FXML fajl za autentifikaciju ne moze biti ucitan
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Ponovna registracija za slucaj da init() nije pozvana
        EventBus.getInstance().subscribe(new NotificationService());
        EventBus.getInstance().subscribe(new SocialService());

        // Ucitavanje pocetnog ekrana za prijavu
        FXMLLoader fxmlLoader = new FXMLLoader(SteamApplication.class.getResource("views/auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Steam Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Zatvara konekciju ka bazi podataka pri zaustavljanju aplikacije.
     */
    @Override
    public void stop() throws Exception {
        DatabaseConnection.getInstance().closeConnection();
        super.stop();
    }

    /**
     * Glavni metod za pokretanje aplikacije.
     *
     * @param args argumenti komandne linije
     */
    public static void main(String[] args) {
        launch();
    }
}
