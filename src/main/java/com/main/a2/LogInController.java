package com.main.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {
    private Stage stage;

    @FXML
    private Button btnCreateAccount;

    @FXML
    private Button btnSignIn;

    @FXML
    private TextField fieldPassword;

    @FXML
    private TextField fieldUsername;

    public void LogInScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("CourseList.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1110, 517);
        stage.setTitle("myTimetable - Course Enrollment!");
        stage.setScene(scene);
        stage.show();
    }

    public void CreateAccountScene(ActionEvent event) throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("CreateAccount.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Create Account");
        stage.setScene(scene);
        stage.show();
    }

}
