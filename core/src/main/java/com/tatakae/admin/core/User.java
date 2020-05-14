package com.tatakae.admin.core;

import com.tatakae.admin.core.services.AuthService;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class User {
    private final String id;
    private final String username;
    private final String email;
    private final ArrayList<Group> groups;
    private final String token;

    public User() {
        this.id = "";
        this.username = "";
        this.email = "";
        this.groups = null;
        this.token = "";
    }

    public User(final String id, final String username, final String email,
                final ArrayList<Group> groups) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.groups = groups;
        this.token = "";
    }

    public User(final String id, final String username, final String email,
                final ArrayList<Group> groups, final String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.groups = groups;
        this.token = token;
    }

    private static ArrayList<String> fillCredentials() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> credentials = new ArrayList<>();

        System.out.print("\tUsername: ");
        credentials.add(scanner.next());
        System.out.print("\tPassword: ");
        credentials.add(scanner.next());

        return credentials;
    }

    public static User login() {
        try {
            ArrayList<String> credentials = fillCredentials();
            return AuthService.authenticate(credentials.get(0), credentials.get(1));

        } catch (InterruptedException | ExecutionException exception) {
            System.out.println(exception.getMessage());
            return null;
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

    public ArrayList<Group> getGroups() { return groups; }

    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "{" +
                "id: '" + id + '\'' +
                ", username: '" + username + '\'' +
                ", email: '" + email + '\'' +
                ", groups: " + groups +
                ", token: '" + token + '\'' +
                '}';
    }
}
