package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.User;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserService {

    public static User getUserByToken(final String token)
            throws ExecutionException, InterruptedException {
        User user = null;
        var res = Unirest.get(Config.url + "/users/me")
                .header("accept", "application/json")
                .header("Authorization", token)
                .asJsonAsync().get().getBody().getObject();

        if (res.has("success") && res.has("profile") && res.getBoolean("success")) {
            user = UserService.serialize(res.getJSONObject("profile"), token);
        }
        return user;
    }

    public static User serialize(JSONObject jUser) {
        if (jUser.has("id") && jUser.has("username")
                && jUser.has("email") && jUser.has("groups")) {
            var id = jUser.getString("id");
            var name = jUser.getString("username");
            var email = jUser.getString("email");
            var groups = GroupService.serialize(jUser.getJSONArray("groups"));

            return new User(id, name,email,groups);
        }

        return null;
    }

    public static User serialize(JSONObject jUser, final String token) {
        if (jUser.has("id") && jUser.has("username")
                && jUser.has("email") && jUser.has("groups")) {
            var id = jUser.getString("id");
            var name = jUser.getString("username");
            var email = jUser.getString("email");
            var groups = GroupService.serialize(jUser.getJSONArray("groups"));

            return new User(id, name,email,groups, token);
        }

        return null;
    }
}
