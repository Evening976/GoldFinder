module com.example.goldfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens com.example.goldfinder.client to javafx.fxml;
    exports com.example.goldfinder.client;
    exports com.example.goldfinder.client.commands;
    opens com.example.goldfinder.client.commands to javafx.fxml;
    exports com.example.utils;
    opens com.example.utils to javafx.fxml;
}