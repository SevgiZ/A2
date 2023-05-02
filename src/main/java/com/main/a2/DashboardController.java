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

public class DashboardController implements Initializable {
    private Stage stage;
    private String searchTerm;
    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private ObservableList<Course> searchResults = FXCollections.observableArrayList();

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
    private Label txtFirstName;

    @FXML
    private Label txtLastName;

    @FXML
    private Label txtStudentId;

    @FXML
    private Label txtUsername;

    public void LogInScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("LogIn.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            courses = LoadCoursesFromDB.Load(courses);
            Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
            String query = "SELECT * FROM courses";
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(query);

            while (rs.next()) {
                courseName.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
                capacity.setCellValueFactory(new PropertyValueFactory<Course, String>("capacity"));
                openclosed.setCellValueFactory(new PropertyValueFactory<Course, String>("slotsLeft"));
                year.setCellValueFactory(new PropertyValueFactory<Course, String>("year"));
                delivery.setCellValueFactory(new PropertyValueFactory<Course, String>("delivery"));
                day.setCellValueFactory(new PropertyValueFactory<Course, String>("day"));
                time.setCellValueFactory(new PropertyValueFactory<Course, String>("time"));
                duration.setCellValueFactory(new PropertyValueFactory<Course, Double>("duration"));
            }

            CurrentUserHolder holder = CurrentUserHolder.getCurrentUser();
            CurrentUser user = holder.getUser();

            txtFirstName.setText(user.getFirstName());
            txtLastName.setText(user.getLastName());
            txtUsername.setText(user.getUsername());
            txtStudentId.setText(user.getUserId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tableCourses.setItems(courses);
    }

    public void enrollButton() throws SQLException {
        Course c = tableCourses.getSelectionModel().getSelectedItem();
        System.out.println(c.getName());

        Connection conn = DriverManager.getConnection("jdbc:sqlite:src\\database\\mytimetable.db");
        String query = "INSERT INTO ";
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);


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

}
