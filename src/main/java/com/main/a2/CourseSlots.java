package com.main.a2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CourseSlots {
    EnrollmentChecks enrollmentChecks = new EnrollmentChecks();

    public void AddCourseSlot(Course c) throws SQLException {
        if (!c.getCapacity().equals("N/A")) {
            Connection conn = DatabaseConnection.getConnection();
            Statement state = conn.createStatement();
            String q = "UPDATE courses SET slots_left = slots_left + 1 " +
                    "WHERE course_name LIKE '%" + c.getName() + "%'";

            state.executeUpdate(q);
            //dashTable.updateTable();

            conn.close();
            state.close();

            enrollmentChecks.CourseOpenCheck();
        }
    }

    public void RemoveCourseSlot(Course c) throws SQLException {
        if (!c.getCapacity().equals("N/A")) {
            Connection conn = DatabaseConnection.getConnection();
            Statement state = conn.createStatement();
            String q = "UPDATE courses SET slots_left = slots_left - 1 " +
                    "WHERE course_name LIKE '%" + c.getName() + "%'";
            state.executeUpdate(q);
            //dashTable.updateTable();
            conn.close();
            state.close();
            enrollmentChecks.CourseCloseCheck();
        }
    }
}
