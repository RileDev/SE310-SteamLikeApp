package org.riledev.se310steamlikeapp.util;

/**
 * Pomocna klasa za validaciju korisnickog unosa.
 * Sadrzi staticke metode za proveru praznih polja i potvrdu lozinke.
 */
public class InputValidator {

    /**
     * Proverava da li se unesena lozinka poklapa sa potvrdom lozinke.
     *
     * @param password originalna lozinka
     * @param confirmedPassword potvrda lozinke
     * @return true ako se lozinke poklapaju
     */
    public static boolean isPasswordConfirmed(String password, String confirmedPassword) {
        return password.trim().equals(confirmedPassword.trim());
    }

    /**
     * Proverava da li je uneta vrednost prazan string.
     *
     * @param value vrednost za proveru
     * @return true ako je string prazan
     */
    public static boolean isInputEmpty(String value) {
        return value.isEmpty();
    }

    /**
     * Proverava da li su polja za autentifikaciju prazna.
     * Ako confirmPassword nije prosljedjen (null), proverava samo username i password.
     *
     * @param username korisnicko ime
     * @param password lozinka
     * @param confirmPassword potvrda lozinke (moze biti null za prijavu)
     * @return true ako je bilo koje obavezno polje prazno
     */
    public static boolean isAuthFieldEmpty(String username, String password, String confirmPassword) {
        // Za prijavu, confirmPassword je null pa se ne proverava
        if (confirmPassword == null)
            return isInputEmpty(username) || isInputEmpty(password);

        return isInputEmpty(username) || isInputEmpty(password) || isInputEmpty(confirmPassword);
    }

}
