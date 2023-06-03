package com.main.controller;

import com.main.model.*;
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
    private ObservableList<CourseModel> courses = FXCollections.observableArrayList();
    private ObservableList<CourseModel> searchResults = FXCollections.observableArrayList();

    @FXML
    private TableColumn<CourseModel, String> capacity;

    @FXML
    private TableColumn<CourseModel, String> openclosed;

    @FXML
    private TableColumn<CourseModel, String> courseName;

    @FXML
    private TableColumn<CourseModel, String> day;

    @FXML
    private TableColumn<CourseModel, String> delivery;

    @FXML
    private TableColumn<CourseModel, Double> duration;

    @FXML
    private TextField fieldSearch;

    @FXML
    private TableView<CourseModel> tableCourses;

    @FXML
    private TableColumn<CourseModel, String> time;

    @FXML
    private TableColumn<CourseModel, String> year;

    @FXML
    private TableColumn<CourseModel, String> dates;

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

    private EnrollmentModel dashEnroll = new EnrollmentModel();
    private EnrollmentChecksModel timetableCheck = new EnrollmentChecksModel();
    private CourseSlotsModel courseSlots = new CourseSlotsModel();
    private WithdrawModel withdraw = new WithdrawModel();
    private EnrolledCoursesShowModel enrolledCoursesShow = new EnrolledCoursesShowModel();
    private CourseExportModel courseExport = new CourseExportModel();
    private LogInUserDetailsModel userDetails = new LogInUserDetailsModel();


    public void setDashboardDetails() {
        CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
        CurrentUserModel user = holder.getUser();

        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtUsername.setText(user.getUsername());
        txtStudentId.setText(user.getUserId());
    }

    //Is there a way to get this shit working from another class/object??
    public void updateTable() {
        try {
            courses.clear();
            courses = LoadCoursesFromDBModel.Load(courses);
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                courseName.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("name"));
                capacity.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("capacity"));
                openclosed.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("openclosed"));
                year.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("year"));
                delivery.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("delivery"));
                day.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("day"));
                time.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("time"));
                duration.setCellValueFactory(new PropertyValueFactory<CourseModel, Double>("duration"));
                dates.setCellValueFactory(new PropertyValueFactory<CourseModel, String>("dates"));

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
        updateTable();
        setDashboardDetails();
    }

    public void Enroll() throws SQLException {
        CourseModel c = tableCourses.getSelectionModel().getSelectedItem();
        if (!timetableCheck.IsEnrolled(c) && timetableCheck.CheckCourseAvailability(c) && !timetableCheck.IsClash(c)) {

            System.out.println(CurrentUserModel.getUserId());

            dashEnroll.enroll(timetableCheck.GetDbCourseId(c), c);

            labelMessage.setTextFill(WHITE);
            labelMessage.setText("Enrolled in: " + c.getName() + " @ " + c.getDay() + ", " + c.getTime());
        }
        else if (timetableCheck.IsEnrolled(c)) {
            labelMessage.setText("You are already enrolled in: " + c.getName() + " @ " + c.getDay() + ", " + c.getTime() + "!");
            labelMessage.setTextFill(RED);
        }

        else if (!timetableCheck.CheckCourseAvailability(c)) {
            labelMessage.setText("Course is currently full!");
            labelMessage.setTextFill(RED);
        }

        else if (timetableCheck.IsClash(c)) {
            labelMessage.setText("Timetable clash!");
            labelMessage.setTextFill(RED);
        }

        else {
            labelMessage.setText("Something went wrong!");
            labelMessage.setTextFill(RED);
        }
    }

    //WITHDRAWING WORKS FINE
    public void Withdraw() throws SQLException {
        CourseModel c = tableCourses.getSelectionModel().getSelectedItem();
        if (timetableCheck.IsEnrolled(c)) {
            withdraw.withdraw(c);
            courseSlots.AddCourseSlot(c);
            labelMessage.setTextFill(WHITE);
            labelMessage.setText("Withdrew from " + c.getName());
        }

        else {
            labelMessage.setText("You are not enrolled in: " + c.getName());
            labelMessage.setTextFill(RED);
        }


    }

    public ObservableList<CourseModel> ShowEnrolledCourses() throws SQLException {
        searchResults.clear();
        searchResults = enrolledCoursesShow.show(searchResults);
        tableCourses.setItems(searchResults);
        return searchResults;

    }

    public void SignOut(ActionEvent event) throws IOException, SQLException {
        CurrentUserModel.ResetUser();
        userDetails.removeCurrentUser();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogInView.fxml"));
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
            if (courses.get(i).getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                searchResults.add(courses.get(i));
            }
        }
        tableCourses.setItems(searchResults);
    }

    public void ShowAllCourses() {
        searchResults.clear();
        tableCourses.setItems(courses);
    }
    public void Export() throws SQLException, IOException {
        System.out.println("Exporting");
        courseExport.export(txtFirstName.getText(), txtLastName.getText(), txtStudentId.getText());
        labelMessage.setTextFill(WHITE);
        labelMessage.setText("Enrolled courses exported as .txt!");
    }

    public void ChangeAccountDetailsScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AccountDetailsView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Course Enrollment!");
        stage.setScene(scene);
        stage.show();
    }

    public void ChangeTimetableScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TimetableView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 936, 976);
        stage.setTitle("myTimetable - Course Enrollment!");
        stage.setScene(scene);
        stage.show();
    }

}
