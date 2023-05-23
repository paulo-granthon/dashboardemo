module org.openjfx {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive java.sql;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}
