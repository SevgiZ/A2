package com.main.a2;
import java.sql.*;

public class DatabaseConnect {
    static Connection conn = null;

    public static void Connect() throws SQLException {
        try {
            String url = "jdbc:sqlite:src\\database\\mytimetable.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Should be connected to database");

            String command = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(command);

            while (rs.next()) {
                System.out.println(rs.getString("course_name"));
            }
            state.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
