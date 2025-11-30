package com.humanitarian.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Test database connection utility
 * Uses a separate test database or in-memory database for testing
 */
public class TestDBConnection {
    private static final String TEST_DB_URL = "jdbc:postgresql://localhost:5432/humanitarian_support_test";
    private static final String TEST_DB_USER = "postgres";
    private static final String TEST_DB_PASSWORD = "postgres";
    
    private static Connection testConnection = null;

    public static Connection getTestConnection() {
        try {
            if (testConnection == null || testConnection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                testConnection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Test database connection error: " + e.getMessage());
        }
        return testConnection;
    }

    public static void closeTestConnection() {
        try {
            if (testConnection != null && !testConnection.isClosed()) {
                testConnection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing test connection: " + e.getMessage());
        }
    }
}

