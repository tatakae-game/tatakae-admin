package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.User;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class UserService {

    public static User getUserByToken(final String token)
            throws FailedParsingJsonException {
        try {
            var res = Unirest.get(Config.url + "/users/me")
                    .header("accept", "application/json")
                    .header("Authorization", token)
                    .asJsonAsync().get().getBody().getObject();

            return UserService.serialize(res.getJSONObject("profile"), token);


        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON USer: " + e.getMessage());
        }
    }

    public static User serialize(JSONObject jUser) throws FailedParsingJsonException{
        try {
            var id = jUser.getString("id");
            var name = jUser.getString("username");
            var email = jUser.getString("email");
            var groups = GroupService.serialize(jUser.getJSONArray("groups"));

            return new User(id, name,email,groups);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON User: " + e.getMessage());
        }
    }

    public static User serialize(JSONObject jUser, final String token) throws FailedParsingJsonException {
        try {
            var id = jUser.getString("id");
            var name = jUser.getString("username");
            var email = jUser.getString("email");
            var groups = GroupService.serialize(jUser.getJSONArray("groups"));

            return new User(id, name,email,groups, token);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON User: " + e.getMessage());
        }
    }
}
