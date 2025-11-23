package main.java.app.service;

import main.java.app.util.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load driver MySQL (optional di Java baru, tapi bagus untuk kompatibilitas)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Buka koneksi
            conn = DriverManager.getConnection(Config.DB_URL, Config.DB_USER, Config.DB_PASS);
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("ERROR: Gagal koneksi ke Database!");
            e.printStackTrace();
        }
        return conn;
    }
}