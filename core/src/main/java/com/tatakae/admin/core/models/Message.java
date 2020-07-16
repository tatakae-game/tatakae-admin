package com.tatakae.admin.core.models;

import java.time.LocalDateTime;

public class Message {
    private final String id;
    private final String type;
    private final String data;
    private final String author;
    private final LocalDateTime date;

    public Message(final String type, final String data, final String author) {
        this.id = "";
        this.type = type;
        this.data = data;
        this.date = LocalDateTime.now();
        this.author = author;
    }

    public Message(String id, String type, String data, LocalDateTime date, final String author) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.date = date;
        this.author = author;
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

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", author:'" + author + '\'' +
                ", type:'" + type + '\'' +
                ", data:'" + data + '\'' +
                ", date:" + date +
                '}';
    }
}
