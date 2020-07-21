package com.tatakae.admin.core.models;

public class Event {

    private String name;
    private Address address;
    private String obstacle;

    public Event(String name, Address address, String obstacle) {
        this.name = name;
        this.address = address;
        this.obstacle = obstacle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getObstacle() {
        return obstacle;
    }

    public void setObstacle(String obstacle) {
        this.obstacle = obstacle;
    }
}
