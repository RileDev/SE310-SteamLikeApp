package org.riledev.se310steamlikeapp.services.events;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.models.User;

/**
 * Dogadjaj koji se emituje nakon uspesne kupovine igre.
 * Sadrzi informacije o korisniku koji je izvrsio kupovinu
 * i igri koja je kupljena. Koristi se za obavestavanje
 * servisa poput {@link org.riledev.se310steamlikeapp.services.notification.NotificationService}
 * i {@link org.riledev.se310steamlikeapp.services.notification.SocialService}.
 *
 * @see Event
 * @see EventBus
 */
public class PurchaseEvent implements Event {

    /** Korisnik koji je izvrsio kupovinu. */
    private final User user;

    /** Igra koja je kupljena. */
    private final Game game;

    /**
     * Kreira novi dogadjaj kupovine.
     *
     * @param user korisnik koji je kupio igru
     * @param game igra koja je kupljena
     */
    public PurchaseEvent(User user, Game game) {
        this.user = user;
        this.game = game;
    }

    /**
     * Vraca korisnika koji je izvrsio kupovinu.
     *
     * @return korisnik kupac
     */
    public User getUser() {
        return user;
    }

    /**
     * Vraca igru koja je kupljena.
     *
     * @return kupljena igra
     */
    public Game getGame() {
        return game;
    }
}
