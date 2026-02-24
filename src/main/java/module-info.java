module com.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example to javafx.fxml, javafx.controls;

    exports com.example;

}
