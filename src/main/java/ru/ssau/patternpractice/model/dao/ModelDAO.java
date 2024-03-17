package ru.ssau.patternpractice.model.dao;

import java.util.UUID;

public class ModelDAO {
    private UUID ownerId;
    private double cost;
    private String name;

    public ModelDAO(UUID ownerId, double cost, String name) {
        this.ownerId = ownerId;
        this.cost = cost;
        this.name = name;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
