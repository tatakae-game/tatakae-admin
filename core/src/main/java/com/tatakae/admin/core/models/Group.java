package com.tatakae.admin.core.models;

import java.util.ArrayList;

public class Group {
    private final String id;
    private final String name;
    private final ArrayList<Permission> permissions;

    public Group() {
        this.id = "";
        this.name = "";
        this.permissions = null;
    }

    public Group(final String id, final String name, final ArrayList<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return '{' +
                "id: '" + id + '\'' +
                ", name: '" + name + '\'' +
                ", permissions: " + permissions +
                '}';
    }
}
