package com.main.a2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {

        LogInUserDetails logUserDetails = new LogInUserDetails();

        //Try seeing if a user remained logged in after last sessions and automatically log them back in
        try {
            System.out.println("Checking username");
            String currentUsername = logUserDetails.checkCurrentUser();
            System.out.println(currentUsername);

            if (currentUsername != null) {
                System.out.println("Current username not null, logging in...");
                CurrentUser user = logUserDetails.getUserDetails(currentUsername);
                CurrentUser.SetUserInstance(user);

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Dashboard.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1200, 550);
                stage.setTitle("myTimetable - Dashboard");
                Image image = new Image("https://i.imgur.com/1vB2hyF.png");
                stage.getIcons().add(image);
                stage.setScene(scene);
                stage.show();
            }

        //If user signed out at last session, go to normal log in screen.
        } catch (Exception e) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 670, 487);
            stage.setTitle("myTimetable - Sign In");
            Image image = new Image("https://i.imgur.com/1vB2hyF.png");
            stage.getIcons().add(image);
            stage.setScene(scene);
            stage.show();
        }

        //If neither works, display error message
        finally {
            System.out.println("Something went wrong!");
        }
    }



    public static void main(String[] args) {
        launch();
    }
}