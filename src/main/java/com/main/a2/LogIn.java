package com.main.a2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LogIn extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        DatabaseConnect.Connect();

        FXMLLoader fxmlLoader = new FXMLLoader(LogIn.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 670, 487);
        stage.setTitle("myTimetable - Sign In");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}