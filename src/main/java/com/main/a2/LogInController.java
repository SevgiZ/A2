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

    LogInUserDetails logUserDetails = new LogInUserDetails();

    public void CreateAccountScene(ActionEvent event) throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CreateAccount.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Create Account");
        stage.setScene(scene);
        stage.show();
    }

    public void LogIn(ActionEvent event) throws SQLException, IOException {
        username = fieldUsername.getText();
        password = fieldPassword.getText();

        //Set the current user details locally in the program. Used for details at top and something else
        //I forgot it's been 3 weeks
        if (logUserDetails.verifyPassword(password) && logUserDetails.verifyUsername(username)) {
            CurrentUser user = logUserDetails.getUserDetails(username);
            CurrentUser.SetUserInstance(user);
            System.out.println(user);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1220, 517);
            stage.setTitle("myTimetable - Course Enrollment!");
            stage.setScene(scene);
            stage.show();
            }

        else {
            txtLoginError.setText("Invalid username and/or password!");
            }
        }
}
