package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.LocalDataManager;
import com.tatakae.admin.core.models.*;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GameService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static ArrayList<Game> getAllGames() throws FailedParsingJsonException {
        try {
            var res = Unirest.get(Config.url + "/games")
                    .header("accept", "application/json")
                    .header("Authorization", LocalDataManager.getToken())
                    .asJsonAsync().get().getBody().getObject();

            System.out.println(res);
            return serialize(res.getJSONArray("games"));


        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Games: " + e.getMessage());
        }
    }

    public static Game getGameById(String id) throws FailedParsingJsonException {
        try {
            var res = Unirest.get(Config.url + "/games/" + id)
                    .header("accept", "application/json")
                    .header("Authorization", LocalDataManager.getToken())
                    .asJsonAsync().get().getBody().getObject();

            System.out.println("access: " + res);

            return serialize(res.getJSONObject("games"));


        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Game: " + e.getMessage());
        }
    }

    public static boolean updateGameStatus(String id, Boolean status) throws FailedParsingJsonException {
        try {
            var res = Unirest.put(Config.url + "/games/status")
                    .header("accept", "application/json")
                    .header("Authorization", LocalDataManager.getToken())
                    .field("id", id)
                    .field("status", String.valueOf(status))
                    .asJsonAsync().get().getBody().getObject();

            System.out.println(res);
            return res.getBoolean("success");

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Game: " + e.getMessage());
        }

    }

    public static ArrayList<Game> serialize(JSONArray jGames) throws FailedParsingJsonException {
        ArrayList<Game> games = new ArrayList<>();

        for (int i = 0; i < jGames.length(); i++) {
            games.add(serialize(jGames.getJSONObject(i)));
        }

        return games;
    }

    public static Game serialize(JSONObject jGame) throws FailedParsingJsonException {
        try {

            final var id = jGame.has("id") ? jGame.getString("id") : jGame.getString("_id");
            final var winners = serializeUsers(jGame.getJSONArray("winners"));
            final var participants = serializeUsers(jGame.getJSONArray("participants"));
            final var created = jGame.has("created") ? LocalDateTime.parse(jGame.getString("created"), formatter) : null;
            final var actions = jGame.has("actions") ? serializeActions(jGame.getJSONArray("actions")) : null;
            final var isActive = jGame.has("active") && jGame.getBoolean("active");

            return new Game(id, winners, participants, created, actions, isActive);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Game: " + e.getMessage());
        }
    }

    public static ArrayList<User> serializeUsers(JSONArray jUsers) throws FailedParsingJsonException {
        try {

            final var users = new ArrayList<User>();

            for (final var user : jUsers) {
                users.add(UserService.getUserById((String) user));
            }

            return users;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Users: " + e.getMessage());
        }
    }

    public static ArrayList<Action> serializeActions(JSONArray jActions) throws FailedParsingJsonException {
        try {

            final var actions = new ArrayList<Action>();

            for (int i = 0; i < jActions.length(); i++) {
                actions.add(serializeAction(jActions.getJSONObject(i)));
            }

            return actions;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Actions: " + e.getMessage());
        }
    }

    public static Action serializeAction(JSONObject jAction) throws FailedParsingJsonException {
        try {
            final var robotId = jAction.getString("robot_id");

            Address newPosition;
            if (jAction.has("new_position")) {
                if (jAction.get("new_position") == null) {
                    newPosition = null;
                } else {
                    newPosition = serializeAddress(jAction.getJSONObject("new_position"));
                }
            } else {
                newPosition = null;
            }

            int damage;
            if (jAction.has("damage")) {
                if(jAction.get("damage") == null) {
                    damage = 0;
                } else {
                    damage = jAction.getInt("damage");
                }
            } else {
                damage = 0;
            }

            String newOrientation;
            if (jAction.has("new_orientation")) {
                if (jAction.get("new_orientation") == null) {
                    newOrientation = "";
                } else {
                    newOrientation = jAction.getString("new_orientation");
                }
            } else {
                newOrientation = "";
            }

            final var name = jAction.getString("name");
            final var events = serializeEvents(jAction.getJSONArray("events"));
            final var tilesChecked = jAction.has("tiles_checked") ? serializeTiles(jAction.getJSONArray("tiles_checked")) : null;

            return new Action(newPosition, newOrientation, name, damage, events, tilesChecked, robotId);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Action: " + e.getMessage());
        }
    }

    public static ArrayList<Event> serializeEvents(JSONArray jEvents) throws FailedParsingJsonException {
        try {

            final var events = new ArrayList<Event>();

            for (int i = 0; i < jEvents.length(); i++) {
                events.add(serializeEvent(jEvents.getJSONObject(i)));
            }

            return events;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Events: " + e.getMessage());
        }
    }

    public static ArrayList<Tile> serializeTiles(JSONArray jTiles) throws FailedParsingJsonException {
        try {

            final var tiles = new ArrayList<Tile>();

            for (int i = 0; i < jTiles.length(); i++) {
                tiles.add(serializeTile(jTiles.getJSONObject(i)));
            }

            return tiles;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Tiles: " + e.getMessage());
        }
    }

    public static Tile serializeTile(JSONObject jTile) throws FailedParsingJsonException {
        try {
            final var ground = jTile.getString("ground");
            final var obstacle = jTile.get("obstacle") != null ? jTile.getString("obstacle") : "";
            final var address = serializeAddress(jTile.getJSONObject("addresses"));
            final var items = serializeItems(jTile.getJSONArray("items"));

            return new Tile(ground, obstacle, items, address);

        } catch (Exception e) {
            System.out.println("jTile: " + jTile);
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Tile: " + e.getMessage());
        }
    }

    public static Event serializeEvent(JSONObject jEvent) throws FailedParsingJsonException {
        try {
            final var name = jEvent.getString("name");
            final var obstacle = jEvent.get("obstacle");
            final var address = jEvent.has("addresses") ? serializeAddress(jEvent.getJSONObject("addresses")) : null;

            return new Event(name, address, (String) obstacle);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Event: " + e.getMessage());
        }
    }

    public static Address serializeAddress(JSONObject jAddress) throws FailedParsingJsonException {
        try {
            final var x = jAddress.getInt("x");
            final var y = jAddress.getInt("y");

            return new Address(x, y);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Address: " + e.getMessage());
        }
    }

    public static ArrayList<String> serializeItems(JSONArray jItems) throws FailedParsingJsonException {
        try {

            final var items = new ArrayList<String>();

            for (final var item : jItems) {
                items.add((String) item);
            }

            return items;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Items: " + e.getMessage());
        }
    }
}
