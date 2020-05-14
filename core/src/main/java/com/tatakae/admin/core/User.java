package com.tatakae.admin.core;

import java.util.ArrayList;

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
