package com.main.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateAccountController {
    private Stage stage;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String studentId;

    @FXML
    private Button btnCreateAccount;

    @FXML
    private Button btnGoBack;

    @FXML
    private TextField fieldCreateFirstName;

    @FXML
    private TextField fieldCreateLastName;

    @FXML
    private PasswordField fieldCreatePassword;

    @FXML
    private TextField fieldCreateStudentId;

    @FXML
    private TextField fieldCreateUsername;

    public void LogInScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("LogIn.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

    public void CreateAccount() throws SQLException {
        System.out.println("Connecting to DB");
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            System.out.println("Connected to database");

            username = fieldCreateUsername.getText();
            studentId = fieldCreateStudentId.getText();
            firstName = fieldCreateFirstName.getText();
            lastName = fieldCreateLastName.getText();
            password = fieldCreatePassword.getText();

            if (!ExistingUsername(conn, username) && !ExistingStudentId(conn, studentId)) {

                String query = "INSERT INTO students VALUES ('" + username + "', '" + studentId + "', '" + firstName + "', '" + lastName + "', '" + password + "'" +
                        ")";
                System.out.println(query);
                Statement state = conn.createStatement();
                state.executeUpdate(query);
                state.close();
                conn.close();
            }

            else {
                System.out.println("Details already exist!");
            }
        }
        catch (Exception e) {
            System.out.println("Couldn't connect: " + e);
        }
    }

    public boolean ExistingUsername(Connection conn, String username) throws SQLException {
        String query = "SELECT * FROM students";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            System.out.println(rs.getString("username"));
            if (rs.getString("username").equals(username)) {
                System.out.println("existing username, returning true");
                return true;
            }
        }
        System.out.println("returning false");
        return false;
    }

    public boolean ExistingStudentId(Connection conn, String username) throws SQLException {
        String query = "SELECT * FROM students";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);

        while (rs.next()) {
            System.out.println(rs.getString("student_id"));
            if (rs.getString("student_id").equals(studentId)) {
                System.out.println("existing id, returning true");
                return true;
            }
        }
        System.out.println("returning false");
        return false;
    }


}
