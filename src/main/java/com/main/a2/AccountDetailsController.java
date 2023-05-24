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

    ChangeDetails changeDetails = new ChangeDetails();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CurrentUserHolder uh = CurrentUserHolder.getCurrentUser();
        CurrentUser u = uh.getUser();

        fieldChangeFirstName.setText(u.getFirstName());
        fieldChangeLastName.setText(u.getFirstName());
        fieldChangePassword.setText(u.getPassword());

    }

    @FXML
    void ChangeAccountDetails() throws SQLException {
        changeDetails.change(fieldChangeFirstName.getText(), fieldChangeLastName.getText(), fieldChangePassword.getText());
        /*
        CurrentUserHolder uh = CurrentUserHolder.getCurrentUser();
        CurrentUser u = uh.getUser();

        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        Statement state = conn.createStatement();

        String q = "UPDATE students SET first_name = '" + fieldChangeFirstName.getText( ) + "', " +
                "last_name = '" + fieldChangeLastName.getText() + "', " +
                "password = '" + fieldChangePassword.getText() + "' " +
                "WHERE student_id = '" + u.getUserId() + "'";
        System.out.println(q);

        state.executeUpdate(q);

        ResultSet rs = state.executeQuery("SELECT * FROM students WHERE username LIKE '%" + u.getUsername() + "%'");

        while (rs.next()) {
            CurrentUser user = new CurrentUser(rs.getString("username"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("student_id"), rs.getString("password"));

            CurrentUser.SetUserInstance(user);
        }

        conn.close();
        state.close();
        rs.close();*/

        labelNotif.setText("Details changed!");
    }

    public void LogInScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1220, 517);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

}
