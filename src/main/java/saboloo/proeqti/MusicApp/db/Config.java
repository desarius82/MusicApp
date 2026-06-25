package saboloo.proeqti.MusicApp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Config {

    private static final String URL =
            "jdbc:mysql://localhost:3306/music_app?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Config() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}