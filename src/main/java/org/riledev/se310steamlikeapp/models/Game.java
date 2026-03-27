package org.riledev.se310steamlikeapp.models;

/**
 * Model klasa koja predstavlja video igru u prodavnici.
 * Cuva sve relevantne podatke o igri ukljucujuci naziv, opis,
 * cenu, putanju do naslovne slike i datum dodavanja.
 */
public class Game {

    /** Jedinstveni identifikator igre u bazi podataka. */
    private int id;

    /** Naziv igre. */
    private String title;

    /** Opis igre koji se prikazuje na stranici sa detaljima. */
    private String description;

    /** Cena igre u evrima. Vrednost 0.0 oznacava besplatnu igru. */
    private double price;

    /** Relativna putanja do naslovne slike igre u resursima. */
    private String coverImagePath;

    /** Datum dodavanja igre u prodavnicu (format iz baze podataka). */
    private String addedDate;

    /**
     * Podrazumevani konstruktor.
     * Kreira prazan Game objekat koji se popunjava putem settera.
     */
    public Game() {
    }

    /**
     * Vraca jedinstveni identifikator igre.
     *
     * @return ID igre
     */
    public int getId() {
        return id;
    }

    /**
     * Postavlja jedinstveni identifikator igre.
     *
     * @param id ID igre iz baze podataka
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Vraca naziv igre.
     *
     * @return naziv igre
     */
    public String getTitle() {
        return title;
    }

    /**
     * Postavlja naziv igre.
     *
     * @param title naziv igre za prikaz u prodavnici
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Vraca opis igre.
     *
     * @return tekstualni opis igre
     */
    public String getDescription() {
        return description;
    }

    /**
     * Postavlja opis igre.
     *
     * @param description tekstualni opis za stranicu sa detaljima
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Vraca cenu igre.
     *
     * @return cena igre u evrima
     */
    public double getPrice() {
        return price;
    }

    /**
     * Postavlja cenu igre.
     *
     * @param price cena u evrima (0.0 za besplatne igre)
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Vraca putanju do naslovne slike igre.
     *
     * @return relativna putanja do slike
     */
    public String getCoverImagePath() {
        return coverImagePath;
    }

    /**
     * Postavlja putanju do naslovne slike igre.
     *
     * @param coverImagePath relativna putanja unutar resursa
     */
    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    /**
     * Vraca datum dodavanja igre u prodavnicu.
     *
     * @return datum dodavanja kao string
     */
    public String getAddedDate() {
        return addedDate;
    }

    /**
     * Postavlja datum dodavanja igre.
     *
     * @param addedDate datum u formatu iz baze podataka
     */
    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}
