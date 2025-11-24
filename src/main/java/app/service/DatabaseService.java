package main.java.app.service;

import main.java.app.util.Config;
import java.sql.*;

public class DatabaseService {
    
    // 1. Method Koneksi Standar
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(Config.DB_URL, Config.DB_USER, Config.DB_PASS);
        } catch (Exception e) {
            System.err.println("ERROR: Gagal koneksi ke Database! Cek Config.java");
            e.printStackTrace();
            return null;
        }
    }

    // 2. Method Inisialisasi (Adaptasi dari kode teman Anda)
    // Dipanggil sekali saat aplikasi start
    public static void initIfNeeded() {
        // SQL untuk membuat tabel jika belum ada
        String createSongs = "CREATE TABLE IF NOT EXISTS songs (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "title VARCHAR(100)," +
                "province VARCHAR(50)," +
                "audio_path VARCHAR(255)," +
                "lyrics TEXT," +
                "translation TEXT," +
                "cultural_values TEXT," +
                "thumbnail VARCHAR(255)" +
                ")";

        String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100)," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "points INT DEFAULT 0," +
                "badges TEXT" +
                ")";
        
        String createLeaderboard = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100)," +
                "email VARCHAR(100)," +
                "score INT," +
                "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String createProgress = "CREATE TABLE IF NOT EXISTS user_progress (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT," +
                "song_id INT," +
                "listened_seconds INT DEFAULT 0," +
                "quiz_score INT DEFAULT 0," +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                "FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE" +
                ")";

        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            // Eksekusi pembuatan tabel
            s.execute(createSongs);
            s.execute(createUsers);
            s.execute(createLeaderboard); // Tambahan untuk fitur leaderboard
            s.execute(createProgress);

            // 3. Seeding Data (Isi data otomatis jika kosong)
            seedSongsIfEmpty(c);
            seedDemoUserIfEmpty(c);
            seedLeaderboardIfEmpty(c); // Tambahan dummy leaderboard

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void seedSongsIfEmpty(Connection c) throws SQLException {
        String check = "SELECT id FROM songs LIMIT 1";
        try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery(check)) {
            if (!rs.next()) {
                System.out.println("INFO: Tabel songs kosong. Melakukan seeding data...");
                String sql = "INSERT INTO songs (title, province, audio_path, lyrics, translation, cultural_values, thumbnail) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    // Data 1: Bengkulu
                    ps.setString(1, "Lalan Belek");
                    ps.setString(2, "Bengkulu");
                    ps.setString(3, "bengkulu-udarang.wav");
                    ps.setString(4, "Oi lalan belek, lalan belek...");
                    ps.setString(5, "Wahai gadis cantik, gadis cantik...");
                    ps.setString(6, "Lagu ini menceritakan tentang nasihat pernikahan.");
                    ps.setString(7, "song-thumb-bengkulu.jpg");
                    ps.executeUpdate();

                    // Data 2: Jawa Tengah
                    ps.setString(1, "Ilir Ilir");
                    ps.setString(2, "Jawa Tengah");
                    ps.setString(3, "jawa-lirih.wav");
                    ps.setString(4, "Lir ilir, lir ilir, tandure wus sumilir...");
                    ps.setString(5, "Bangunlah, bangunlah, tanaman sudah bersemi...");
                    ps.setString(6, "Tembang ciptaan Sunan Kalijaga sebagai sarana dakwah.");
                    ps.setString(7, "song-thumb-jawa.jpg");
                    ps.executeUpdate();
                    
                    // Tambahkan data NTB jika perlu di sini...
                }
            }
        }
    }

    private static void seedDemoUserIfEmpty(Connection c) throws SQLException {
        String check = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement ps = c.prepareStatement(check)) {
            ps.setString(1, "demo@test.com");
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("INFO: Membuat user demo...");
                    String sql = "INSERT INTO users (name, email, password, points, badges) VALUES (?, ?, ?, 0, ?)";
                    try (PreparedStatement insert = c.prepareStatement(sql)) {
                        insert.setString(1, "Siswa Teladan");
                        insert.setString(2, "demo@test.com");
                        insert.setString(3, "demoPassword");
                        insert.setString(4, "Newbie");
                        insert.executeUpdate();
                    }
                }
            }
        }
    }
    
    private static void seedLeaderboardIfEmpty(Connection c) throws SQLException {
        String check = "SELECT id FROM leaderboard LIMIT 1";
        try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery(check)) {
            if (!rs.next()) {
                System.out.println("INFO: Seeding leaderboard...");
                String sql = "INSERT INTO leaderboard (name, email, score) VALUES (?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, "Budi Juara"); ps.setString(2, "budi@test.com"); ps.setInt(3, 150); ps.executeUpdate();
                    ps.setString(1, "Siti Pintar"); ps.setString(2, "siti@test.com"); ps.setInt(3, 120); ps.executeUpdate();
                }
            }
        }
    }
}