module com.example.goldfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens com.example.goldfinder to javafx.fxml;
    exports com.example.goldfinder;

    opens com.example.goldfinder.client to javafx.fxml;
    exports com.example.goldfinder.client;
}