package com.parkinglot.utility;

import com.parkinglot.service.ParkingLot;
import com.parkinglot.interfaces.IParkingLotStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LargeVehicle implements IParkingLotStrategy {
    @Override
    public ParkingLot getParkingLot(List<ParkingLot> parkingLots) {
        List<ParkingLot> parkingLotsList = new ArrayList<>(parkingLots);
        parkingLotsList.sort(Comparator.comparing(ParkingLot::getVehicleCount));
        return parkingLotsList.get(0);
    }
}
