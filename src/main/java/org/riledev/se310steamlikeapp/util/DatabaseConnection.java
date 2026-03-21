package org.riledev.se310steamlikeapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:steamdb.sqlite";

    public DatabaseConnection(){
        try {
            connection = DriverManager.getConnection(DB_URL);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to the SQLite database.");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance(){
        if(instance == null)
            instance = new DatabaseConnection();

        return instance;
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void closeConnection(){
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
