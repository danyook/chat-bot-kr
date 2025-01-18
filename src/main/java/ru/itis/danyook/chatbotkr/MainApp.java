package ru.itis.danyook.chatbotkr;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private TextArea chatArea;
    private TextField inputField;

    private ChatBot botLogic;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("JavaFX ChatBot");

        botLogic = new ChatBot();

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("Введите команду...");
        inputField.setOnAction(event -> handleUserInput());

        BorderPane root = new BorderPane();
        VBox bottomPane = new VBox();
        bottomPane.getChildren().add(inputField);
        bottomPane.setPadding(new Insets(10));

        root.setCenter(chatArea);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        addMessage("Чат-бот готов. Введите 'list' для получения списка команд.");
    }

    private void handleUserInput() {
        String userInput = inputField.getText().trim();
        inputField.clear();

        if (!userInput.isEmpty()) {

            addMessage("Вы: " + userInput);

            String botResponse = botLogic.handleCommand(userInput);
            addMessage("Бот: " + botResponse);

            if (userInput.equalsIgnoreCase("quit")) {
                Stage stage = (Stage) inputField.getScene().getWindow();
                stage.close();
            }
        }
    }

    private void addMessage(String message) {
        chatArea.appendText(message + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

