package main.java.app.db;

import java.sql.*;

/**
 * DB utility for MySQL (plain-password version).
 * Ubah URL, USER, PASS sesuai environment.
 */
public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/nusamelody?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Initialize DB tables and seed demo data (plain password).
     */
    public static void initIfNeeded() {
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
            s.execute(createSongs);
            s.execute(createUsers);
            s.execute(createProgress);

            // seed songs if empty
            String checkSong = "SELECT id FROM songs LIMIT 1";
            try (ResultSet rs = s.executeQuery(checkSong)) {
                if (!rs.next()) {
                    String insSong = "INSERT INTO songs (title, province, audio_path, lyrics, translation, cultural_values, thumbnail) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = c.prepareStatement(insSong)) {
                        ps.setString(1, "Lalan Belek");
                        ps.setString(2, "Bengkulu");
                        ps.setString(3, "bengkulu-udarang.wav");
                        ps.setString(4, "Oi lalan belek, lalan belek...");
                        ps.setString(5, "Wahai gadis cantik, gadis cantik...");
                        ps.setString(6, "Lagu ini menceritakan tentang nasihat pernikahan dan kehidupan gadis Bengkulu.");
                        ps.setString(7, "song-thumb-bengkulu.jpg");
                        ps.executeUpdate();

                        ps.setString(1, "Ilir Ilir");
                        ps.setString(2, "Jawa Tengah");
                        ps.setString(3, "jawa-lirih.wav");
                        ps.setString(4, "Lir ilir, lir ilir, tandure wus sumilir...");
                        ps.setString(5, "Bangunlah, bangunlah, tanaman sudah bersemi...");
                        ps.setString(6, "Tembang ciptaan Sunan Kalijaga sebagai sarana dakwah Islam di tanah Jawa.");
                        ps.setString(7, "song-thumb-jawa.jpg");
                        ps.executeUpdate();
                    }
                }
            }

            // ensure demo user exists (plain password = demoPassword)
            String checkDemo = "SELECT id FROM users WHERE email = ?";
            try (PreparedStatement ps = c.prepareStatement(checkDemo)) {
                ps.setString(1, "demo@test.com");
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        String ins = "INSERT INTO users (name, email, password, points, badges) VALUES (?, ?, ?, 0, ?)";
                        try (PreparedStatement ips = c.prepareStatement(ins)) {
                            ips.setString(1, "Siswa Teladan");
                            ips.setString(2, "demo@test.com");
                            ips.setString(3, "demoPassword"); // plain password for demo
                            ips.setString(4, "Newbie");
                            ips.executeUpdate();
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
