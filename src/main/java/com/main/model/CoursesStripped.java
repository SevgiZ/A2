package com.main.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;

public class CoursesStripped {
    private EnrolledCoursesShowModel enrolledCourses = new EnrolledCoursesShowModel();
    private ObservableList<CourseModel> enrolled = FXCollections.observableArrayList();
    private ArrayList<String> coursesStrip = new ArrayList<>();


    public ArrayList<String> getStripped() throws SQLException {
        enrolled = enrolledCourses.show(enrolled);

        for (CourseModel c : enrolled) {
            String name = c.getName().replaceAll("\\s+","");
            coursesStrip.add(name);
            System.out.println(name);
        }
        return coursesStrip;
    }
}
