package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.Exceptions.CannotCreateFileException;
import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.LocalDataManager;
import com.tatakae.admin.core.models.Message;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.services.RoomService;
import com.tatakae.admin.core.services.UserService;
import com.tatakae.admin.core.services.WebSocketService;
import com.tatakae.admin.gui.bubble.BubbleSpec;
import com.tatakae.admin.gui.bubble.BubbledLabel;
import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ChatViewController {

    private Socket socket;

    private final String[] statusChoices = {"opened", "in progress", "closed"};

    @FXML
    private Label subjectLabel;

    @FXML
    private Label endUserLabel;

    @FXML
    private Label createdLabel;

    @FXML
    private TextField messageTextField;

    @FXML
    private VBox messagesVBoxContainer;

    @FXML
    private ScrollPane messagesScrollPane;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private ChoiceBox<String> assignedToChoiceBox;

    @FXML
    public void initialize(Room room) {
        try {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            subjectLabel.setText(room.getName());
            endUserLabel.setText(room.getAuthor().getUsername());
            createdLabel.setText(room.getCreated().format(formatter));

            initializeStatus(room);
            initializeAssignedTo(room);
            initializeMessages(room);
            initializeSocket(room);

            messageTextField.setOnKeyPressed(keyEvent -> enterKeyPressed(keyEvent.getCode()));

        } catch (CannotCreateFileException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initializeStatus(final Room room) {
        for (var status : statusChoices) {
            statusChoiceBox.getItems().add(status);
        }
        statusChoiceBox.setValue(room.getStatus());
        statusChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener( (options, oldValue, newValue) -> {
                    try {
                        if (!oldValue.equals(newValue)) {
                            RoomService.updateStatus(room.getId(), newValue);
                        }
                    } catch (FailedParsingJsonException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                });
    }

    private void initializeAssignedTo(final Room room) {
        try {
            var administrators = UserService.getAdministrators();

            for (var admin : administrators) {
                assignedToChoiceBox.getItems().add(admin.getUsername());
            }

            if (room.getAssignedTo() != null) {
                assignedToChoiceBox.setValue(room.getAssignedTo().getUsername());
            }

            assignedToChoiceBox.getSelectionModel().selectedItemProperty()
                    .addListener( (options, oldValue, newValue) -> {
                        if (oldValue == null || !oldValue.equals(newValue)) {
                            for (var admin : administrators) {
                                if (admin.getUsername().equals(newValue)) {
                                    try {
                                        RoomService.updateAssignedTo(room.getId(), admin.getId());
                                        break;
                                    } catch (FailedParsingJsonException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
        } catch (FailedParsingJsonException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateChatBubble(final Message message) {
        try {
            var currentUser = UserService.getUserByToken(LocalDataManager.getToken());
            BubbleSpec direction;
            Color color;
            Pos position;

            if (currentUser.getId().equals(message.getAuthor())) {
                direction = BubbleSpec.FACE_RIGHT_CENTER;
                color = Color.rgb(119, 182, 249);
                position = Pos.TOP_RIGHT;

            } else {
                direction = BubbleSpec.FACE_LEFT_CENTER;
                color = Color.rgb(241, 241, 241);
                position = Pos.TOP_LEFT;
            }

            BubbledLabel bubbledLabel = new BubbledLabel(direction);
            bubbledLabel.setText(message.getData());
            bubbledLabel.setFont(new Font("system", 18));
            bubbledLabel.setBackground(new Background(new BackgroundFill(color,
                    new CornerRadii(0.5), null)));

            var hBox = new HBox();
            hBox.getChildren().add(bubbledLabel);
            hBox.setAlignment(position);
            VBox.setMargin(hBox, new Insets(5, 15, 5, 15));

            messagesVBoxContainer.getChildren().add(hBox);

        } catch (FailedParsingJsonException | CannotCreateFileException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeMessages(final Room room) {
        for (var message : room.getMessages()) {
            generateChatBubble(message);
        }

        messagesVBoxContainer.heightProperty().addListener(observable -> messagesScrollPane.setVvalue(1D));
    }

    @FXML
    private void initializeSocket(final Room room) throws CannotCreateFileException, URISyntaxException {
        try {
            final var query = new HashMap<String, String>();
            query.put("room", room.getId());

            socket = WebSocketService.connect("/chat", query);
            socket.on("new message", this::call).on(Socket.EVENT_DISCONNECT, args -> { });
            socket.connect();

        } catch (URISyntaxException | CannotCreateFileException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendMessage() {
        try {
            if (socket.connected() && !messageTextField.getText().isEmpty()) {
                final var data = messageTextField.getText();

                var json = new JSONObject();
                json.put("type", "text");
                json.put("data", data);

                socket.emit("message", json);
                messagesVBoxContainer.heightProperty().addListener(observable -> messagesScrollPane.setVvalue(1D));
                messageTextField.setText("");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void call(Object... args) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    try {
                        final Message message = RoomService.serializeMessage((JSONObject) args[0]);
                        generateChatBubble(message);
                    } catch (FailedParsingJsonException | JSONException e) {
                        e.printStackTrace();
                    }
                });
                return null;
            }
        };
        task.run();
    }

    @FXML
    private void enterKeyPressed(KeyCode keyCode) {
        if(keyCode == KeyCode.ENTER) {
            sendMessage();
        }
    }
}
