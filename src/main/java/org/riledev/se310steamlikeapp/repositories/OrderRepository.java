package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderRepository {

    public boolean processPurchase(int userId, Game game, String paymentMethod) {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            String orderSql = "INSERT INTO StoreOrder (user_id, total_amount) VALUES (?, ?)";
            int orderId = -1;

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, game.getPrice());
                orderStmt.executeUpdate();

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            String itemSql = "INSERT INTO OrderItem (order_id, game_id, price_at_purchase, payment_method) VALUES (?, ?, ?, ?)";
            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, game.getId());
                itemStmt.setDouble(3, game.getPrice());
                itemStmt.setString(4, paymentMethod);
                itemStmt.executeUpdate();
            }

            String librarySql = "INSERT INTO LibraryItem (user_id, game_id) VALUES (?, ?)";
            try (PreparedStatement libStmt = conn.prepareStatement(librarySql)) {
                libStmt.setInt(1, userId);
                libStmt.setInt(2, game.getId());
                libStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.err.println("Transaction rolled back due to error.");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}