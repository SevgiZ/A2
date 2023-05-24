package com.main.a2;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.*;

public class TimetableChecks {
    @FXML
    private TableView<Course> tableCourses;
    DashboardTable dashTable = new DashboardTable();
    TimeConversion timeConvert = new TimeConversion();
    private int resultSize;
    private int course_id;

    public void CourseCloseCheck() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        String q = "UPDATE courses SET open_closed = 'CLOSED' WHERE capacity = 0";
        state.executeUpdate(q);
        //dashTable.updateTable();
        conn.close();
        state.close();
    }

    public void CourseOpenCheck() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        String q = "UPDATE courses SET open_closed = 'OPEN' WHERE capacity > 0";
        state.executeUpdate(q);
        //dashTable.updateTable();
        conn.close();
        state.close();
    }

    public boolean CheckCourseAvailability(Course c) throws SQLException {
        if (!c.getCapacity().equals("N/A")) {
            Connection conn = DatabaseConnection.getConnection();
            Statement state = conn.createStatement();

            String q = "SELECT * FROM courses WHERE course_name LIKE '%" + c.getName() + "%'";
            ResultSet rs = state.executeQuery(q);

            if (rs.getString("open_closed").equals("CLOSED")) {
                System.out.println("COURSE IS CLOSED");
                conn.close();
                state.close();
                rs.close();
                return false;
            }

            else {
                conn.close();
                state.close();
                rs.close();
                System.out.println("COURSE IS OPEN");
                return true;
            }
        }
        System.out.println("COURSE IS OPEN");
        return true;
    }

    public boolean IsClash(Course c) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        Statement state = conn.createStatement();
        String q = "SELECT * FROM (student_enrolled_courses INNER JOIN courses " +
                "ON student_enrolled_courses.course_id = courses.course_id) " +
                "WHERE student_id LIKE '%" + CurrentUser.getUserId() + "%'";

        ResultSet rs = state.executeQuery(q);

        double enrollingStartTime = timeConvert.StringTimeToDouble(c.getTime());
        double enrollingFinishTime = enrollingStartTime+ timeConvert.DurationToRealTime(c.getDuration());
        System.out.println("enrollingStartTime " + enrollingStartTime + "\nEnrolling finish time: " + enrollingFinishTime);

        while (rs.next()) {
            if (rs.getString("day_of_lecture").equals(c.getDay())) {
                double clashStartTime = timeConvert.DurationToRealTime(timeConvert.StringTimeToDouble(rs.getString("time_of_lecture")));
                System.out.println("Clash start time: " + clashStartTime);
                //clashFinishTime = clash start time (in actual hours) + its duration

                double clashFinishTime = timeConvert.StringTimeToDouble(rs.getString("time_of_lecture")) +
                        timeConvert.DurationToRealTime(rs.getDouble("duration_of_lecture"));
                System.out.println("Clash finish time: " + clashFinishTime);

                if (enrollingStartTime > clashStartTime && enrollingStartTime < clashFinishTime) {
                    System.out.println("SHOULD BE A CLASH!!!");
                    conn.close();
                    state.close();
                    rs.close();
                    return true;
                }
                //If you are trying to enroll in a course where a clashing time is earlier than the enrolling course time
                if (enrollingStartTime < clashStartTime) {
                    if (clashStartTime > enrollingStartTime && clashStartTime < enrollingFinishTime) {
                        System.out.println("SHOULD BE A CLASH 222!!!");
                        conn.close();
                        state.close();
                        rs.close();
                        return true;
                    }
                }
            }
        }
        System.out.println("SHOULD BE GOOD TO GO");
        conn.close();
        state.close();
        rs.close();
        return false;
    }
    public boolean IsEnrolled(Course c) throws SQLException {

        //Course c = tableCourses.getSelectionModel().getSelectedItem();
        Connection conn = DatabaseConnection.getConnection();
        String q = "SELECT COUNT(*) AS total FROM student_enrolled_courses WHERE course_id = " + GetDbCourseId(c) + " AND " +
                "student_id LIKE '%" + CurrentUser.getUserId() + "%'";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);
        System.out.println(q);

        resultSize = Integer.parseInt(rs.getString("total"));
        System.out.println("result size:  " + resultSize);

        if (resultSize > 0) {
            System.out.println("Enrolled, returning true");
            conn.close();
            state.close();
            rs.close();
            return true;
        }
        conn.close();
        state.close();
        rs.close();
        return false;
    }

    public int GetDbCourseId(Course c) throws SQLException {
        //Course c = tableCourses.getSelectionModel().getSelectedItem();
        System.out.println(c.getName());

        Connection conn = DatabaseConnection.getConnection();

        String q =  "SELECT course_id FROM courses WHERE course_name = '" + c.getName() +  "'";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);


        while (rs.next()) {
            course_id = Integer.parseInt(rs.getString("course_id"));
            System.out.println(course_id);
        }

        conn.close();
        state.close();

        return course_id;
    }
}
