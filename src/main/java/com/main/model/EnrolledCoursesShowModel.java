package com.main.model;

import com.main.controller.DatabaseConnection;
import javafx.collections.ObservableList;

import java.sql.*;

public class EnrolledCoursesShowModel {

    public ObservableList<CourseModel> show(ObservableList<CourseModel> searchResults) throws SQLException {
        //Gets all the details of the courses that the specific student is enrolled in.
        String q = "SELECT * FROM (student_enrolled_courses INNER JOIN courses " +
                "ON student_enrolled_courses.course_id = courses.course_id) " +
                "WHERE student_id LIKE '%" + CurrentUserModel.getUserId() + "%'";
        System.out.println(q);

        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);

        while (rs.next()) {
            searchResults.add(
                    new CourseModel(rs.getString("course_name"), rs.getString("capacity"), rs.getString("open_closed"), rs.getString("year"),
                            rs.getString("delivery_mode"), rs.getString("day_of_lecture"), rs.getString("time_of_lecture"),
                            rs.getDouble("duration_of_lecture"), rs.getString("dates"))
            );
        }

        conn.close();
        state.close();
        return searchResults;
    }
}
