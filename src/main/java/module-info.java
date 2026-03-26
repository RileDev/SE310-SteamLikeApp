module org.riledev.se310steamlikeapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;

    opens org.riledev.se310steamlikeapp to javafx.fxml;
    exports org.riledev.se310steamlikeapp;

    opens org.riledev.se310steamlikeapp.controllers to javafx.fxml;
    exports org.riledev.se310steamlikeapp.controllers;
    exports org.riledev.se310steamlikeapp.services.launch;
    exports org.riledev.se310steamlikeapp.services.session;
    exports org.riledev.se310steamlikeapp.util;

}