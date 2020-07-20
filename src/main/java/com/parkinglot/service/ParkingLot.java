package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotObserver;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private final int parkingLotCapacity;
    private List<IParkingLotObserver> parkingLotObservers;
    private List<Object> vehicleList;

    public ParkingLot(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
        this.vehicleList = new ArrayList<>();
        parkingLotObservers = new ArrayList<>();
    }

    public void parkVehicle(Object vehicle) throws ParkingLotException {
        if (this.vehicleList.size() == this.parkingLotCapacity) {
            for (IParkingLotObserver observer : parkingLotObservers) {
                observer.parkingFull();
            }
            throw new ParkingLotException("Parking lot is full", ParkingLotException.ExceptionType.PARKING_FULL);
        }
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("Already Parked", ParkingLotException.ExceptionType.ALREADY_PARKED);
        vehicleList.add(vehicle);
    }

    public void unParkVehicle(Object vehicle) throws ParkingLotException {
        if (vehicleList.contains(vehicle)) {
            this.vehicleList.remove(vehicle);
            for (IParkingLotObserver observer : parkingLotObservers) {
                observer.parkingAvailable();
            }
            throw new ParkingLotException("Parking lot is available", ParkingLotException.ExceptionType.PARKING_AVAILABLE);
        }
        throw new ParkingLotException("Vehicle not found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public boolean isVehicleParked(Object vehicle) {
        return vehicleList.contains(vehicle);
    }

    public void registerParkingLotObserver(IParkingLotObserver observer) {
        this.parkingLotObservers.add(observer);
    }
}
