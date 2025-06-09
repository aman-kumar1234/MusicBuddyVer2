package com.musicapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseSetup {
    
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "Putin@777";
    
    public static void initializeDatabase() {
        try {
            // Create database if it doesn't exist
            Connection conn = DriverManager.getConnection(MYSQL_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS musicapp");
            stmt.close();
            conn.close();
            
            // Connect to the musicapp database and create tables
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            
            // Read and execute schema.sql
            String schema = new String(Files.readAllBytes(Paths.get("src/main/resources/sql/schema.sql")));
            String[] statements = schema.split(";");
            
            for (String sql : statements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    stmt.executeUpdate(sql);
                }
            }
            
            stmt.close();
            conn.close();
            
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}