package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;
import org.riledev.se310steamlikeapp.util.InputValidator;
import org.riledev.se310steamlikeapp.util.PasswordHash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repozitorijum za upravljanje korisnickim nalozima.
 * Obezbeduje prijavu, registraciju i azuriranje profila korisnika.
 * Koristi BCrypt hesiranje lozinki putem PasswordHash klase.
 */
public class UserRepository {

    /**
     * Prijavljuje korisnika na sistem.
     * Proverava korisnicko ime, verifikuje lozinku putem BCrypt-a
     * i validira da je nalog aktivan (status_id = 1).
     *
     * @param username korisnicko ime
     * @param plainPassword lozinka u plain tekstu
     * @return User objekat ako je prijava uspesna, null u suprotnom
     */
    public User login(String username, String plainPassword) {
        String sql = "SELECT id, username, password, profile_color, profile_picture_path, status_id FROM User WHERE username = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {

            pstmt.setString(1, username.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");

                    // Verifikacija lozinke putem BCrypt-a
                    if (PasswordHash.checkPassword(plainPassword, storedHash)) {

                        // Provera da li je nalog aktivan
                        int statusId = rs.getInt("status_id");
                        if (statusId != 1) {
                            System.out.println("Login Denied: Account is not ACTIVE (Status ID: " + statusId + ")");
                            return null;
                        }

                        // Kreiranje User objekta sa podacima iz baze
                        User loggedInUser = new User();
                        loggedInUser.setId(rs.getInt("id"));
                        loggedInUser.setUsername(rs.getString("username"));
                        loggedInUser.setProfileColor(rs.getString("profile_color"));

                        // Podrazumevana profilna slika ako nije postavljena
                        String picPath = rs.getString("profile_picture_path");
                        loggedInUser.setProfilePicturePath(picPath != null ? picPath : "assets/profile/default.png");

                        loggedInUser.setStatusId(statusId);

                        System.out.println("Login Successful for user: " + username);
                        return loggedInUser;

                    } else {
                        System.out.println("Login Failed: Incorrect password for user " + username);
                    }
                } else {
                    System.out.println("Login Failed: Username '" + username + "' not found.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred during login process.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Registruje novog korisnika u sistemu.
     * Pre registracije proverava da li se lozinke poklapaju,
     * zatim hesira lozinku putem BCrypt-a i cuva korisnika u bazu.
     *
     * @param username korisnicko ime
     * @param plainPassword lozinka u plain tekstu
     * @param plainPasswordRepeated ponovljena lozinka za potvrdu
     * @return true ako je registracija uspesna
     */
    public boolean register(String username, String plainPassword, String plainPasswordRepeated) {
        // Validacija poklapanja lozinki
        if (!InputValidator.isPasswordConfirmed(plainPassword, plainPasswordRepeated)) {
            System.out.println("Registration Failed: Passwords do not match.");
            return false;
        }

        // Hesiranje lozinke pre cuvanja u bazu
        String hashedPassword = PasswordHash.hashPassword(plainPassword);

        String sql = "INSERT INTO User (username, password) VALUES (?, ?)";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            pstmt.setString(2, hashedPassword);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Registration successful for: " + username);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred during registration process.");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Azurira boju profila korisnika u bazi podataka.
     *
     * @param userId ID korisnika
     * @param hexColor HEX string boje (npr. "#1b2838")
     * @return true ako je azuriranje uspesno
     */
    public boolean updateProfileColor(int userId, String hexColor) {
        String sql = "UPDATE User SET profile_color = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {

            pstmt.setString(1, hexColor);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error occurred while updating profile color.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Azurira putanju do profilne slike korisnika u bazi podataka.
     *
     * @param userId ID korisnika
     * @param path relativna putanja do nove profilne slike
     * @return true ako je azuriranje uspesno
     */
    public boolean updateProfilePicturePath(int userId, String path) {
        String sql = "UPDATE User SET profile_picture_path = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {

            pstmt.setString(1, path);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Database error occurred while updating profile picture.");
            e.printStackTrace();
        }
        return false;
    }
}