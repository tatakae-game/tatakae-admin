package com.tatakae.admin.cli;

import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.models.Message;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.services.UserService;
import com.tatakae.admin.core.services.WebSocketService;
import io.socket.client.Socket;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Chat {

    private Socket socket;

    private final Room ticket;

    public Chat(final Room ticket) {
        this.ticket = ticket;
        initializeSocket(ticket);
    }

    private void initializeSocket(final Room room) {
        try {
            final var query = new HashMap<String, String>();
            query.put("room", room.getId());

            socket = WebSocketService.connect("/chat", query);
            socket.on("new message", this::call).on(Socket.EVENT_DISCONNECT, args -> { });
            socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(final String message) {
        try {
            if (socket.connected() && !message.isEmpty()) {

                var json = new JSONObject();
                json.put("type", "text");
                json.put("data", message);

                socket.emit("message", json);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void showMessages() {
        System.out.println();

        final var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (final var message : ticket.getMessages()) {
            try {
                final var author = UserService.getUserById(message.getAuthor());

                System.out.print("[" + message.getDate().format(dateTimeFormatter) + "]");
                System.out.print("[" + author.getUsername() + "]: ");
                System.out.println(message.getData());

            } catch (FailedParsingJsonException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return;
            }
            System.out.println();
        }
    }

    private void call(Object... args) {
        var json = new kong.unirest.json.JSONObject(args[0].toString());

        var message = new Message(
                json.getString("type"),
                json.getString("data"),
                json.getString("author")
        );

        ticket.getMessages().add(message);
    }
}
