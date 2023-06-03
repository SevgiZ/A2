package com.main.model;

import com.main.controller.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class LoadCoursesFromDBModel {
    private ObservableList<CourseModel> courseList = FXCollections.observableArrayList();

    public static ObservableList<CourseModel> Load(ObservableList<CourseModel> courseList) throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                courseList.add(new CourseModel(rs.getString("course_name"), rs.getString("capacity"), rs.getString("open_closed"),
                        rs.getString("year"), rs.getString("delivery_mode"), rs.getString("day_of_lecture"),
                        rs.getString("time_of_lecture"), rs.getDouble("duration_of_lecture"), rs.getString("dates")));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return courseList;


    }


}
