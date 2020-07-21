package com.tatakae.admin.core.models;

import com.tatakae.admin.core.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Game {
    private String id;
    private ArrayList<User> winners;
    private ArrayList<User> participants;
    private LocalDateTime created;
    private ArrayList<Action> actions;
    private boolean isActive;

    public Game() {
        id = "";
        winners = null;
        participants = null;
        created = null;
        actions = null;
        isActive = false;
    }

    public Game(String id, ArrayList<User> winners, ArrayList<User> participants) {
        this.id = id;
        this.winners = winners;
        this.participants = participants;
        this.created = null;
        this.actions = null;
        this.isActive = false;
    }

    public Game(String id,
                ArrayList<User> winners,
                ArrayList<User> participants,
                LocalDateTime created,
                ArrayList<Action> actions,
                boolean isActive) {
        this.id = id;
        this.winners = winners;
        this.participants = participants;
        this.created = created;
        this.actions = actions;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<User> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<User> winners) {
        this.winners = winners;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", winners=" + winners +
                ", participants=" + participants +
                '}';
    }
}
