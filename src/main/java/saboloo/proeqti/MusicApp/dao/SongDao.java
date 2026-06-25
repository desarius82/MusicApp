package saboloo.proeqti.MusicApp.dao;

import saboloo.proeqti.MusicApp.db.Config;
import saboloo.proeqti.MusicApp.model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SongDao {

    public List<Song> findAll() throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT id, title, artist, stream_url FROM songs ORDER BY id";
        try (Connection c = Config.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                songs.add(new Song(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        rs.getString("stream_url")));
            }
        }
        return songs;
    }

    public List<Song> findLiked(int userId) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.id, s.title, s.artist, s.stream_url "
                + "FROM songs s JOIN liked_songs l ON s.id = l.song_id "
                + "WHERE l.user_id = ? ORDER BY l.liked_date DESC";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    songs.add(new Song(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("artist"),
                            rs.getString("stream_url")));
                }
            }
        }
        return songs;
    }

    public void like(int userId, int songId) throws SQLException {
        String sql = "INSERT IGNORE INTO liked_songs (user_id, song_id) VALUES (?, ?)";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }
    }

    public void unlike(int userId, int songId) throws SQLException {
        String sql = "DELETE FROM liked_songs WHERE user_id = ? AND song_id = ?";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();
        }
    }

    public boolean isLiked(int userId, int songId) throws SQLException {
        String sql = "SELECT 1 FROM liked_songs WHERE user_id = ? AND song_id = ?";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
