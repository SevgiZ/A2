module com.main.a2 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.main.a2 to javafx.fxml;
    exports com.main.a2;
}