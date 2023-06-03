package com.main.model;

import com.main.controller.CurrentUserHolder;
import com.main.controller.DatabaseConnection;

import java.sql.*;

public class ChangeDetailsModel {
    public void change(String fieldChangeFirstName, String fieldChangeLastName, String fieldChangePassword) throws SQLException {
        CurrentUserHolder uh = CurrentUserHolder.getCurrentUser();
        CurrentUserModel u = uh.getUser();

        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();

        String q = "UPDATE students SET first_name = '" + fieldChangeFirstName + "', " +
                "last_name = '" + fieldChangeLastName + "', " +
                "password = '" + fieldChangePassword + "' " +
                "WHERE student_id = '" + u.getUserId() + "'";
        System.out.println(q);

        state.executeUpdate(q);

        ResultSet rs = state.executeQuery("SELECT * FROM students WHERE username = '" + u.getUsername() + "'");

        while (rs.next()) {
            CurrentUserModel user = new CurrentUserModel(rs.getString("username"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("student_id"), rs.getString("password"));

            CurrentUserModel.SetUserInstance(user);
        }

        conn.close();
        state.close();
        rs.close();
    }

}
