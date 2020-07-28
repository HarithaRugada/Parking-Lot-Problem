package com.parkinglot.model;

public class Vehicle {
    private String color;
    private String modelName;

    public Vehicle(String color, String modelName) {
        this.color = color;
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getColor() {
        return color;
    }
}