package com.employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Utility Class
 * Handles MySQL database connections using environment variables
 */
public class DatabaseConnection {
    
    // Static block to load environment variables
    static {
        EnvLoader.loadEnv();
    }
    
    /**
     * Establish and return a database connection using environment variables
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Get database configuration from environment variables
            String host = EnvLoader.getEnv("DB_HOST", "localhost");
            String port = EnvLoader.getEnv("DB_PORT", "3306");
            String dbName = EnvLoader.getEnv("DB_NAME", "employee_db");
            String username = EnvLoader.getEnv("DB_USERNAME", "root");
            String password = EnvLoader.getEnv("DB_PASSWORD", "");
            String driver = EnvLoader.getEnv("DB_DRIVER", "com.mysql.cj.jdbc.Driver");
            
            // Build connection URL
            String url = String.format("jdbc:mysql://%s:%s/%s", host, port, dbName);
            
            // Load MySQL JDBC driver
            Class.forName(driver);
            
            // Create connection
            Connection connection = DriverManager.getConnection(url, username, password);
            
            System.out.println("✅ Database connection established successfully!");
            System.out.println("🔗 Connected to: " + host + ":" + port + "/" + dbName);
            return connection;
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage(), e);
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database: " + e.getMessage());
            System.err.println("💡 Please check your .env file configuration");
            throw e;
        }
    }
    
    /**
     * Close database connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔐 Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}