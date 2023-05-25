package com.main.a2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class DashboardTable {
    @FXML
    private TableView<Course> tableCourses;
    @FXML
    private TableColumn<Course, String> capacity;

    @FXML
    private TableColumn<Course, String> openclosed;

    @FXML
    private TableColumn<Course, String> courseName;

    @FXML
    private TableColumn<Course, String> day;

    @FXML
    private TableColumn<Course, String> delivery;

    @FXML
    private TableColumn<Course, Double> duration;
    @FXML
    private TableColumn<Course, String> time;

    @FXML
    private TableColumn<Course, String> year;
    @FXML
    private TableColumn<Course, String> dates;

    ObservableList<Course> courses = FXCollections.observableArrayList();

    public void updateTable() {
        try {
            courses.clear();
            courses = LoadCoursesFromDB.Load(courses);
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getString("course_name"));

                //First var is the FXML name, later var is the object name
                courseName.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
                capacity.setCellValueFactory(new PropertyValueFactory<Course, String>("capacity"));
                openclosed.setCellValueFactory(new PropertyValueFactory<Course, String>("openclosed"));
                year.setCellValueFactory(new PropertyValueFactory<Course, String>("year"));
                delivery.setCellValueFactory(new PropertyValueFactory<Course, String>("delivery"));
                day.setCellValueFactory(new PropertyValueFactory<Course, String>("day"));
                time.setCellValueFactory(new PropertyValueFactory<Course, String>("time"));
                duration.setCellValueFactory(new PropertyValueFactory<Course, Double>("duration"));
                dates.setCellValueFactory(new PropertyValueFactory<Course, String>("dates"));
            }
            rs.close();
            conn.close();
            state.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
