package org.riledev.se310steamlikeapp;

import javafx.application.Application;

/**
 * Ulazna tacka aplikacije koja zaobilazi problem sa JavaFX modulima.
 * Delegira pokretanje na SteamApplication klasu putem Application.launch().
 */
public class Launcher {

    /**
     * Glavni metod koji pokrece JavaFX aplikaciju.
     *
     * @param args argumenti komandne linije
     */
    public static void main(String[] args) {
        Application.launch(SteamApplication.class, args);
    }
}
