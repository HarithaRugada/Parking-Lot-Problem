package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LotParking {
    private int lotCapacity;
    private List<ParkingLot> parkingLots;

    public LotParking(int lotCapacity) {
        this.lotCapacity = lotCapacity;
        this.parkingLots = new ArrayList<>();
    }

    public void addLot(ParkingLot parkingLot) {
        this.parkingLots.add(parkingLot);
    }

    public boolean isLotAdded(ParkingLot parkingLot) {
        return this.parkingLots.contains(parkingLot);
    }

    public void parkVehicle(Object vehicle) throws ParkingLotException {
        List<ParkingLot> parkingLotsList = this.parkingLots;
        parkingLotsList.sort(Comparator.comparing(list -> list.getSlotList().size(), Comparator.reverseOrder()));
        ParkingLot lot = parkingLotsList.get(0);
        lot.parkVehicle(vehicle);
    }

    public boolean unParkVehicle(Object vehicle) throws ParkingLotException {
        for (ParkingLot lot : this.parkingLots) {
            if (lot.unParkVehicle(vehicle)) {
                return true;
            }
        }
        throw new ParkingLotException("Vehicle not found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.parkingLots.get(0).isVehicleParked(vehicle);
    }
}
