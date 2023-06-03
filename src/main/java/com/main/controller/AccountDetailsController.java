package com.main.controller;

import com.main.model.ChangeDetailsModel;
import com.main.model.CurrentUserModel;
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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AccountDetailsController implements Initializable {

    @FXML
    private Stage stage;

    @FXML
    private Label labelNotif;

    @FXML
    private TextField fieldChangeFirstName;

    @FXML
    private TextField fieldChangeLastName;

    @FXML
    private PasswordField fieldChangePassword;

    private ChangeDetailsModel changeDetails = new ChangeDetailsModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CurrentUserHolder uh = CurrentUserHolder.getCurrentUser();
        CurrentUserModel u = uh.getUser();

        fieldChangeFirstName.setText(u.getFirstName());
        fieldChangeLastName.setText(u.getFirstName());
        fieldChangePassword.setText(u.getPassword());

    }

    @FXML
    void ChangeAccountDetails() throws SQLException {
        changeDetails.change(fieldChangeFirstName.getText(), fieldChangeLastName.getText(), fieldChangePassword.getText());
        labelNotif.setText("Details changed!");
    }

    public void LogInScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("DashboardView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1220, 517);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

}
