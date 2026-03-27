package org.riledev.se310steamlikeapp.models;

/**
 * Model klasa koja predstavlja korisnika sistema.
 * Sadrzi osnovne informacije o korisniku ukljucujuci korisnicko ime,
 * boju profila, putanju do profilne slike i status naloga.
 */
public class User {

    /** Jedinstveni identifikator korisnika u bazi podataka. */
    private int id;

    /** Korisnicko ime za prijavu i prikaz. */
    private String username;

    /** HEX boja pozadine korisnickog profila (npr. "#1b2838"). */
    private String profileColor;

    /** Relativna putanja do slike profila u resursima. */
    private String profilePicturePath;

    /** Status naloga korisnika (1 = aktivan). */
    private int statusId;

    /**
     * Podrazumevani konstruktor.
     * Kreira prazan User objekat koji se naknadno popunjava putem settera.
     */
    public User() {
    }

    /**
     * Vraca jedinstveni identifikator korisnika.
     *
     * @return ID korisnika
     */
    public int getId() {
        return id;
    }

    /**
     * Postavlja jedinstveni identifikator korisnika.
     *
     * @param id ID korisnika iz baze podataka
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Vraca korisnicko ime.
     *
     * @return korisnicko ime
     */
    public String getUsername() {
        return username;
    }

    /**
     * Postavlja korisnicko ime.
     *
     * @param username korisnicko ime za prikaz i autentifikaciju
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Vraca HEX boju profila korisnika.
     *
     * @return HEX string boje profila
     */
    public String getProfileColor() {
        return profileColor;
    }

    /**
     * Postavlja boju pozadine profila korisnika.
     *
     * @param profileColor HEX string boje (npr. "#1b2838")
     */
    public void setProfileColor(String profileColor) {
        this.profileColor = profileColor;
    }

    /**
     * Vraca relativnu putanju do profilne slike.
     *
     * @return putanja do slike profila
     */
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    /**
     * Postavlja putanju do profilne slike korisnika.
     *
     * @param profilePicturePath relativna putanja unutar resursa aplikacije
     */
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    /**
     * Vraca status ID korisnickog naloga.
     *
     * @return status ID (1 = aktivan)
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Postavlja status korisnickog naloga.
     *
     * @param statusId numericki status (1 = aktivan)
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
