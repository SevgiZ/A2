package com.main.a2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreation {
    public void createDatabase() {
        try {
            //Upon creating connection, if the database in the file not exists, then it'll be created automatically.
            Connection conn = DatabaseConnection.getConnection();
            Statement state = conn.createStatement();

            System.out.println("Database was made!");

            conn.close();
            state.close();
        } catch (Exception e) {
            System.out.println("Something went wrong and couldn't create the database!");
        }
    }

    public void createTables() throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement state = conn.createStatement();

            String createStudents = "CREATE TABLE IF NOT EXISTS students (\n" +
                    "username TEXT NOT NULL UNIQUE,\n" +
                    "student_id TEXT PRIMARY KEY UNIQUE,\n" +
                    "first_name TEXT NOT NULL,\n" +
                    "last_name TEXT NOT NULL,\n" +
                    "password TEXT NOT NULL\n" +
                    ");";

            String createCourses = "CREATE TABLE IF NOT EXISTS courses (\n" +
                    "course_id INTEGER PRIMARY KEY,\n" +
                    "course_name TEXT NOT NULL,\n" +
                    "capacity TEXT NOT NULL,\n" +
                    "open_closed TEXT NOT NULL,\n" +
                    "year TEXT NOT NULL,\n" +
                    "delivery_mode TEXT NOT NULL,\n" +
                    "day_of_lecture TEXT NOT NULL,\n" +
                    "time_of_lecture TEXT NOT NULL,\n" +
                    "duration_of_lecture DOUBLE NOT NULL,\n" +
                    "dates TEXT NOT NULL,\n" +
                    "slots_left INT NOT NULL" +
                    ");";

            String createEnrolledCourses = "CREATE TABLE IF NOT EXISTS student_enrolled_courses (\n" +
                    "enrolled_course_num INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "student_id TEXT NOT NULL,\n" +
                    "course_id INTEGER NOT NULL,\n" +
                    "FOREIGN KEY (student_id) REFERENCES students (student_id),\n" +
                    "FOREIGN KEY (course_id) REFERENCES courses (id)\n" +
                    ");";

            String createCurrentUser = "CREATE TABLE current_user (\n" +
                    "username TEXT NOT NULL,\n" +
                    "signed_in INTEGER NOT NULL,\n" +
                    "FOREIGN KEY (username) REFERENCES students (username)\n" +
                    ");";

            state.executeUpdate(createStudents);
            state.executeUpdate(createCourses);
            state.executeUpdate(createEnrolledCourses);
            state.executeUpdate(createCurrentUser);
            System.out.println("Tables created!");
        } catch (Exception e) {
            System.out.println("Couldn't create new tables! " + e);
        }
    }

    public void populate() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader("src\\database\\dbcourse.csv"));
        String line;
        reader.readLine();

         try {
             String q = "INSERT INTO courses (course_id, course_name, capacity, open_closed, year, delivery_mode, day_of_lecture, time_of_lecture," +
                     "duration_of_lecture, dates, slots_left) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

             Connection conn = DatabaseConnection.getConnection();
             PreparedStatement state = conn.prepareStatement(q);

             System.out.println("Loading batch!");
             while ((line = reader.readLine()) != null) {
                 String[] data = line.split(",");
                 System.out.println(line.split(","));

                 state.setString(1, data[0]);
                 state.setString(2, data[1]);
                 state.setString(3, data[2]);
                 state.setString(4, data[3]);
                 state.setString(5, data[4]);
                 state.setString(6, data[5]);
                 state.setString(7, data[6]);
                 state.setString(8, data[7]);
                 state.setString(9, data[8]);
                 state.setString(10, data[9]);
                 state.setString(11, data[10]);

                 state.addBatch();
                 state.executeBatch();
             }

             reader.close();
             conn.close();
             state.close();
             System.out.println("Tables populated!");
         } catch (Exception e) {
             System.out.println("Something went wrong when trying to populate tables! " + e);
         }
    }
}
