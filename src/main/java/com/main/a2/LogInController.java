package com.main.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LogInController {
    private Stage stage;
    private Statement state;
    private String username;
    private String password;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private TextField fieldUsername;

    @FXML
    private Label txtLoginError;

    public void CreateAccountScene(ActionEvent event) throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("CreateAccount.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Create Account");
        stage.setScene(scene);
        stage.show();
    }

    public void LogIn(ActionEvent event) throws SQLException, IOException {
        System.out.println("Logging in, opening DB");
        Connection conn = DatabaseConnection.getConnection();
        username = fieldUsername.getText();
        password = fieldPassword.getText();

        if (VerifyPassword(conn, password) && VerifyUsername(conn, username)) {
            state = conn.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM students WHERE username LIKE '%" + username + "%'");

            while (rs.next()) {
                CurrentUser user = new CurrentUser(rs.getString("username"), rs.getString("first_name"),
                     rs.getString("last_name"), rs.getString("student_id"), rs.getString("password"));

            CurrentUser.SetUserInstance(user);
            System.out.println(user);
        }
            conn.close();
            FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("Dashboard.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1220, 517);
            stage.setTitle("myTimetable - Course Enrollment!");
            stage.setScene(scene);
            stage.show();
        }
    }

    public boolean VerifyUsername(Connection conn, String username) throws SQLException {
        String query = "SELECT * FROM students";
        state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            if (rs.getString("username").equals(username)) {
                System.out.println("valid username, returning true");
                return true;
            }
        }
        txtLoginError.setText("Invalid username and/or password!");
        return false;
    }

    public boolean VerifyPassword(Connection conn, String password) throws SQLException {
        String query = "SELECT * FROM students";
        state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            if (rs.getString("password").equals(password)) {
                System.out.println("valid pass, returning true");
                return true;
            }
        }
        txtLoginError.setText("Invalid username and/or password!");
        return false;
    }

}
