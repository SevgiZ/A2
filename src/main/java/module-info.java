module com.main.a2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.main.controller to javafx.fxml;
    exports com.main.controller;
    exports com.main.model;
    opens com.main.model to javafx.fxml;
}