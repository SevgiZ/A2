package com.main.controller;

import com.main.model.CreateAccountModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private TextField fieldCreateFirstName;

    @FXML
    private TextField fieldCreateLastName;

    @FXML
    private PasswordField fieldCreatePassword;

    @FXML
    private TextField fieldCreateStudentId;

    @FXML
    private TextField fieldCreateUsername;

    @FXML
    private Label txtUsernameError;

    @FXML
    private Label txtIdError;
    @FXML
    private Label labelAccCreated;
    private CreateAccountModel createAccount = new CreateAccountModel();

    public void LogInScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogInView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

    public void CreateAccount() throws SQLException {
        username = fieldCreateUsername.getText();
        studentId = fieldCreateStudentId.getText();
        firstName = fieldCreateFirstName.getText();
        lastName = fieldCreateLastName.getText();
        password = fieldCreatePassword.getText();

        //Cant have these in if statements because wont trigger both error messages
        boolean userCheck = createAccount.existingUsername(username, txtUsernameError);
        boolean idCheck = createAccount.existingStudentId(studentId, txtIdError);

        if (userCheck || idCheck) {
            System.out.println("Error! Details already exist!");
            labelAccCreated.setText("");
        } else {
            createAccount.create(username, studentId, firstName, lastName, password);
            labelAccCreated.setText("Account created!");
        }

    }




}
