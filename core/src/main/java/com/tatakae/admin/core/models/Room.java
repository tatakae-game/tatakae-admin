package com.tatakae.admin.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Room {

    private final String id;
    private final String status;
    private final User author;
    private final String name;
    private final boolean isTicket;
    private final User assignedTo;
    private final LocalDateTime created;
    private final ArrayList<Message> messages;
    private final ArrayList<User> users;

    public Room(String id, String status, User author, String name, boolean isTicket,
                User assignedTo, LocalDateTime created, ArrayList<Message> messages, ArrayList<User> users) {
        this.id = id;
        this.status = status;
        this.author = author;
        this.name = name;
        this.isTicket = isTicket;
        this.assignedTo = assignedTo;
        this.created = created;
        this.messages = messages;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public User getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public boolean isTicket() {
        return isTicket;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", status:'" + status + '\'' +
                ", author:" + author +
                ", name:'" + name + '\'' +
                ", isTicket:" + isTicket +
                ", assignedTo:" + assignedTo +
                ", created:" + created +
                ", messages:" + messages +
                ", users:" + users +
                '}';
    }
}

