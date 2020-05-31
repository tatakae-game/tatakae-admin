package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.services.RoomService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TicketsViewController {

    @FXML
    private AnchorPane baseAnchorPane;


    @FXML
    private VBox ticketsListContainer;

    @FXML
    public void initialize() {
        try {
            initializeTickets();

        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadChatView(final Room room) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));
        Parent root = loader.load();

        ChatViewController controller = loader.getController();
        controller.initialize(room);

        baseAnchorPane.getChildren()
                .setAll(root);
    }

    private void initializeTickets() throws FailedParsingJsonException {
        try {
            final var tickets = RoomService.getAllTickets();

            for (final var ticket : tickets) {
                Button button = new Button(ticket.getName());
                button.setAlignment(Pos.CENTER_LEFT);
                button.setMinWidth(1200);
                button.setMinHeight(60);
                button.setStyle("-fx-background-color: brown; -fx-text-fill: white; -fx-font-size: 20;");

                EventHandler<ActionEvent> event = e -> {
                    try {
                        loadChatView(ticket);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                };
                button.setOnAction(event);

                VBox.setMargin(button, new Insets(15, 25, 15, 25));

                ticketsListContainer.getChildren().add(button);
            }

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Rooms list: " + e.getMessage());
        }
    }
}
