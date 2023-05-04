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

import java.io.BufferedWriter;
import java.io.FileWriter;
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


    public void setDashboardDetails() {
        CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
        CurrentUser user = holder.getUser();

        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtUsername.setText(user.getUsername());
        txtStudentId.setText(user.getUserId());
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
            setDashboardDetails();
    }

    public void Enroll() throws SQLException {
        Course c = tableCourses.getSelectionModel().getSelectedItem();
        if (!IsEnrolled() && CheckCourseAvailability(c) && !IsClash(c)) {
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
        else if (IsEnrolled()) {
            labelMessage.setText("You are already enrolled in: " + c.getName() + " @ " + c.getDay() + ", " + c.getTime() + "!");
            labelMessage.setTextFill(RED);
        }

        else if (!CheckCourseAvailability(c)) {
            labelMessage.setText("Course is currently full!");
            labelMessage.setTextFill(RED);
        }

        else if (IsClash(c)) {
            labelMessage.setText("Timetable clash!");
            labelMessage.setTextFill(RED);
        }

        else {
            labelMessage.setText("Something went wrong!");
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

            AddCourseSlot(c);

            labelMessage.setTextFill(WHITE);
            labelMessage.setText("Withdrew from " + c.getName());
        }

        else {
            labelMessage.setText("You are not enrolled in: " + c.getName());
            labelMessage.setTextFill(RED);
        }


    }

    public ObservableList<Course> ShowEnrolledCourses() throws SQLException {
        searchResults.clear();
        //Gets all the details of the courses that the specific student is enrolled in.
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

        return searchResults;

    }

    public void SignOut(ActionEvent event) throws IOException {
        CurrentUser.ResetUser();

        FXMLLoader fxmlLoader = new FXMLLoader(LogInView.class.getResource("LogIn.fxml"));
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

    public boolean IsEnrolled() throws SQLException {

        Course c = tableCourses.getSelectionModel().getSelectedItem();
        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        String q = "SELECT COUNT(*) AS total FROM student_enrolled_courses WHERE course_id = " + GetDbCourseId() + " AND " +
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

    public void CourseCloseCheck() throws SQLException {
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
            state.executeUpdate(q);
            UpdateTable();
            conn.close();
            state.close();
            CourseCloseCheck();
        }
    }

    public void CourseOpenCheck() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        Statement state = conn.createStatement();
        String q = "UPDATE courses SET open_closed = 'OPEN' WHERE capacity > 0";
        state.executeUpdate(q);
        UpdateTable();
        conn.close();
        state.close();
    }

    public void AddCourseSlot(Course c) throws SQLException {
        if (!c.getCapacity().equals("N/A")) {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            Statement state = conn.createStatement();
            String q = "UPDATE courses SET capacity = capacity + 1 " +
                    "WHERE course_name LIKE '%" + c.getName() + "%'";

            state.executeUpdate(q);
            UpdateTable();

            conn.close();
            state.close();

            CourseOpenCheck();
        }
    }

    public boolean CheckCourseAvailability(Course c) throws SQLException {
        if (!c.getCapacity().equals("N/A")) {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
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

    public void Export() throws SQLException, IOException {
        System.out.println("Exporting");

        String q = "SELECT * FROM (student_enrolled_courses INNER JOIN courses " +
                "ON student_enrolled_courses.course_id = courses.course_id) " +
                "WHERE student_id LIKE '%" + CurrentUser.getUserId() + "%'";
        System.out.println(q);
        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(q);

        BufferedWriter bw = new BufferedWriter(new FileWriter("src\\database\\enrollment.txt"));
        bw.write(txtFirstName.getText() + " " + txtLastName.getText() + ", " + txtStudentId.getText() + "\n\n");

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
        labelMessage.setTextFill(WHITE);
        labelMessage.setText("Enrolled courses exported as .txt!");
    }

    public boolean IsClash(Course c) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        Statement state = conn.createStatement();
        String q = "SELECT * FROM (student_enrolled_courses INNER JOIN courses " +
                "ON student_enrolled_courses.course_id = courses.course_id) " +
                "WHERE student_id LIKE '%" + CurrentUser.getUserId() + "%'";

        ResultSet rs = state.executeQuery(q);

        double enrollingStartTime = StringTimeToDouble(c.getTime());
        double enrollingFinishTime = enrollingStartTime+ DurationToRealTime(c.getDuration());
        System.out.println("enrollingStartTime " + enrollingStartTime + "\nEnrolling finish time: " + enrollingFinishTime);

        while (rs.next()) {
            if (rs.getString("day_of_lecture").equals(c.getDay())) {
                double clashStartTime = DurationToRealTime(StringTimeToDouble(rs.getString("time_of_lecture")));
                System.out.println("Clash start time: " + clashStartTime);
                //clashFinishTime = clash start time (in actual hours) + its duration

                double clashFinishTime = StringTimeToDouble(rs.getString("time_of_lecture")) +
                        DurationToRealTime(rs.getDouble("duration_of_lecture"));
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

    public Double StringTimeToDouble(String inputTime) {
        //Use doubles becuase we are working with 'duration' which needs to be a double

        String[] timeSplit = inputTime.split(":");
        double dHour = Double.parseDouble(timeSplit[0]);
        double dMinute = Double.parseDouble(timeSplit[1]) / 100;

        Double realTime = dHour + dMinute;

        return realTime;
    }

    //To convert duration only. Start time is already in real time.
    public double DurationToRealTime(double inputTime) {
        double leftOver = inputTime % 1;
        double hour = inputTime - leftOver;
        double realLeftOver = (leftOver * (60/1) / 100);
        double realTime = hour + realLeftOver;
        return realTime;
    }

    public void ChangeAccountDetailsScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogInView.class.getResource("AccountDetailsView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Course Enrollment!");
        stage.setScene(scene);
        stage.show();
    }

}
