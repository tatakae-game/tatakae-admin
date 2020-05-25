package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.models.Message;
import com.tatakae.admin.core.models.Room;
import com.tatakae.admin.core.StoredDataManager;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RoomService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static ArrayList<Room> getAllTickets()
            throws FailedParsingJsonException {
        try {
            var res = Unirest.get(Config.url + "/support/tickets")
                    .header("accept", "application/json")
                    .header("Authorization", StoredDataManager.getToken())
                    .asJsonAsync().get().getBody().getObject();

            return serialize(res.getJSONArray("rooms"));


        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Room: " + e.getMessage());
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
            var id = jRoom.getString("_id");
            var name = jRoom.getString("name");
            var status = jRoom.getString("status");
            var author = UserService.getUserById(jRoom.getString("author"));
            var assignedTo = jRoom.has("assigned_to") ? UserService.getUserById(jRoom.getString("assigned_to")) : null;
            var created = LocalDateTime.parse(jRoom.getString("created"), formatter);
            var isTicket = jRoom.getBoolean("is_ticket");
            var users = UserService.serialize(jRoom.getJSONArray("users"));
            var messages = serializeMessages(jRoom.getJSONArray("messages"));

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
                var jMessage = jMessages.getJSONObject(i);
                var id = jMessage.getString("_id");
                var type = jMessage.getString("type");
                var data = jMessage.getString("data");
                var date = LocalDateTime.parse(jMessage.getString("date"), formatter);
                messages.add(new Message(id, type, data, date));
            }

            return messages;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON messages" + e.getMessage());
        }
    }
}
