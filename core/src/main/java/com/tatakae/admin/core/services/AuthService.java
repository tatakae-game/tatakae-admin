package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.StoredDataManager;
import com.tatakae.admin.core.models.User;

import kong.unirest.Unirest;

import java.io.FileWriter;
import java.util.concurrent.ExecutionException;

public class AuthService {

    private static boolean authed = false;

    public static User authenticate (final String username, final String password) throws ExecutionException {
        try {
            var token = getToken(username, password);

            if (token.isEmpty()) { return null; }

            var user = UserService.getUserByToken(token);

            if (isAdmin(user)) {
                authed = true;
                final var configFile = StoredDataManager.getFile("config.json");
                String jsonContent = "{\"token\": \"" + token + "\"}";

                final var fw = new FileWriter(configFile);
                fw.write(jsonContent);
                fw.flush();
                fw.close();
            }

            return user;

        } catch (Exception e) {
            throw new ExecutionException(e.getMessage(), e.getCause());
        }
    }

    public static boolean isAuthed() { return authed; }

    public static void logout() {
        authed = false;
    }

    private static boolean isAdmin(User user) {
        var groups = user.getGroups();

        for (var group : groups) {
            for (var permission : group.getPermissions()) {
                if (permission.getName().equals("Dashboard")) {
                    return permission.getValue();
                }
            }
        }

        return false;
    }

    private static String getToken(final String username, final String password) throws ExecutionException {
        try {
            var res = Unirest.post(Config.url + "/auth/login")
                    .header("accept", "application/json")
                    .field("username", username)
                    .field("password", password)
                    .asJsonAsync().get().getBody().getObject();

            return (res.has("success") && res.getBoolean("success")) ? res.getString("token") : "";

        } catch (Exception e) {
            throw new ExecutionException(e.getMessage(), e.getCause());
        }
    }

}
