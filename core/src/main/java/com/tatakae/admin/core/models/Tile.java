package com.tatakae.admin.core.models;

import java.util.ArrayList;

public class Tile {
    private String ground;
    private String obstacle;
    private ArrayList<String> items;
    private Address address;

    public Tile(String ground, String obstacle, ArrayList<String> items, Address address) {
        this.ground = ground;
        this.obstacle = obstacle;
        this.items = items;
        this.address = address;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public String getObstacle() {
        return obstacle;
    }

    public void setObstacle(String obstacle) {
        this.obstacle = obstacle;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
