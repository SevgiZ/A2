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

    public void updateCurrentUser(String student_id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();

        String q = "INSERT INTO current_user VALUES ('" + student_id + "', 1)";

        state.executeUpdate(q);
        conn.close();
        state.close();
    }

    public String checkCurrentUser() throws SQLException {
        String username = null;
        String q = "SELECT * FROM current_user";

        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);

        //while (rs.next()) {
            username = rs.getString("username");
        //}

        conn.close();
        state.close();
        System.out.println(username);
        return username;
    }

    public void removeCurrentUser() throws SQLException {
        String q = "DELETE FROM current_user WHERE signed_in = 1";
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        state.executeUpdate(q);
        conn.close();
        state.close();
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
