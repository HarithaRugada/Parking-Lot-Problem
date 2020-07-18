package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;

public class ParkingLot {
    public Object vehicle;
    private int parkingLotCapacity;
    private int currentParkingLotSize;

    public ParkingLot(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
    }

    public ParkingLot() {
    }

    public boolean parkVehicle(Object vehicle) throws ParkingLotException {
        if (this.currentParkingLotSize == this.parkingLotCapacity)
            throw new ParkingLotException("Parking lot is full");
        this.vehicle = vehicle;
        currentParkingLotSize++;
        return true;
    }

    public boolean unParkVehicle(Object vehicle) throws ParkingLotException {
        if (this.vehicle != null && this.vehicle.equals(vehicle)) {
            this.vehicle = null;
            currentParkingLotSize--;
            return true;
        }
        throw new ParkingLotException("Vehicle not found");
    }

}
