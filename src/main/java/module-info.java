module ru.itis.danyook.chatbotkr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.net.http;
    requires org.json;

    opens ru.itis.danyook.chatbotkr to javafx.fxml;
    exports ru.itis.danyook.chatbotkr;
}