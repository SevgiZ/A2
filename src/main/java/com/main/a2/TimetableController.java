package com.main.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TimetableController implements Initializable {

    @FXML
    private Label Advancedpythonprogramming;

    @FXML
    private Label Algorithmsandcomplexity;

    @FXML
    private Label Datamining;

    @FXML
    private Label Javaprogramming;

    @FXML
    private Label Knowledgetechnologies;

    @FXML
    private Label Math;

    @FXML
    private Label Programmingskills;

    @FXML
    private Button btnDashboard;
    TimetableChecks timeCheck = new TimetableChecks();
    private Stage stage;
    public void DashboardScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1220, 517);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            timeCheck.getCoursesStrip();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }
}
