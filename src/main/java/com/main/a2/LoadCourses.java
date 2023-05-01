package com.main.a2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadCourses {

    ObservableList<Course> courses = FXCollections.observableArrayList();

    public static ObservableList<Course> Load(ObservableList<Course> courses) {
        String file = "src\\database\\course.csv";
        String line = "";
        int iteration = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {    //While the read line actually has content in it
                if (iteration == 0) {   //If reading the first line, don't split/load it. Responsible for skipping the header content.
                    iteration += 1;
                }
                else {
                    String[] row = line.split(",");
                    courses.add(new Course(row[0], row[1], row[2], row[3], row[4], row[5], Double.parseDouble(row[6])));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return courses;

    }
}
