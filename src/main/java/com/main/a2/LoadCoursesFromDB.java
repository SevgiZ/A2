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
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                courseList.add(new Course(rs.getString("course_name"), rs.getString("capacity"),
                        rs.getString("year"), rs.getString("delivery_mode"), rs.getString("day_of_lecture"),
                        rs.getString("day_of_lecture"), rs.getDouble("duration_of_lecture")));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("DB LOAD:::"+courseList);
        System.out.println("DB LOAD:" + courseList.get(0).getSlotsLeft());
        return courseList;


    }


}
