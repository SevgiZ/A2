package com.main.a2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:sqlite:src\\database\\myTimetable.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

}
