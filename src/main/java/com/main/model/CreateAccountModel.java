package com.main.model;

import com.main.controller.DatabaseConnection;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateAccountModel {
    public void create(String username, String studentId, String firstName, String lastName, String password) {
        try {
            Connection conn = DatabaseConnection.getConnection();

                String query = "INSERT INTO students VALUES ('" + username + "', '" + studentId +
                        "', '" + firstName + "', '" + lastName + "', '" + password + "'" + ")";
                System.out.println(query);
                Statement state = conn.createStatement();
                state.executeUpdate(query);
                state.close();
                conn.close();

        }
        catch (Exception e) {
            System.out.println("Couldn't connect: " + e);
        }
    }

    public boolean existingUsername(String username, Label txtUsernameError) throws SQLException {
        String query = "SELECT * FROM students";
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            System.out.println(rs.getString("username"));
            if (rs.getString("username").equals(username)) {
                txtUsernameError.setText("Username already taken!");
                conn.close();
                return true;
            }
        }
        conn.close();
        txtUsernameError.setText("");
        return false;
    }

    public boolean existingStudentId(String studentId, Label txtIdError) throws SQLException {
        String query = "SELECT * FROM students";
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            System.out.println(rs.getString("student_id"));
            if (rs.getString("student_id").equals(studentId)) {
                conn.close();
                txtIdError.setText("Student ID already taken or invalid!");
                return true;
            }
        }
        conn.close();
        txtIdError.setText("");
        return false;
    }
}
