package com.main.a2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LogInUserDetails {
    public CurrentUser getUserDetails(String username) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM students WHERE username = '" + username + "'");

        CurrentUser user = new CurrentUser(rs.getString("username"), rs.getString("first_name"),
                rs.getString("last_name"), rs.getString("student_id"), rs.getString("password"));

        conn.close();
        state.close();
        rs.close();
        return user;
    }

    public boolean verifyUsername(String username) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM students";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            if (rs.getString("username").equals(username)) {
                System.out.println("valid username, returning true");
                rs.close();
                conn.close();
                state.close();
                return true;
            }
        }
        rs.close();
        conn.close();
        state.close();
        return false;
    }

    public boolean verifyPassword(String password) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM students";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            if (rs.getString("password").equals(password)) {
                System.out.println("valid pass, returning true");
                conn.close();
                state.close();
                return true;
            }
        }
        conn.close();
        state.close();
        return false;
    }
}
