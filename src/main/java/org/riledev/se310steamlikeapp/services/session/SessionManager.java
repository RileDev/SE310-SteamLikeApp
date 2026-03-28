package org.riledev.se310steamlikeapp.services.session;

import org.riledev.se310steamlikeapp.models.User;

/**
 * Menadzer korisnicke sesije (Singleton obrazac).
 * Odrzava globalno stanje trenutno ulogovanog korisnika tokom
 * zivotnog veka aplikacije. Obezbeduje centralizovan pristup
 * podacima o aktivnom korisniku iz bilo kog dela sistema.
 */
public class SessionManager {

    /** Jedina instanca SessionManager klase. */
    private static SessionManager instance;

    /** Trenutno ulogovani korisnik (null ako niko nije prijavljen). */
    private User currentUser;

    /**
     * Privatni konstruktor - sprecava direktno instanciranje.
     * Pristup se vrsi iskljucivo putem {@link #getInstance()}.
     */
    private SessionManager() {
    }

    /**
     * Vraca jedinu instancu SessionManager klase.
     * Thread-safe implementacija putem synchronized kljucne reci.
     *
     * @return singleton instanca SessionManager-a
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }

        return instance;
    }

    /**
     * Prijavljuje korisnika na sistem i zapocinje novu sesiju.
     *
     * @param user korisnik koji se prijavljuje
     */
    public void loginUser(User user) {
        this.currentUser = user;
        if (user != null) {
            System.out.println("Session started for user: " + user.getUsername());
        }
    }

    /**
     * Odjavljuje trenutno ulogovanog korisnika i zavrsava sesiju.
     */
    public void logoutUser() {
        System.out.println("Session ended for user: " + (currentUser != null ? currentUser.getUsername() : "Unknown"));
        this.currentUser = null;
    }

    /**
     * Vraca trenutno ulogovanog korisnika.
     *
     * @return trenutni korisnik ili null ako niko nije prijavljen
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Proverava da li je neki korisnik trenutno prijavljen.
     *
     * @return true ako je korisnik prijavljen, false u suprotnom
     */
    public boolean isUserLoggedIn() {
        return this.currentUser != null;
    }

}
