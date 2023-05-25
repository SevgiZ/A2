package com.main.a2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Withdraw {
    EnrollmentChecks timetableCheck = new EnrollmentChecks();

    public void withdraw(Course c) throws SQLException {
        String q = "DELETE FROM student_enrolled_courses WHERE course_id = " + timetableCheck.GetDbCourseId(c) + " AND student_id LIKE " +
                "'%" + CurrentUser.getUserId() + "%'";

        System.out.println(q);

        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();

        state.executeUpdate(q);

        conn.close();
        state.close();
        System.out.println("SHOULD HAVE BEEN WITHDRAWN");
    }
}
