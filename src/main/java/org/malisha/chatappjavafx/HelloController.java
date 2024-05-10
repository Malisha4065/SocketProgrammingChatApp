package org.malisha.chatappjavafx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.malisha.chatappjavafx.client.Client;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private VBox messagesBox;
    @FXML
    private TextField typedMessage;
    @FXML
    private Label welcomeText;
    @FXML
    private Button groupChatButton;

    private Client client;
    private String myUsername = "";

    public void initData(String username) {
        welcomeText.setText("Welcome, " + username + "!");
        client.sendMessage(username);
        myUsername = username;
    }

    @FXML
    protected void onHelloButtonClick() {
        String messageToSend = typedMessage.getText();
        if (!messageToSend.isEmpty()) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.getStyleClass().addAll("bubble", "bubble-sent");

            hbox.getChildren().add(textFlow);
            messagesBox.getChildren().add(hbox);

            if (!privateChat) {
                client.sendMessage(messageToSend);

                chatHistories.get("group").add("(s)" + messageToSend);

            } else {
                client.sendMessage("@" + receiver + " " + messageToSend);

                if (!chatHistories.containsKey(receiver)) {
                    chatHistories.put(receiver, new ArrayList<>());
                }
                chatHistories.get(receiver).add("(s)" + messageToSend);
            }
            typedMessage.clear();
        }
    }

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

        chatHistories.put("group", new ArrayList<>());
        groupChatButton.setVisible(false);
    }

    private void addMessageToBox(String message) {
        Platform.runLater(() -> {
            if (message.startsWith("Active Users:")) {
                String users = message.substring("Active Users:".length()).trim();
                if (!users.isEmpty()) {
                    if (users.endsWith(",")) {
                        users = users.substring(0, users.length() - 1); // remove the trailing comma
                    }
                    List<String> userList = Arrays.asList(users.split(", "));
                    populateUserList(userList);
                }
            } else if (message.startsWith("(Private) " + receiver + ": ") && privateChat) {
                //HBox hbox = new HBox();
                //hbox.setAlignment(Pos.CENTER_LEFT);
                String privateHandle = "(Private) " + receiver + ": ";
                String messageWithReceiverStripped = message.substring(privateHandle.length()).trim();
                //Text text = new Text(messageWithReceiverStripped);

                if (!chatHistories.containsKey(receiver)) {
                    chatHistories.put(receiver, new ArrayList<>());
                }

                chatHistories.get(receiver).add("(r)" + messageWithReceiverStripped);
                populateChat(receiver);

                //TextFlow textFlow = new TextFlow(text);
                //textFlow.getStyleClass().addAll("bubble", "bubble-received");

                //hbox.getChildren().add(textFlow);
                //messagesBox.getChildren().add(hbox);

                mainScrollPane.setVvalue(1.0);
            } else if (!message.startsWith("(Private)") && !privateChat) {
                // handle group chat
                //HBox hbox = new HBox();
                //hbox.setAlignment(Pos.CENTER_LEFT);


                chatHistories.get("group").add("(r)" + message);
                populateChat("group");

                //Text text = new Text(message);

                //TextFlow textFlow = new TextFlow(text);
                //textFlow.getStyleClass().addAll("bubble", "bubble-received");

                //hbox.getChildren().add(textFlow);
                //messagesBox.getChildren().add(hbox);

                mainScrollPane.setVvalue(1.0);
            } else if (message.startsWith("(Private)")) {
                String messageContent = message.substring("(Private) ".length());

                // Split the messageContent into username and actual message using ": "
                String[] parts = messageContent.split(": ", 2);

                if (parts.length == 2) {
                    String username = parts[0]; // Extract the username
                    String actualMessage = parts[1]; // Extract the actual message

                    if (!chatHistories.containsKey(username)) {
                        chatHistories.put(username, new ArrayList<>());
                    }

                    chatHistories.get(username).add("(r)" + actualMessage);
                }
            } else {
                chatHistories.get("group").add("(r)" + message);
            }
        });
    }

    private void populateChat(String key) {
        messagesBox.getChildren().clear();
        for (String message : chatHistories.get(key)) {
            HBox hBox = new HBox();
            if (message.startsWith("(r)")) {
                message = message.substring("(r)".length()).trim();

                hBox.setAlignment(Pos.CENTER_LEFT);
                Text text = new Text(message);
                TextFlow textFlow = new TextFlow(text);
                textFlow.getStyleClass().addAll("bubble", "bubble-received");
                hBox.getChildren().add(textFlow);
            } else {
                message = message.substring("(s)".length()).trim();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                Text text = new Text(message);
                TextFlow textFlow = new TextFlow(text);
                textFlow.getStyleClass().addAll("bubble", "bubble-sent");
                hBox.getChildren().add(textFlow);
            }
            messagesBox.getChildren().add(hBox);
        }
    }

    // private chat
    @FXML
    private ListView<String> userListView;

    private void populateUserList(List<String> users) {
        Platform.runLater(() -> {
            userListView.getItems().clear();
            userListView.getItems().addAll(users);
        });
    }

    @FXML
    protected void onUserClicked() {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Perform actions to switch to private chat mode with selectedUser
            switchToPrivateChat(selectedUser);
        }
    }

    private boolean privateChat = false;
    private String receiver = "";
    private Map<String, List<String>> chatHistories = new HashMap<>();

    private void switchToPrivateChat(String selectedUser) {
        // Implement your logic to switch to private chat mode with the selected user
        // For example, update UI or display messages related to private chat
        System.out.println("Switching to private chat with: " + selectedUser);

        // Here you can update the UI or perform actions based on the selected user
        // You may want to clear existing messages, update labels, etc.

        // Example: Clear messages box
        messagesBox.getChildren().clear();
        // Example: Show private chat header
        welcomeText.setText("Private chat with " + selectedUser);
        privateChat = true;
        groupChatButton.setVisible(true);

        receiver = selectedUser;
        if (!chatHistories.containsKey(receiver)) {
            chatHistories.put(receiver, new ArrayList<>());
        }
        populateChat(receiver);
    }

    @FXML
    protected void onGroupChatButtonClick() {
        privateChat = false;
        receiver = "";

        populateChat("group");
        groupChatButton.setVisible(false);
        welcomeText.setText("Welcome, " + myUsername + "!");

        mainScrollPane.setVvalue(1.0);
    }
}