package com.tatakae.admin.core;

import kong.unirest.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class User {
    private final String id;
    private final String username;
    private final String token;

    public User() {
        this.id = "";
        this.username = "";
        this.token = "";
    }

    public User(final String id, final String username, final String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    private ArrayList<String> fillCredentials() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> credentials = new ArrayList<>();

        System.out.print("\tUsername: ");
        credentials.add(scanner.next());
        System.out.print("\tPassword: ");
        credentials.add(scanner.next());

        return credentials;
    }

    public User login() {
        try {
            ArrayList<String> credentials = fillCredentials();
            JSONObject res = Auth.login(credentials.get(0), credentials.get(1));
            if (res.getBoolean("success")) {
                System.out.println("\nConnection successful.\n");
                return new User(
                        res.getJSONObject("user").getString("id"),
                        res.getJSONObject("user").getString("username"),
                        res.getString("token")
                );
            } else {
                System.out.println("\nConnection failed.\n");
                return new User();
            }
        } catch (InterruptedException | ExecutionException exception) {
            System.out.println(exception.getMessage());
            return new User();
        }
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
