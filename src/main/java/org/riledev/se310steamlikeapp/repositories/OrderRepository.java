package org.riledev.se310steamlikeapp.repositories;

import org.riledev.se310steamlikeapp.models.Game;
import org.riledev.se310steamlikeapp.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Repozitorijum za obradu narudzbina i kupovine igara.
 * Koristi transakcioni pristup sa tri koraka: kreiranje narudzbine,
 * dodavanje stavke narudzbine i dodavanje igre u biblioteku korisnika.
 */
public class OrderRepository {

    /**
     * Obradjuje kompletnu kupovinu igre u okviru jedne transakcije.
     * Transakcija obuhvata tri operacije:
     * 1. Kreiranje zapisa u StoreOrder tabeli
     * 2. Kreiranje stavke narudzbine u OrderItem tabeli
     * 3. Dodavanje igre u korisnikovu biblioteku (LibraryItem tabela)
     *
     * @param userId ID korisnika koji kupuje igru
     * @param game igra koja se kupuje
     * @param paymentMethod naziv metode placanja (npr. "paypal")
     * @return true ako je transakcija uspesno izvrsena
     */
    public boolean processPurchase(int userId, Game game, String paymentMethod) {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try {
            // Iskljucivanje auto-commit-a za transakciono upravljanje
            conn.setAutoCommit(false);

            // Korak 1: Kreiranje narudzbine
            String orderSql = "INSERT INTO StoreOrder (user_id, total_amount) VALUES (?, ?)";
            int orderId = -1;

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, game.getPrice());
                orderStmt.executeUpdate();

                // Preuzimanje generisanog ID-a narudzbine
                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            // Korak 2: Kreiranje stavke narudzbine sa cenom u trenutku kupovine
            String itemSql = "INSERT INTO OrderItem (order_id, game_id, price_at_purchase, payment_method) VALUES (?, ?, ?, ?)";
            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, game.getId());
                itemStmt.setDouble(3, game.getPrice());
                itemStmt.setString(4, paymentMethod);
                itemStmt.executeUpdate();
            }

            // Korak 3: Dodavanje igre u korisnikovu biblioteku
            String librarySql = "INSERT INTO LibraryItem (user_id, game_id) VALUES (?, ?)";
            try (PreparedStatement libStmt = conn.prepareStatement(librarySql)) {
                libStmt.setInt(1, userId);
                libStmt.setInt(2, game.getId());
                libStmt.executeUpdate();
            }

            // Potvrda transakcije
            conn.commit();
            return true;

        } catch (SQLException e) {
            // Ponistavanje transakcije u slucaju greske
            try {
                conn.rollback();
                System.err.println("Transaction rolled back due to error.");
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            // Vracanje auto-commit-a na podrazumevanu vrednost
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}