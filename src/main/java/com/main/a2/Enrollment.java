package com.main.a2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Enrollment {
    CourseSlots courseSlots = new CourseSlots();

    public void enroll(int courseId, Course c) throws SQLException {
        String q = "INSERT INTO student_enrolled_courses (student_id, course_id) " +
                "VALUES ('" + CurrentUser.getUserId() + "', " + courseId + ");";



        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();

        state.executeUpdate(q);

        courseSlots.RemoveCourseSlot(c);

        conn.close();
        state.close();
    }


}
