package com.main.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
    private TextField fieldCreatePassword;

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

            String query = "INSERT INTO students VALUES ('"+username+"', '"+studentId+"', '"+firstName+"', '"+lastName+"', '"+password+"'" +
                    ")";
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


}
