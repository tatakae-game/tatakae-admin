package com.tatakae.admin.core.models;

import java.time.LocalDateTime;

public class Message {
    private final String id;
    private final String type;
    private final String data;
    private final LocalDateTime date;

    public Message(String id, String type, String data, LocalDateTime date) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", type:'" + type + '\'' +
                ", data:'" + data + '\'' +
                ", date:" + date +
                '}';
    }
}
