package org.malisha.chatappjavafx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.malisha.chatappjavafx.client.Client;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivateChatController implements Initializable {
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private VBox messagesBox;
    @FXML
    private TextField typedMessage;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client(new Socket("localhost", 1234));
            client.listenForMessage(this::addMessageToBox);
            System.out.println("Connected to the server.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        messagesBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mainScrollPane.setVvalue((Double) t1);
            }
        });
    }

    @FXML
    protected void onHelloButtonClick() {

    }

    private void addMessageToBox(String message) {
        Platform.runLater(() -> {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);

            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);
            textFlow.getStyleClass().addAll("bubble", "bubble-received");

            hbox.getChildren().add(textFlow);
            messagesBox.getChildren().add(hbox);

            mainScrollPane.setVvalue(1.0);
        });
    }
}
