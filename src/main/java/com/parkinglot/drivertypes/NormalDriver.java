package com.parkinglot.drivertypes;

import com.parkinglot.interfaces.IParkingLotOnDriverType;
import com.parkinglot.service.ParkingLot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public enum NormalDriver implements IParkingLotOnDriverType {
    NORMAL;

    @Override
    public ParkingLot getParkingLot(List<ParkingLot> parkingLots) {
        List<ParkingLot> parkingLotsList = new ArrayList<>(parkingLots);
        parkingLotsList.sort(Comparator.comparing(list -> list.getSlotList().size(), Comparator.reverseOrder()));
        return parkingLotsList.get(0);
    }
}
