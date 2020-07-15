package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.models.Message;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.LocalDataManager;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RoomService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static ArrayList<Room> getAllTickets() throws FailedParsingJsonException {
        try {
            var res = Unirest.get(Config.url + "/support/tickets")
                    .header("accept", "application/json")
                    .header("Authorization", LocalDataManager.getToken())
                    .asJsonAsync().get().getBody().getObject();

            return serialize(res.getJSONArray("rooms"));


        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Room: " + e.getMessage());
        }
    }

    public static boolean updateStatus(final String id, final String status) throws FailedParsingJsonException {
        try {
            var res = Unirest.put(Config.url + "/support/tickets/{id}/status")
                    .header("accept", "application/json")
                    .header("Authorization", LocalDataManager.getToken())
                    .routeParam("id", id)
                    .field("status", status.toLowerCase())
                    .asJsonAsync().get().getBody().getObject();

            return res.getBoolean("success");

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON response: " + e.getMessage());
        }
    }

    public static boolean updateAssignedTo(final String id, final String userId) throws FailedParsingJsonException {
        try {
            var res = Unirest.put(Config.url + "/support/tickets/{id}/assign")
                    .header("accept", "application/json")
                    .header("Authorization", LocalDataManager.getToken())
                    .routeParam("id", id)
                    .field("user", userId.toLowerCase())
                    .asJsonAsync().get().getBody().getObject();

            return res.getBoolean("success");

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON response: " + e.getMessage());
        }
    }

    public static ArrayList<Room> serialize(JSONArray jRooms) throws FailedParsingJsonException {
        ArrayList<Room> rooms = new ArrayList<>();

        for (int i = 0; i < jRooms.length(); i++) {
            rooms.add(serialize(jRooms.getJSONObject(i)));
        }

        return rooms;
    }

    public static Room serialize(JSONObject jRoom) throws FailedParsingJsonException {
        try {
            final var id = jRoom.getString("_id");
            final var name = jRoom.getString("name");
            final var status = jRoom.getString("status");
            final var author = UserService.getUserById(jRoom.getString("author"));
            final var assignedTo = jRoom.has("assigned_to") ? UserService.getUserById(jRoom.getString("assigned_to")) : null;
            final var created = LocalDateTime.parse(jRoom.getString("created"), formatter);
            final var isTicket = jRoom.getBoolean("is_ticket");
            final var users = UserService.serialize(jRoom.getJSONArray("users"));
            final var messages = serializeMessages(jRoom.getJSONArray("messages"));

            return new Room(id, status, author, name, isTicket, assignedTo, created, messages, users);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Room: " + e.getMessage());
        }
    }

    public static ArrayList<Message> serializeMessages(JSONArray jMessages) throws FailedParsingJsonException {
        try {
            ArrayList<Message> messages = new ArrayList<>();

            for (int i = 0; i < jMessages.length(); i++) {
                messages.add(serializeMessage(jMessages.getJSONObject(i)));
            }

            return messages;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON messages: " + e.getMessage());
        }
    }

    public static Message serializeMessage(final org.json.JSONObject jMessage) throws FailedParsingJsonException, JSONException {
        final var id = (jMessage.has("_id")) ? jMessage.getString("_id"): "";
        return getMessage(id, jMessage.getString("type"), jMessage.getString("data"), jMessage.getString("date"), jMessage.getString("author"));
    }

    private static Message getMessage(final String id, final String type, final String data, final String date, final String author) throws FailedParsingJsonException {
        try {
            var created = LocalDateTime.parse(date, formatter);
            return new Message(id, type, data, created, author);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON message: " + e.getMessage());
        }
    }

    public static Message serializeMessage(final JSONObject jMessage) throws FailedParsingJsonException {
        final var id = (jMessage.has("_id")) ? jMessage.getString("_id"): "";
        return getMessage(id, jMessage.getString("type"), jMessage.getString("data"), jMessage.getString("date"), jMessage.getString("author"));
    }
}
