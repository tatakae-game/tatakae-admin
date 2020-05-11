package com.tatakae.admin.core;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Auth{

    public static boolean authed = false;

    public static JSONObject login (final String username, final String password)
            throws ExecutionException, InterruptedException {

        var res = Unirest.post(Service.url + "auth/login")
                .header("accept", "application/json")
                .field("username", username)
                .field("password", password)
                .asJsonAsync().get().getBody().getObject();

        Auth.authed = res.getBoolean("success");

        return res;
    }

    public boolean isAuthed () {
        return Auth.authed;
    }

    public void logout () {
        Auth.authed = false;
    }

}
