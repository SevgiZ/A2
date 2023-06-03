package com.main.model;

import com.main.controller.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class DashboardTableModel {
    @FXML
    private TableView<CourseModel> tableCourses;
    @FXML
    private TableColumn<CourseModel, String> capacity;

    @FXML
    private TableColumn<CourseModel, String> openclosed;

    @FXML
    private TableColumn<CourseModel, String> courseName;

    @FXML
    private TableColumn<CourseModel, String> day;

    @FXML
    private TableColumn<CourseModel, String> delivery;

    @FXML
    private TableColumn<CourseModel, Double> duration;
    @FXML
    private TableColumn<CourseModel, String> time;

    @FXML
    private TableColumn<CourseModel, String> year;
    @FXML
    private TableColumn<CourseModel, String> dates;

    private ObservableList<CourseModel> courses = FXCollections.observableArrayList();

    public void updateTable() {
        try {
            courses.clear();
            courses = LoadCoursesFromDBModel.Load(courses);
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getString("course_name"));

                //First var is the FXML name, later var is the object name
                courseName.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("name"));
                capacity.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("capacity"));
                openclosed.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("openclosed"));
                year.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("year"));
                delivery.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("delivery"));
                day.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("day"));
                time.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("time"));
                duration.setCellValueFactory(new PropertyValueFactory<CourseModel, Double>("duration"));
                dates.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("dates"));
            }
            rs.close();
            conn.close();
            state.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
