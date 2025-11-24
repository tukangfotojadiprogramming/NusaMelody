package main.java.app.service;

import main.java.app.model.LeaderboardEntry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardRepository {
    
    public void saveScore(String name, String email, int score) {
        String sql = "INSERT INTO leaderboard (name, email, score) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, score);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LeaderboardEntry> getTopScores() {
        List<LeaderboardEntry> list = new ArrayList<>();
        // Ambil 10 skor tertinggi
        String sql = "SELECT name, score, DATE_FORMAT(played_at, '%d-%m-%Y %H:%i') as formatted_date " +
                     "FROM leaderboard ORDER BY score DESC LIMIT 10";
        
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new LeaderboardEntry(
                    rs.getString("name"),
                    rs.getInt("score"),
                    rs.getString("formatted_date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}