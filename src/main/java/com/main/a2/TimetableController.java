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
import java.util.ArrayList;
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
    @FXML
    private Button btnVisible;

    private ArrayList<Label> allCourses = new ArrayList<>();
    TimetableChecks timeCheck = new TimetableChecks();
    private ArrayList<String> enrolled = new ArrayList<>();
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
        allCourses = getAllLabels();
        try {
            enrolled = timeCheck.getCoursesStrip();

            //Nested for loop. Checks to see if there are any matches between all your currently enrolled courses
            //against all the courses in the system/timetable
            for (int i=0;i<enrolled.size();i++) {
                for (int x=0;x<allCourses.size();x++) {
                    System.out.println(allCourses.get(x));
                    if (enrolled.get(i).equals(allCourses.get(x).getId())) {
                        allCourses.get(x).setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong!" + e);
        }
    }

    //Holy fuck this is lazy
    public ArrayList<Label> getAllLabels() {
        allCourses.add(Math);
        allCourses.add(Programmingskills);
        allCourses.add(Knowledgetechnologies);
        allCourses.add(Javaprogramming);
        allCourses.add(Datamining);
        allCourses.add(Algorithmsandcomplexity);
        allCourses.add(Advancedpythonprogramming);

        return allCourses;
    }
}
