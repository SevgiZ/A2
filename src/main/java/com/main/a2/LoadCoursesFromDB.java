package com.main.a2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class LoadCoursesFromDB {
    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    public static ObservableList<Course> Load(ObservableList<Course> courseList) throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                courseList.add(new Course(rs.getString("course_name"), rs.getString("capacity"), rs.getString("open_closed"),
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
