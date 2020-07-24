package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotObserver;
import com.parkinglot.model.ParkingSlot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLot {
    private int parkingLotCapacity;
    private List<ParkingSlot> vehicleList;
    private List<IParkingLotObserver> parkingLotObservers;

    public ParkingLot(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
        this.vehicleList = new ArrayList<>();
        parkingLotObservers = new ArrayList<>();
    }

    public void setCapacity(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
    }

    public void parkVehicle(Object vehicle) throws ParkingLotException {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        if (!this.vehicleList.contains(null)) {
            for (IParkingLotObserver observer : parkingLotObservers) {
                observer.parkingFull();
            }
            throw new ParkingLotException("Parking lot is full", ParkingLotException.ExceptionType.PARKING_FULL);
        }
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("Already Parked", ParkingLotException.ExceptionType.ALREADY_PARKED);
        int emptySlot = getParkingSlot();
        this.vehicleList.set(emptySlot, parkingSlot);
    }

    public boolean unParkVehicle(Object vehicle) throws ParkingLotException {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        for (int slotNumber = 0; slotNumber < this.vehicleList.size(); slotNumber++) {
            if (this.vehicleList.contains(parkingSlot)) {
                this.vehicleList.set(slotNumber, null);
                for (IParkingLotObserver observer : parkingLotObservers) {
                    observer.parkingAvailable();
                }
                return true;
            }
        }
        throw new ParkingLotException("Vehicle not found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public boolean isVehicleParked(Object vehicle) {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        return this.vehicleList.contains(parkingSlot);
    }

    public void registerParkingLotObserver(IParkingLotObserver observer) {
        this.parkingLotObservers.add(observer);
    }

    public int initializeParkingLot() {
        IntStream.range(0, this.parkingLotCapacity).forEachOrdered(slots -> vehicleList.add(null));
        return vehicleList.size();
    }

    public ArrayList getSlotList() {
        ArrayList emptySlots = new ArrayList();
        for (int slot = 0; slot < this.parkingLotCapacity; slot++) {
            if (this.vehicleList.get(slot) == null)
                emptySlots.add(slot);
        }
        return emptySlots;
    }

    public int getParkingSlot() throws ParkingLotException {
        ArrayList<Integer> emptySlotList = getSlotList();
        for (int slot = 0; slot < emptySlotList.size(); slot++) {
            if (emptySlotList.get(0) == (slot)) {
                return slot;
            }
        }
        throw new ParkingLotException("Parking lot is full", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public int findVehicle(Object vehicle) throws ParkingLotException {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        if (this.vehicleList.contains(parkingSlot))
            return this.vehicleList.indexOf(parkingSlot);
        throw new ParkingLotException("Vehicle not found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public boolean isTimeSet(Object vehicle) {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        for (int i = 0; i < this.vehicleList.size(); i++) {
            if (this.vehicleList.get(i).time != null && this.vehicleList.contains(parkingSlot))
                return true;
        }
        return false;
    }
}
