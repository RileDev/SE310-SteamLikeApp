package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibraryRepository {

    public List<Game> getOwnedGames(int userId){
        List<Game> ownedGames = new ArrayList<>();

        String sql = "SELECT g.* FROM Game g " +
                "INNER JOIN LibraryItem li ON g.id = li.game_id " +
                "WHERE li.user_id = ?";

        try(PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)){
            pstmt.setInt(1, userId);

            try(ResultSet rs = pstmt.executeQuery()){
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

        }catch (SQLException e){
            System.err.println("Database error occurred while fetching library games.");
            e.printStackTrace();
        }

        return ownedGames;
    }

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
}
