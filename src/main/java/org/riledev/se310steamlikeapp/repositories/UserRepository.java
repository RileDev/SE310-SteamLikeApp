package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.User;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;
import org.riledev.se310steamlikeapp.util.InputValidator;
import org.riledev.se310steamlikeapp.util.PasswordHash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public User login(String username, String plainPassword){
        String sql = "SELECT id, username, password, profile_color, profile_picture_path, status_id FROM User WHERE username = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {

            pstmt.setString(1, username.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");

                    if (PasswordHash.checkPassword(plainPassword, storedHash)) {

                        int statusId = rs.getInt("status_id");
                        if (statusId != 1) {
                            System.out.println("Login Denied: Account is not ACTIVE (Status ID: " + statusId + ")");
                            return null;
                        }

                        User loggedInUser = new User();
                        loggedInUser.setId(rs.getInt("id"));
                        loggedInUser.setUsername(rs.getString("username"));
                        loggedInUser.setProfileColor(rs.getString("profile_color"));

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

    public boolean register(String username, String plainPassword, String plainPasswordRepeated){
        if (!InputValidator.isPasswordConfirmed(plainPassword, plainPasswordRepeated)) {
            System.out.println("Registration Failed: Passwords do not match.");
            return false;
        }
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
}