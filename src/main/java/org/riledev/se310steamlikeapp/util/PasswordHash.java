package org.riledev.se310steamlikeapp.util;


import org.mindrot.jbcrypt.BCrypt;

/**
 * Pomocna klasa za hesiranje i proveru lozinki.
 * Koristi BCrypt algoritam sa cost faktorom 12 za sigurno cuvanje lozinki.
 */
public class PasswordHash {

    /**
     * Hesira lozinku koristeci BCrypt algoritam.
     *
     * @param password lozinka u plain tekstu
     * @return hesirana lozinka
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Proverava da li se unesena lozinka poklapa sa hesiranom verzijom.
     *
     * @param password lozinka u plain tekstu za proveru
     * @param hashedPassword hesirana lozinka iz baze podataka
     * @return true ako se lozinke poklapaju
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
