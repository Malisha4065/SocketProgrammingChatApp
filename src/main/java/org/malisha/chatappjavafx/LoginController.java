package org.malisha.chatappjavafx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private void enterChat(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();

        if (!username.isEmpty()) {
            // Load mainview.fxml
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//            // Pass the username to the controller of mainview.fxml
            Parent root = loader.load();
            HelloController helloController = loader.getController();
            helloController.initData(username);

            // Set the main BorderPane to display the chatPane
            // Close the login window
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root, 640, 480);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
            stage.setMinWidth(640);
            stage.setMinHeight(480);
            stage.setTitle("Chat App");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            // Show an error message if username is empty
            // You can implement this part based on your UI/UX design
            System.out.println("Please enter a valid username.");
        }
    }
}
