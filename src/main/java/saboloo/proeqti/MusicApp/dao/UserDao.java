package saboloo.proeqti.MusicApp.dao;

import saboloo.proeqti.MusicApp.db.Config;
import saboloo.proeqti.MusicApp.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDao {


    public boolean register(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return false;
            }
            throw e;
        }
    }

    public Optional<User> login(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, is_premium FROM users WHERE username = ?";
        try (Connection c = Config.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()
                        && password.matches(password)) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getBoolean("is_premium")));
                }
            }
        }
        return Optional.empty();
    }

    public void upgradeToPremium(int userId) throws SQLException {
        try (Connection c = Config.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement up = c.prepareStatement(
                    "UPDATE users SET is_premium = TRUE WHERE id = ?");
                 PreparedStatement sub = c.prepareStatement(
                         "INSERT INTO subscriptions (user_id, plan) VALUES (?, 'PREMIUM')")) {
                up.setInt(1, userId);
                up.executeUpdate();
                sub.setInt(1, userId);
                sub.executeUpdate();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
}
