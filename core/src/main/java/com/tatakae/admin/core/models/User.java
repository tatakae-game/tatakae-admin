package com.tatakae.admin.core.models;

import java.util.ArrayList;

public class User {
    private final String id;
    private final String username;
    private final String email;
    private final ArrayList<Group> groups;

    public User() {
        this.id = "";
        this.username = "";
        this.email = "";
        this.groups = null;
    }

    public User(final String id, final String username, final String email,
                final ArrayList<Group> groups) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
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
                '}';
    }
}
