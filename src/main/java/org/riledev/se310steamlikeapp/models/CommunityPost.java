package org.riledev.se310steamlikeapp.models;

/**
 * Model klasa koja predstavlja objavu korisnika u zajednici (Community).
 * Sadrzi informacije o sadrzaju objave, autoru i datumu objavljivanja.
 * Polja authorUsername i authorProfilePicture se popunjavaju putem JOIN upita.
 */
public class CommunityPost {

    /** Jedinstveni identifikator objave u bazi podataka. */
    private int id;

    /** ID korisnika koji je kreirao objavu (spoljni kljuc ka tabeli User). */
    private int userId;

    /** Tekstualni sadrzaj objave. */
    private String content;

    /** Datum i vreme objavljivanja (generise baza podataka). */
    private String postedDate;

    /** Korisnicko ime autora objave (popunjava se iz JOIN upita). */
    private String authorUsername;

    /** Putanja do profilne slike autora (popunjava se iz JOIN upita). */
    private String authorProfilePicture;

    /**
     * Podrazumevani konstruktor.
     * Kreira prazan CommunityPost objekat koji se popunjava putem settera.
     */
    public CommunityPost() {
    }

    /**
     * Vraca jedinstveni identifikator objave.
     *
     * @return ID objave
     */
    public int getId() {
        return id;
    }

    /**
     * Postavlja jedinstveni identifikator objave.
     *
     * @param id ID objave iz baze podataka
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Vraca ID korisnika koji je kreirao objavu.
     *
     * @return ID autora objave
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Postavlja ID korisnika koji je kreirao objavu.
     *
     * @param userId ID autora (spoljni kljuc)
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Vraca tekstualni sadrzaj objave.
     *
     * @return sadrzaj objave
     */
    public String getContent() {
        return content;
    }

    /**
     * Postavlja tekstualni sadrzaj objave.
     *
     * @param content tekst objave
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Vraca datum i vreme objavljivanja.
     *
     * @return datum objavljivanja kao string
     */
    public String getPostedDate() {
        return postedDate;
    }

    /**
     * Postavlja datum i vreme objavljivanja.
     *
     * @param postedDate datum u formatu iz baze podataka
     */
    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    /**
     * Vraca korisnicko ime autora objave.
     *
     * @return korisnicko ime autora
     */
    public String getAuthorUsername() {
        return authorUsername;
    }

    /**
     * Postavlja korisnicko ime autora objave.
     *
     * @param authorUsername korisnicko ime iz JOIN upita
     */
    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    /**
     * Vraca putanju do profilne slike autora.
     *
     * @return putanja do profilne slike
     */
    public String getAuthorProfilePicture() {
        return authorProfilePicture;
    }

    /**
     * Postavlja putanju do profilne slike autora objave.
     *
     * @param authorProfilePicture relativna putanja do slike
     */
    public void setAuthorProfilePicture(String authorProfilePicture) {
        this.authorProfilePicture = authorProfilePicture;
    }
}
