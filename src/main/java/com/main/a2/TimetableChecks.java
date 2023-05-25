package com.main.a2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;

public class TimetableChecks {
    EnrolledCoursesShow enrolledCourses = new EnrolledCoursesShow();
    private ObservableList<Course> enrolled = FXCollections.observableArrayList();
    private ArrayList<String> coursesStrip = new ArrayList<>();


    public ArrayList<String> getCoursesStrip() throws SQLException {
        enrolled = enrolledCourses.show(enrolled);

        for (Course c : enrolled) {
            String name = c.getName().replaceAll("\\s+","");
            coursesStrip.add(name);
            System.out.println(name);
        }
        return coursesStrip;
    }
}
