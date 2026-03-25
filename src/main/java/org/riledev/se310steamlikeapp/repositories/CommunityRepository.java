package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.CommunityPost;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommunityRepository {
    public boolean createPost(int userId, String content) {
        String sql = "INSERT INTO CommunityPost (user_id, content) VALUES (?, ?)";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, content);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error occurred while creating a community post.");
            e.printStackTrace();
        }
        return false;
    }

    public List<CommunityPost> getAllPosts() {
        List<CommunityPost> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.user_id, p.content, p.posted_date, u.username, u.profile_picture_path " +
                "FROM CommunityPost p " +
                "INNER JOIN User u ON p.user_id = u.id " +
                "ORDER BY p.posted_date DESC";

        try (PreparedStatement pstmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CommunityPost post = new CommunityPost();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setContent(rs.getString("content"));
                post.setPostedDate(rs.getString("posted_date"));

                post.setAuthorUsername(rs.getString("username"));
                post.setAuthorProfilePicture(rs.getString("profile_picture_path"));

                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("Database error occurred while fetching community posts.");
            e.printStackTrace();
        }
        return posts;
    }
}
