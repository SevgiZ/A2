package com.main.a2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;

public class DashboardController implements Initializable {
    private Stage stage;
    private String searchTerm;
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private ObservableList<Course> searchResults = FXCollections.observableArrayList();
    private int course_id;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnShowAll;

    @FXML
    private TableColumn<Course, String> capacity;

    @FXML
    private TableColumn<Course, String> openclosed;

    @FXML
    private TableColumn<Course, String> courseName;

    @FXML
    private TableColumn<Course, String> day;

    @FXML
    private TableColumn<Course, String> delivery;

    @FXML
    private TableColumn<Course, Double> duration;

    @FXML
    private TextField fieldSearch;

    @FXML
    private TableView<Course> tableCourses;

    @FXML
    private TableColumn<Course, String> time;

    @FXML
    private TableColumn<Course, String> year;

    @FXML
    private TableColumn<Course, String> dates;

    @FXML
    private Label txtFirstName;

    @FXML
    private Label txtLastName;

    @FXML
    private Label txtStudentId;

    @FXML
    private Label txtUsername;

    @FXML
    private Label labelMessage;

    private int resultSize;

    public void LogInScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("LogIn.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

    public void UpdateTable() {
        try {
            courses.clear();
            courses = LoadCoursesFromDB.Load(courses);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                courseName.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
                capacity.setCellValueFactory(new PropertyValueFactory<Course, String>("capacity"));
                openclosed.setCellValueFactory(new PropertyValueFactory<Course, String>("openclosed"));
                year.setCellValueFactory(new PropertyValueFactory<Course, String>("year"));
                delivery.setCellValueFactory(new PropertyValueFactory<Course, String>("delivery"));
                day.setCellValueFactory(new PropertyValueFactory<Course, String>("day"));
                time.setCellValueFactory(new PropertyValueFactory<Course, String>("time"));
                duration.setCellValueFactory(new PropertyValueFactory<Course, Double>("duration"));
                dates.setCellValueFactory(new PropertyValueFactory<Course, String>("dates"));

                conn.close();
                state.close();
                rs.close();

                tableCourses.setItems(courses);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            UpdateTable();

            CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
            CurrentUser user = holder.getUser();

            txtFirstName.setText(user.getFirstName());
            txtLastName.setText(user.getLastName());
            txtUsername.setText(user.getUsername());
            txtStudentId.setText(user.getUserId());
    }

    public void Enroll() throws SQLException {
        Course c = tableCourses.getSelectionModel().getSelectedItem();
        if (!IsEnrolled()) {
            System.out.println(CurrentUser.getUserId());

            String q = "INSERT INTO student_enrolled_courses (student_id, course_id) " +
                    "VALUES ('" + CurrentUser.getUserId() + "', " + GetDbCourseId() + ");";

            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            Statement state = conn.createStatement();

            state.executeUpdate(q);

            RemoveCourseSlot(c);

            conn.close();
            state.close();


            labelMessage.setTextFill(WHITE);
            labelMessage.setText("Enrolled in: " + c.getName() + " @ " + c.getDay() + ", " + c.getTime());
        }
        else {
            labelMessage.setText("You are already enrolled in: " + c.getName() + " @ " + c.getDay() + ", " + c.getTime() + "!");
            labelMessage.setTextFill(RED);
        }

    }

    public void Withdraw() throws SQLException {
        Course c = tableCourses.getSelectionModel().getSelectedItem();
        if (IsEnrolled()) {
            String q = "DELETE FROM student_enrolled_courses WHERE course_id = " + GetDbCourseId() + " AND student_id LIKE " +
                    "'%" + CurrentUser.getUserId() + "%'";

            System.out.println(q);

            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            Statement state = conn.createStatement();

            state.executeUpdate(q);

            conn.close();
            state.close();
            System.out.println("SHOULD HAVE BEEN WITHDRAWN");

            labelMessage.setTextFill(WHITE);
            labelMessage.setText("Withdrew from " + c.getName());
        }

        else {
            labelMessage.setText("You are not enrolled in: " + c.getName());
            labelMessage.setTextFill(RED);
        }


    }

    public void ShowEnrolledCourses() throws SQLException {
        searchResults.clear();
        String q = "SELECT * FROM (student_enrolled_courses INNER JOIN courses " +
                "ON student_enrolled_courses.course_id = courses.course_id) " +
                "WHERE student_id LIKE '%" + CurrentUser.getUserId() + "%'";
        System.out.println(q);

        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);

        while (rs.next()) {
            searchResults.add(
                    new Course(rs.getString("course_name"), rs.getString("capacity"), rs.getString("open_closed"), rs.getString("year"),
                            rs.getString("delivery_mode"), rs.getString("day_of_lecture"), rs.getString("time_of_lecture"),
                            rs.getDouble("duration_of_lecture"), rs.getString("dates"))
            );
        }

        conn.close();
        state.close();
        tableCourses.setItems(searchResults);

    }

    public void SignOut(ActionEvent event) throws IOException {
        //Erase current user details
        //CurrentUser u = new CurrentUser();
        //CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
        //holder.setCurrentUser(u);

        CurrentUser.ResetUser();

        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("LogIn.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

    public void SearchCourses() {
        searchResults.clear();
        searchTerm = fieldSearch.getText();

        for (int i=0;i< courses.size();i++) {
            if (courses.get(i).getName().contains(searchTerm)) {
                searchResults.add(courses.get(i));
            }
        }
        tableCourses.setItems(searchResults);
    }

    public void ShowAllCourses() {
        searchResults.clear();
        tableCourses.setItems(courses);
    }

    public int GetDbCourseId() throws SQLException {
        Course c = tableCourses.getSelectionModel().getSelectedItem();
        System.out.println(c.getName());

        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");

        String q =  "SELECT course_id FROM courses WHERE course_name LIKE '%" + c.getName() +  "%'";
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

    public boolean IsEnrolled() throws SQLException {

        Course c = tableCourses.getSelectionModel().getSelectedItem();
        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        String q = "SELECT COUNT(*) AS total FROM student_enrolled_courses WHERE course_id = " + GetDbCourseId() + " AND " +
                "student_id LIKE '%" + CurrentUser.getUserId() + "%'";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);
        //System.out.println(q);

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

    //Is there a better way to do literally everything about this?
    public void CloseCourseAvailability() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        Statement state = conn.createStatement();

        String q = "UPDATE courses SET open_closed = 'CLOSED' WHERE capacity = 0";
        state.executeUpdate(q);

        UpdateTable();

        conn.close();
        state.close();
    }

    public void RemoveCourseSlot(Course c) throws SQLException {
        if (!c.getCapacity().equals("N/A")) {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            Statement state = conn.createStatement();
            String q = "UPDATE courses SET capacity = capacity - 1 " +
                    "WHERE course_name LIKE '%" + c.getName() + "%'";

            System.out.println("UPDATE COURSE SLOTS: " + q);

            state.executeUpdate(q);
            UpdateTable();

            conn.close();
            state.close();

            CloseCourseAvailability();
        }
    }



}
