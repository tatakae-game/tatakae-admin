package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.StoredDataManager;
import com.tatakae.admin.core.models.User;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;

public class UserService {

    public static User getUserById(final String id) throws FailedParsingJsonException {
        try {
            var res = Unirest.get(Config.url + "/users/" + id)
                    .header("accept", "application/json")
                    .header("Authorization", StoredDataManager.getToken())
                    .asJsonAsync().get().getBody().getObject();

            return UserService.serialize(res.getJSONObject("profile"));
        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON User: " + e.getMessage());
        }
    }

    public static User getUserByToken(final String token) throws FailedParsingJsonException {
        try {
            var res = Unirest.get(Config.url + "/users/me")
                    .header("accept", "application/json")
                    .header("Authorization", token)
                    .asJsonAsync().get().getBody().getObject();

            return serialize(res.getJSONObject("profile"));


        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON User: " + e.getMessage());
        }
    }

    public static ArrayList<User> serialize(JSONArray jUsers) throws FailedParsingJsonException{
        try {
            ArrayList<User> users = new ArrayList<>();

            for (int i = 0; i < jUsers.length(); i++) {
                users.add(serialize(jUsers.getJSONObject(i)));
            }

            return users;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Users list: " + e.getMessage());
        }
    }

    public static User serialize(JSONObject jUser) throws FailedParsingJsonException{
        try {
            var id = jUser.getString("id");
            var username = jUser.getString("username");
            var email = jUser.getString("email");
            var groups = GroupService.serialize(jUser.getJSONArray("groups"));

            return new User(id, username, email, groups);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON User: " + e.getMessage());
        }
    }
}
