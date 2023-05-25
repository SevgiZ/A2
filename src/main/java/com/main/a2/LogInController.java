package com.main.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
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

    private boolean firstTimeSetup;

    LogInUserDetails logUserDetails = new LogInUserDetails();
    DatabaseCreation dbCreate = new DatabaseCreation();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Checks to see if the database file already exists
        //Done we know if we should bother try creating and populating the tables
        File f = new File("src\\database\\myTimetable.db");

        if (f.exists() && !f.isDirectory()) {
            System.out.println("Database already exists!");
            firstTimeSetup = false;
        } else {
            System.out.println("DB doesn't exist, running first time setup for DB!");
            firstTimeSetup = true;
        }

        if (firstTimeSetup) {
            try {
                dbCreate.createDatabase();
                dbCreate.createTables();
                dbCreate.populate();
                firstTimeSetup = false;

            } catch (Exception e) {
                System.out.println("Something went wrong while trying to create DB!");
            }

        }

    }

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
