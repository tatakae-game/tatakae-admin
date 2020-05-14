package com.tatakae.admin.core;

public class Permission {
    private final String id;
    private final String name;
    private final boolean value;

    public Permission(final String id, final String name, final boolean value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return '{' +
                "id: '" + id + '\'' +
                ", name: '" + name + '\'' +
                ", value: " + value +
                '}';
    }
}
