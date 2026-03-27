package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repozitorijum za pristup korisnickoj biblioteci igara.
 * Obezbeduje preuzimanje kupljenih igara, proveru vlasnistva
 * i brojanje igara u biblioteci korisnika.
 */
public class LibraryRepository {

    /**
     * Preuzima sve igre koje korisnik poseduje u svojoj biblioteci.
     * Koristi JOIN sa tabelom LibraryItem za povezivanje korisnika sa igrama.
     *
     * @param userId ID korisnika cija se biblioteka ucitava
     * @return lista igara u vlasnistvu korisnika
     */
    public List<Game> getOwnedGames(int userId) {
        List<Game> ownedGames = new ArrayList<>();

        // JOIN izmedju Game i LibraryItem tabela za pronalazenje kupljenih igara
        String sql = "SELECT g.* FROM Game g " +
                "INNER JOIN LibraryItem li ON g.id = li.game_id " +
                "WHERE li.user_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Game game = new Game();
                    game.setId(rs.getInt("id"));
                    game.setTitle(rs.getString("title"));
                    game.setDescription(rs.getString("description"));
                    game.setPrice(rs.getDouble("price"));
                    game.setCoverImagePath(rs.getString("cover_image_path"));
                    game.setAddedDate(rs.getString("added_date"));

                    ownedGames.add(game);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred while fetching library games.");
            e.printStackTrace();
        }

        return ownedGames;
    }

    /**
     * Proverava da li korisnik vec poseduje odredjenu igru.
     *
     * @param userId ID korisnika
     * @param gameId ID igre
     * @return true ako korisnik vec ima igru u biblioteci
     */
    public boolean doesUserOwnGame(int userId, int gameId) {
        String sql = "SELECT 1 FROM LibraryItem WHERE user_id = ? AND game_id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, gameId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Database error checking game ownership.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Vraca ukupan broj igara u biblioteci korisnika.
     *
     * @param userId ID korisnika
     * @return broj igara u biblioteci
     */
    public int getOwnedGamesCount(int userId) {
        String sql = "SELECT COUNT(*) FROM LibraryItem WHERE user_id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting owned games.");
            e.printStackTrace();
        }
        return 0;
    }
}
