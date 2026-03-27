package org.riledev.se310steamlikeapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton klasa za upravljanje konekcijom sa SQLite bazom podataka.
 * Obezbeduje globalni pristup jedinstvenoj konekciji i kontrolise
 * njeno otvaranje i zatvaranje tokom zivotnog veka aplikacije.
 */
public class DatabaseConnection {

    /** Jedina instanca DatabaseConnection klase. */
    private static DatabaseConnection instance;

    /** Aktivna konekcija ka SQLite bazi podataka. */
    private Connection connection;

    /** URL za povezivanje sa lokalnom SQLite bazom podataka. */
    private static final String DB_URL = "jdbc:sqlite:steamdb.sqlite";

    /**
     * Privatni konstruktor - uspostavlja konekciju ka bazi i ukljucuje
     * podrsku za strane kljuceve (PRAGMA foreign_keys).
     */
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);

            // Ukljucivanje podrske za strane kljuceve u SQLite
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to the SQLite database.");
            e.printStackTrace();
        }
    }

    /**
     * Vraca jedinu instancu DatabaseConnection klase.
     * Thread-safe implementacija putem synchronized kljucne reci.
     *
     * @return singleton instanca konekcije ka bazi
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null)
            instance = new DatabaseConnection();

        return instance;
    }

    /**
     * Vraca aktivnu konekciju ka bazi podataka.
     *
     * @return JDBC Connection objekat
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Zatvara konekciju ka bazi podataka.
     * Poziva se pri zaustavljanju aplikacije.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection.");
            e.printStackTrace();
        }
    }

}
