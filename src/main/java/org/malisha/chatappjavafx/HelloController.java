package org.malisha.chatappjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.malisha.server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void initialize() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}