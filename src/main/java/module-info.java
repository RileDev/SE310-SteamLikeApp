module org.riledev.se310steamlikeapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.riledev.se310steamlikeapp to javafx.fxml;
    exports org.riledev.se310steamlikeapp;
}