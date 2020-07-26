package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotObserver;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLot {
    private int parkingLotCapacity;
    private List<ParkingSlot> vehicleList;
    private List<IParkingLotObserver> parkingLotObservers;
    private int vehicleCount;

    public ParkingLot(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
        this.vehicleList = new ArrayList<>();
        parkingLotObservers = new ArrayList<>();
    }

    public void setCapacity(int parkingLotCapacity) {
        this.parkingLotCapacity = parkingLotCapacity;
    }

    public void registerParkingLotObserver(IParkingLotObserver observer) {
        this.parkingLotObservers.add(observer);
    }

    public void parkVehicle(Vehicle vehicle, Enum type, String attendantName) throws ParkingLotException {
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("Already Parked", ParkingLotException.ExceptionType.ALREADY_PARKED);
        ParkingSlot parkingSlot = new ParkingSlot(vehicle, type, attendantName);
        int emptySlot = getParkingSlot(vehicle);
        parkingSlot.setSlot(emptySlot);
        this.vehicleList.set(emptySlot, parkingSlot);
        vehicleCount++;
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        return this.vehicleList.contains(parkingSlot);
    }

    public boolean unParkVehicle(Vehicle vehicle) {
        boolean present = this.vehicleList
                .stream()
                .anyMatch(slot -> (vehicle) == slot.getVehicle());
        for (IParkingLotObserver observer : parkingLotObservers) {
            observer.parkingAvailable();
        }
        return present;
    }

    public int initializeParkingLot() {
        IntStream.range(0, this.parkingLotCapacity).forEachOrdered(slots -> this.vehicleList.add(new ParkingSlot(slots)));
        return vehicleList.size();
    }

    public ArrayList getSlotList() {
        ArrayList<Integer> emptySlots = new ArrayList();
        IntStream.range(0, parkingLotCapacity)
                .filter(slot -> this.vehicleList.get(slot).getVehicle() == null)
                .forEach(emptySlots::add);
        if (emptySlots.size() == 0) {
            for (IParkingLotObserver observer : parkingLotObservers) {
                observer.parkingFull();
            }
        }
        return emptySlots;
    }

    public int getParkingSlot(Vehicle vehicle) throws ParkingLotException {
        ArrayList<Integer> emptySlotList = getSlotList();
        for (int slot = 0; slot < emptySlotList.size(); slot++) {
            if (emptySlotList.get(0) == (slot)) {
                return slot;
            }
        }
        throw new ParkingLotException("Parking lot is full", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public int findVehicleLot(Vehicle vehicle) throws ParkingLotException {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        if (this.vehicleList.contains(parkingSlot))
            return this.vehicleList.indexOf(parkingSlot) + 1;
        throw new ParkingLotException("Vehicle not found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public boolean isTimeSet(Vehicle vehicle) {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        for (int i = 0; i < this.vehicleList.size(); i++) {
            if (this.vehicleList.get(i).time != null && this.vehicleList.contains(parkingSlot))
                return true;
        }
        return false;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public List<Integer> findOnFieldColor(String color) {
        List<Integer> fieldList = this.vehicleList
                .stream()
                .filter(parkingSlot -> parkingSlot.getVehicle() != null)
                .filter(parkingSlot -> parkingSlot.getVehicle()
                        .getColor()
                        .equalsIgnoreCase(color))
                .map(ParkingSlot::getSlot)
                .collect(Collectors.toList());
        return fieldList;
    }

    public List<String> findOnTwoFields(String color, String modelName) {
        List<String> fieldList1 = this.vehicleList
                .stream()
                .filter(parkingSlot -> parkingSlot.getVehicle() != null)
                .filter(parkingSlot -> parkingSlot.getVehicle().getModelName().equalsIgnoreCase(modelName))
                .filter(parkingSlot -> parkingSlot.getVehicle().getColor().equalsIgnoreCase(color))
                .map(parkingSlot -> (parkingSlot.getAttendantName()) + "  " + (parkingSlot.getSlot()))
                .collect(Collectors.toList());
        return fieldList1;
    }

    public List<Integer> findOnFieldModelName(String modelName) {
        List<Integer> fieldList = this.vehicleList.stream()
                .filter(parkingSlot -> parkingSlot.getVehicle() != null)
                .filter(parkingSlot -> parkingSlot.getVehicle()
                        .getModelName()
                        .equalsIgnoreCase(modelName))
                .map(ParkingSlot::getSlot)
                .collect(Collectors.toList());
        return fieldList;
    }

    public List<String> getVehiclesWhichIsParkedFrom30Min() {
        List<String> parkingLotList = this.vehicleList
                .stream()
                .filter(parkingSlot -> parkingSlot.getVehicle() != null)
                .filter(parkingSlot -> parkingSlot.getTime().getMinute() - LocalDateTime.now().getMinute() <= 30)
                .map(parkingSlot -> ((parkingSlot.getSlot())) + " " + (parkingSlot.getVehicle().getModelName()) + " " + (parkingSlot.getVehicle().getColor()))
                .collect(Collectors.toList());
        return parkingLotList;
    }
}
