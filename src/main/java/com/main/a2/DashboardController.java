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
import javafx.scene.layout.Pane;
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

    Enrollment dashEnroll = new Enrollment();
    TimetableChecks timetableCheck = new TimetableChecks();
    CourseSlots courseSlots = new CourseSlots();
    Withdraw withdraw = new Withdraw();
    AllCourses allCourses = new AllCourses();
    CourseExport courseExport = new CourseExport();
    LogInUserDetails userDetails = new LogInUserDetails();


    public void setDashboardDetails() {
        CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
        CurrentUser user = holder.getUser();

        txtFirstName.setText(user.getFirstName());
        txtLastName.setText(user.getLastName());
        txtUsername.setText(user.getUsername());
        txtStudentId.setText(user.getUserId());
    }

    //Is there a way to get this shit working from another class/object??
    public void updateTable() {
        try {
            courses.clear();
            courses = LoadCoursesFromDB.Load(courses);
            Connection conn = DatabaseConnection.getConnection();
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
        updateTable();
        setDashboardDetails();
    }

    public void Enroll() throws SQLException {
        Course c = tableCourses.getSelectionModel().getSelectedItem();
        if (!timetableCheck.IsEnrolled(c) && timetableCheck.CheckCourseAvailability(c) && !timetableCheck.IsClash(c)) {

            System.out.println(CurrentUser.getUserId());

            dashEnroll.enroll(timetableCheck.GetDbCourseId(c), c);

            //dashTable.updateTable();

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
        Course c = tableCourses.getSelectionModel().getSelectedItem();
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

    public ObservableList<Course> ShowEnrolledCourses() throws SQLException {
        searchResults.clear();
        searchResults = allCourses.show(searchResults);
        tableCourses.setItems(searchResults);
        return searchResults;

    }

    public void SignOut(ActionEvent event) throws IOException, SQLException {
        CurrentUser.ResetUser();
        userDetails.removeCurrentUser();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn.fxml"));
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

}
