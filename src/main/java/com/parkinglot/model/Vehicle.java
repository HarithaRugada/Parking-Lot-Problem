package com.parkinglot.model;

public class Vehicle {
    private String color;
    private String modelName;
    private String plateNumber;

    public Vehicle(String color, String modelName, String plateNumber) {
        this.color = color;
        this.modelName = modelName;
        this.plateNumber = plateNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public String getColor() {
        return color;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
}