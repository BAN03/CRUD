module com.ban03 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;

    opens com.ban03 to javafx.fxml;

    exports com.ban03;
}
