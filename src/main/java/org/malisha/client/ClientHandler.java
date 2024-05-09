package org.malisha.client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessageWithoutHandler("SERVER: " + clientUsername + " has entered the chat!");
            broadcastUserList();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    //    @Override
//    public void run() {
//        String clientMessage;
//
//        while(socket.isConnected()) {
//            try {
//                clientMessage = bufferedReader.readLine();
//                broadcastMessage(clientMessage);
//            }
//            catch (IOException e) {
//                closeEverything(socket, bufferedReader, bufferedWriter);
//                break;
//            }
//        }
//    }
    @Override
    public void run() {
        String clientMessage;

        try {
            while ((clientMessage = bufferedReader.readLine()) != null) {
                broadcastMessage(clientMessage);
            }
        } catch (IOException e) {
            // Client disconnected
        } finally {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void broadcastMessageWithoutHandler(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void broadcastMessage(String message) {
        message = message.trim();
        if (message.equals("/list")) {
            sendUserList();
        } else if (message.startsWith("@")) {
            // Direct message format: "@username message"
            int spaceIndex = message.indexOf(' ');
            if (spaceIndex != -1) {
                String recipientUsername = message.substring(1, spaceIndex); // Extract username
                String directMessage = message.substring(spaceIndex + 1);

                sendDirectMessage(recipientUsername, directMessage);
            } else {
                // If there's no message after '@', handle it appropriately
                // For example, send an error message to the sender.
                try {
                    bufferedWriter.write("SERVER: Your direct message is empty. Please provide a message.");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        } else {
            for (ClientHandler clientHandler : clientHandlers) {
                try {
                    if (!clientHandler.clientUsername.equals(clientUsername)) {
                        clientHandler.bufferedWriter.write(clientUsername + ": " + message);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    private void sendUserList() {
        StringBuilder userList = new StringBuilder();
        userList.append("Active Users: ");
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.clientUsername.equals(clientUsername)) {
                userList.append(clientHandler.clientUsername).append(", ");
            }
        }
        // Send the user list to the client who requested it
        try {
            bufferedWriter.write(userList.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void broadcastUserList() {
        for (ClientHandler clientHandler : clientHandlers) {
            StringBuilder userList = new StringBuilder();
            userList.append("Active Users: ");

            for (ClientHandler otherClient : clientHandlers) {
                if (!otherClient.clientUsername.equals(clientHandler.clientUsername)) {
                    userList.append(otherClient.clientUsername).append(", ");
                }
            }

            try {
                clientHandler.bufferedWriter.write(userList.toString());
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    private void sendDirectMessage(String recipientUsername, String message) {
        boolean sent = false;
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.clientUsername.equals(recipientUsername)) {
                try {
                    clientHandler.bufferedWriter.write("(Private) " + clientUsername + ": " + message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                    sent = true;
                    break;
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
        if (!sent) {
            // Notify sender that recipient is not available
            try {
                bufferedWriter.write("SERVER: User '" + recipientUsername + "' is not available.");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        broadcastMessageWithoutHandler("SERVER: " + clientUsername + " has left the chat!");
        clientHandlers.remove(this);
        broadcastUserList();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
