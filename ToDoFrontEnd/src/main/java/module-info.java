module org.example.todofrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.dlsc.formsfx;

    opens org.example.todofrontend to javafx.fxml;
    exports org.example.todofrontend;
}