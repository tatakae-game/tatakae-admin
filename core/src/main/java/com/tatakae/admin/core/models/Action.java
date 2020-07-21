package com.tatakae.admin.core.models;

import java.util.ArrayList;

public class Action {

    private Address newPosition;
    private String newOrientation;
    private String name;
    private Integer damage;
    private ArrayList<Event> events;
    private ArrayList<Tile> tilesChecked;
    private String robotId;

    public Action(Address newPosition,
                  String newOrientation,
                  String name,
                  Integer damage,
                  ArrayList<Event> events,
                  ArrayList<Tile> tilesChecked,
                  String robotId) {
        this.newPosition = newPosition;
        this.newOrientation = newOrientation;
        this.name = name;
        this.damage = damage != null ? damage : 0;
        this.events = events;
        this.tilesChecked = tilesChecked;
        this.robotId = robotId;
    }

    public Address getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Address newPosition) {
        this.newPosition = newPosition;
    }

    public String getNewOrientation() {
        return newOrientation;
    }

    public void setNewOrientation(String newOrientation) {
        this.newOrientation = newOrientation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Tile> getTilesChecked() {
        return tilesChecked;
    }

    public void setTilesChecked(ArrayList<Tile> tilesChecked) {
        this.tilesChecked = tilesChecked;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }
}
