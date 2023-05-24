package com.main.a2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class CourseExport {
    //Needing to pass arguments for this method is fucking lazy.
    //Do a better implementation later?
    public void export(String txtFirstName, String txtLastName, String txtStudentId) throws SQLException, IOException {
        String q = "SELECT * FROM (student_enrolled_courses INNER JOIN courses " +
                "ON student_enrolled_courses.course_id = courses.course_id) " +
                "WHERE student_id LIKE '%" + CurrentUser.getUserId() + "%'";
        System.out.println(q);
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);

        BufferedWriter bw = new BufferedWriter(new FileWriter("src\\database\\enrollment.txt"));

        bw.write(txtFirstName + " " + txtLastName + ", " + txtStudentId + "\n\n");

        while (rs.next()) {
            bw.write("Course: " + rs.getString("course_name") + "\n" +
                    "Year: " + rs.getString("year") + "\n" +
                    "Delivery: " + rs.getString("delivery_mode") + "\n" +
                    "Day: " + rs.getString("day_of_lecture") + "\n" +
                    "Time: " + rs.getString("time_of_lecture") + "\n" +
                    "Duration (hours): " + rs.getDouble("duration_of_lecture") + "\n" +
                    "Dates: " + rs.getString("dates") + "\n\n");
        }
        bw.close();
        conn.close();
    }
}
