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

public class SteamApplication extends Application {
    @Override
    public void init() throws Exception {
        super.init();
        EventBus.getInstance().subscribe(new NotificationService());
        EventBus.getInstance().subscribe(new SocialService());

        System.out.println("System Initialized: EventBus observers registered.");
    }

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getInstance().subscribe(new NotificationService());
        EventBus.getInstance().subscribe(new SocialService());
        FXMLLoader fxmlLoader = new FXMLLoader(SteamApplication.class.getResource("views/auth.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Steam Login");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception{
        DatabaseConnection.getInstance().closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
