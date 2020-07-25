package com.parkinglot.service;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotOnDriverType;

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

    public void parkVehicle(Object vehicle, IParkingLotOnDriverType driverType) throws ParkingLotException {
        ParkingLot lot = driverType.getParkingLot(this.parkingLots);
        lot.parkVehicle(vehicle, driverType);
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
        for (ParkingLot parkingLot : this.parkingLots) {
            if (parkingLot.isVehicleParked(vehicle)) {
                return true;
            }
        }
        return false;
    }

    public ParkingLot findVehicleLot(Object vehicle) throws ParkingLotException {
        return this.parkingLots
                .stream()
                .filter(parkingLot -> parkingLot.isVehicleParked(vehicle))
                .findFirst()
                .orElseThrow(() -> new ParkingLotException("Vehicle not found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND));
    }
}
