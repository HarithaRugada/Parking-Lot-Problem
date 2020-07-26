package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotStrategy;
import com.parkinglot.utility.CheckType;

import java.util.ArrayList;
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

    public void parkVehicle(Object vehicle, Enum type) throws ParkingLotException {
        IParkingLotStrategy parkingLotStrategy = CheckType.typeImplementation(type);
        ParkingLot lot = parkingLotStrategy.getParkingLot(this.parkingLots);
        lot.parkVehicle(vehicle, type);
    }

    public boolean isVehicleParked(Object vehicle) {
        for (ParkingLot parkingLot : this.parkingLots) {
            if (parkingLot.isVehicleParked(vehicle)) {
                return true;
            }
        }
        return false;
    }

    public void unParkVehicle(Object vehicle) throws ParkingLotException {
        for (ParkingLot lot : this.parkingLots) {
            lot.unParkVehicle(vehicle);
        }
    }
}
