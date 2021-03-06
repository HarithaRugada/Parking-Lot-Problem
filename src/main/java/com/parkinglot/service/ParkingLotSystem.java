package com.parkinglot.service;

import com.parkinglot.enums.DriverType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotStrategy;
import com.parkinglot.model.Vehicle;
import com.parkinglot.utility.ParkingLotStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingLotSystem {
    private int lotCapacity;
    private List<ParkingLot> parkingLots;

    public ParkingLotSystem(int lotCapacity) {
        this.lotCapacity = lotCapacity;
        this.parkingLots = new ArrayList<>();
    }

    public void addLot(ParkingLot parkingLot) {
        this.parkingLots.add(parkingLot);
    }

    public boolean isLotAdded(ParkingLot parkingLot) {
        return this.parkingLots.contains(parkingLot);
    }

    public void parkVehicle(Vehicle vehicle, DriverType driverType, VehicleType vehicleType, String attendantName) throws ParkingLotException {
        IParkingLotStrategy parkingLotStrategy = ParkingLotStrategy.getStrategy(driverType, vehicleType);
        ParkingLot lot = parkingLotStrategy.getParkingLot(this.parkingLots);
        lot.parkVehicle(vehicle, driverType, vehicleType, attendantName);
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        for (ParkingLot parkingLot : this.parkingLots) {
            if (parkingLot.isVehicleParked(vehicle)) {
                return true;
            }
        }
        return false;
    }

    public void unParkVehicle(Vehicle vehicle) throws ParkingLotException {
        for (ParkingLot lot : this.parkingLots) {
            lot.unParkVehicle(vehicle);
        }
        throw new ParkingLotException("Vehicle Not Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
    }

    public List<List<String>> findVehicleByColor(String color) {
        List<List<String>> parkingLotsList = this.parkingLots
                .stream()
                .map(lot -> lot.findOnFieldColor(color))
                .collect(Collectors.toList());
        return parkingLotsList;
    }

    public List<List<String>> findVehicleByTwoFields(String color, String modelName) {
        List<List<String>> parkingLotsList = new ArrayList<>();
        for (ParkingLot list : this.parkingLots) {
            List<String> onField = list.findOnTwoFields(color, modelName);
            parkingLotsList.add(onField);
        }
        return parkingLotsList;
    }

    public List<List<String>> findVehicleByModelName(String modelName) {
        List<List<String>> parkingLotsList = this.parkingLots
                .stream()
                .map(lot -> lot.findOnFieldModelName(modelName))
                .collect(Collectors.toList());
        return parkingLotsList;
    }

    public List<List<String>> findVehicleParkedLast30Minutes() {
        List<List<String>> parkingLotsList = new ArrayList<>();
        for (ParkingLot list : this.parkingLots) {
            List<String> onField = list.getVehiclesWhichIsParkedFrom30Min();
            parkingLotsList.add(onField);
        }
        return parkingLotsList;
    }

    public List<List<String>> findVehiclesBySpecifiedType(DriverType type) {
        List<List<String>> parkingLotsList = new ArrayList<>();
        for (ParkingLot list : this.parkingLots) {
            List<String> onField = list.getVehiclesListOfSpecifiedType(type);
            parkingLotsList.add(onField);
        }
        return parkingLotsList;
    }

    public List<List<String>> findAllVehicles() {
        List<List<String>> parkingLotsList = new ArrayList<>();
        for (ParkingLot list : this.parkingLots) {
            List<String> onField = list.getCompleteVehiclesList();
            parkingLotsList.add(onField);
        }
        return parkingLotsList;
    }
}