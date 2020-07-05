package com.tatakae.admin.gui.controllers;

import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.LocalDataManager;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.services.RoomService;
import com.tatakae.admin.core.services.UserService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class TicketsViewController {

    @FXML
    private AnchorPane baseAnchorPane;

    @FXML
    private TabPane ticketsTabPane;

    @FXML
    private Tab openedTab;

    @FXML
    private Tab inProgressTab;

    @FXML
    private Tab closedTab;

    @FXML
    private Tab assignedToMeTab;

    @FXML
    private VBox openedTicketsListContainer;

    @FXML
    private VBox inProgressTicketsListContainer;

    @FXML
    private VBox closedTicketsListContainer;

    @FXML
    private VBox myTicketsListContainer;

    @FXML
    public void initialize() {
        try {
            ticketsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
                if (!oldTab.equals(newTab)) {
                    loadTab(newTab);
                }
            });

            loadTab(openedTab);

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

        baseAnchorPane.getChildren().setAll(root);
    }

    private Button generateTicketButton(final Room ticket) {
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

        return button;
    }

    private void loadOpenedTickets(final ArrayList<Room> tickets) {
        openedTicketsListContainer.getChildren().clear();
        for (var ticket : tickets) {
            if (ticket.getStatus().equals(openedTab.getText().toLowerCase())) {
                final var button = generateTicketButton(ticket);
                openedTicketsListContainer.getChildren().add(button);
            }
        }
    }

    private void loadInProgressTickets(final ArrayList<Room> tickets) {
        inProgressTicketsListContainer.getChildren().clear();
        for (var ticket : tickets) {
            if (ticket.getStatus().equals(inProgressTab.getText().toLowerCase())) {
                final var button = generateTicketButton(ticket);
                inProgressTicketsListContainer.getChildren().add(button);
            }
        }
    }

    private void loadClosedTickets(final ArrayList<Room> tickets) {
        closedTicketsListContainer.getChildren().clear();
        for (var ticket : tickets) {
            if (ticket.getStatus().equals(closedTab.getText().toLowerCase())) {
                final var button = generateTicketButton(ticket);
                closedTicketsListContainer.getChildren().add(button);
            }
        }
    }

    private void loadMyTickets(final ArrayList<Room> tickets) {
        try {
            final var currentUser = UserService.getUserByToken(LocalDataManager.getToken());
            myTicketsListContainer.getChildren().clear();
            for (final var ticket : tickets) {
                if (ticket.getAssignedTo() != null) {
                    if (ticket.getAssignedTo().getId().equals(currentUser.getId())) {
                        final var button = generateTicketButton(ticket);
                        myTicketsListContainer.getChildren().add(button);
                    }
                }
            }
        } catch (FailedParsingJsonException e) {
            e.printStackTrace();
        }
    }

    private void loadTab(final Tab tab) {
        try {
            final var tickets = RoomService.getAllTickets();

            if (tab.getId().equals(assignedToMeTab.getId())) {
                loadMyTickets(tickets);
            } else if (tab.getId().equals(openedTab.getId())) {
                loadOpenedTickets(tickets);
            } else if (tab.getId().equals(inProgressTab.getId())) {
                loadInProgressTickets(tickets);
            } else if (tab.getId().equals(closedTab.getId())) {
                loadClosedTickets(tickets);
            }
        } catch (FailedParsingJsonException e) {
            e.printStackTrace();
        }
    }
}
