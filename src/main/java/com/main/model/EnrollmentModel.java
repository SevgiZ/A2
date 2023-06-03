package com.main.model;

import com.main.controller.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class EnrollmentModel {
    private CourseSlotsModel courseSlots = new CourseSlotsModel();

    public void enroll(int courseId, CourseModel c) throws SQLException {
        String q = "INSERT INTO student_enrolled_courses (student_id, course_id) " +
                "VALUES ('" + CurrentUserModel.getUserId() + "', " + courseId + ");";

        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();

        state.executeUpdate(q);

        courseSlots.RemoveCourseSlot(c);

        conn.close();
        state.close();
    }


}
