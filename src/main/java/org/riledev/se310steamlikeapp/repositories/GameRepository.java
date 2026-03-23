package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Game ORDER BY added_date DESC";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setDescription(rs.getString("description"));
                game.setPrice(rs.getDouble("price"));
                game.setCoverImagePath(rs.getString("cover_image_path"));
                game.setAddedDate(rs.getString("added_date"));

                games.add(game);
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred while fetching games for the store.");
            e.printStackTrace();
        }

        return games;
    }

    public List<Game> searchGames(String keyword) {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Game WHERE title LIKE ? ORDER BY added_date DESC";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Game game = new Game();
                    game.setId(rs.getInt("id"));
                    game.setTitle(rs.getString("title"));
                    game.setDescription(rs.getString("description"));
                    game.setPrice(rs.getDouble("price"));
                    game.setCoverImagePath(rs.getString("cover_image_path"));
                    game.setAddedDate(rs.getString("added_date"));
                    games.add(game);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }
}
