package com.parkinglot.service;

public class ParkingLot {
    public Object vehicle;

    public boolean parkVehicle(Object vehicle) {
        this.vehicle = vehicle;
        return true;
    }

    public boolean unParkVehicle(Object vehicle) {
        if (this.vehicle.equals(vehicle))
            return true;
        return false;
    }
}
