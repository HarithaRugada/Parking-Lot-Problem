package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.utility.AirportSecurity;
import com.parkinglot.utility.ParkingOwner;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private final int parkingLotCapacity;
    private ParkingOwner owner;
    private AirportSecurity security;
    private List<Object> vehicleList;

    public ParkingLot(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
        this.vehicleList = new ArrayList<>();
    }

    public void parkVehicle(Object vehicle) throws ParkingLotException {
        if (this.vehicleList.size() == this.parkingLotCapacity) {
            owner.parkingFull();
            security.parkingFull();
            throw new ParkingLotException("Parking lot is full", ParkingLotException.ExceptionType.PARKING_FULL);
        }
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("Already Parked", ParkingLotException.ExceptionType.ALREADY_PARKED);
        vehicleList.add(vehicle);
    }

    public boolean unParkVehicle(Object vehicle) throws ParkingLotException {
        if (vehicleList.contains(vehicle)) {
            vehicleList.remove(vehicle);
            return true;
        }
        throw new ParkingLotException("Vehicle not found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.vehicleList.contains(vehicle);
    }

    public void registerOwner(ParkingOwner owner) {
        this.owner = owner;
    }

    public void registerSecurity(AirportSecurity security) {
        this.security = security;
    }
}
