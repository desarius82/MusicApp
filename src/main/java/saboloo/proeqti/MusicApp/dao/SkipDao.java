package saboloo.proeqti.MusicApp.dao;

import saboloo.proeqti.MusicApp.db.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkipDao {

    public static final int FREE_SKIP_LIMIT_PER_HOUR = 4;

    public int skipsInLastHour(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM skip_log "
                + "WHERE user_id = ? AND skip_date >= (NOW() - INTERVAL 1 HOUR)";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public void logSkip(int userId) throws SQLException {
        String sql = "INSERT INTO skip_log (user_id) VALUES (?)";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
