package org.malisha.chatappjavafx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private Label validityShow;

    @FXML
    private Button goButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    ActionEvent actionEvent = new ActionEvent(usernameField, usernameField);
                    enterChat(actionEvent); // Create a synthetic ActionEvent and pass it
                } catch (IOException e) {
                    e.printStackTrace(); // Handle IOException if necessary
                }
            }
        });

        goButton.getStyleClass().add("custom-button");

    }

    @FXML
    private void enterChat(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();

        if (!username.isEmpty() && !username.contains(" ")) {
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
            stage.setMinWidth(1080);
            stage.setMinHeight(720);
            stage.setTitle("Chat App");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            // Show an error message if username is empty
            // You can implement this part based on your UI/UX design
            System.out.println("Please enter a valid username.");
            validityShow.setText("Please enter a valid username.");
        }
    }


}
