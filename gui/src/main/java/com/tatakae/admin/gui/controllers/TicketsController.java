package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.services.RoomService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class TicketsController {

    @FXML
    private ScrollPane openedTicketsScrollPane;

    @FXML
    private VBox ticketsListContainer;

    @FXML
    public void initialize() {

        openedTicketsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        openedTicketsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        try {
            final var tickets = RoomService.getAllTickets();

            for (final var ticket : tickets) {
                Button button = new Button(ticket.getName());
                button.setAlignment(Pos.CENTER_LEFT);
                button.setMinWidth(1200);
                button.setMinHeight(60);
                button.setStyle("-fx-background-color: brown; -fx-text-fill: white; -fx-font-size: 20;");

                EventHandler<ActionEvent> event = e -> loadChatView(ticket.getId());
                button.setOnAction(event);

                VBox.setMargin(button, new Insets(15, 25, 15, 25));

                ticketsListContainer.getChildren().add(button);
            }

        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void loadChatView(final String id) {
        System.out.println("hello " + id);
    }
}
