module org.malisha.chatappjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires SocketProgrammingChatApp;

    opens org.malisha.chatappjavafx to javafx.fxml;
    exports org.malisha.chatappjavafx;
}