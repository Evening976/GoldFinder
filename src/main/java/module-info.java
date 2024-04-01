module com.example.goldfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;

    opens com.example.goldfinder.client to javafx.fxml;
    exports com.example.goldfinder.client;
    exports com.example.goldfinder.client.commands;
    opens com.example.goldfinder.client.commands to javafx.fxml;
    exports com.example.utils;
    opens com.example.utils to javafx.fxml;
    exports com.example.utils.players;
    opens com.example.utils.players to javafx.fxml;
    exports com.example.utils.games;
    opens com.example.utils.games to javafx.fxml;
    exports com.example.goldfinder.server;
    exports com.example.goldfinder.server.commands.dispatcherserver;
    opens com.example.goldfinder.server.commands.dispatcherserver to javafx.fxml;
    exports com.example.goldfinder.server.commands.gameserver;
    opens com.example.goldfinder.server.commands.gameserver to javafx.fxml;
}