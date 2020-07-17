package com.parkinglot.service;

public class ParkingLot {
    public Object vehicle;

    public boolean parkVehicle(Object vehicle) {
        this.vehicle = vehicle;
        return true;
    }
}
