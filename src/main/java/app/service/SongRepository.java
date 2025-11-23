package main.java.app.service;

import main.java.app.model.RegionalSong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongRepository {

    public List<RegionalSong> getAllSongs() {
        List<RegionalSong> songList = new ArrayList<>();
        String query = "SELECT * FROM songs";

        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Mapping dari baris Tabel ke Object Java (ORM konsep sederhana)
                RegionalSong song = new RegionalSong(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("province"),
                    rs.getString("audio_path"),
                    rs.getString("lyrics"),
                    rs.getString("translation"),
                    rs.getString("cultural_values"),
                    rs.getString("thumbnail")
                );
                songList.add(song);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songList;
    }
}