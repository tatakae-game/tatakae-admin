package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.User;
import kong.unirest.Unirest;
import java.util.concurrent.ExecutionException;

public class AuthService {

    private static boolean authed = false;

    public static User authenticate (final String username, final String password)
            throws ExecutionException, InterruptedException {

        var token = login(username, password);

        if (token.isEmpty()) { return null; }

        var user = UserService.getUserByToken(token);

        if (user != null) {
            if (isAdmin(user)) {
                authed = true;
            }
        }

        return user;
    }

    public static boolean isAuthed() { return authed; }

    public void logout() {
        authed = false;
    }

    private static boolean isAdmin(User user) {
        var groups = user.getGroups();

        for (var group : groups) {
            for (var permission : group.getPermissions()) {
                if (permission.getName().equals("Dashboard")) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String login(final String username, final String password)
            throws ExecutionException, InterruptedException {

        var res = Unirest.post(Config.url + "/auth/login")
                .header("accept", "application/json")
                .field("username", username)
                .field("password", password)
                .asJsonAsync().get().getBody().getObject();

        if (res.has("success") && res.getBoolean("success")
                && res.has("token") && !res.getString("token").isEmpty()) {
            return res.getString("token");
        } else {
            return "";
        }
    }

}
