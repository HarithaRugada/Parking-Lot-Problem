package com.parkinglot.drivertypes;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotOnDriverType;
import com.parkinglot.service.ParkingLot;

import java.util.List;

public enum HandicapDriver implements IParkingLotOnDriverType {
    HANDICAP;

    @Override
    public ParkingLot getParkingLot(List<ParkingLot> parkingLots) throws ParkingLotException {
        ParkingLot parkingLot1 = parkingLots.stream()
                .filter(parkingLot -> parkingLot.getSlotList().size() > 0)
                .findFirst()
                .orElseThrow(() -> new ParkingLotException("Parking is full", ParkingLotException.ExceptionType.PARKING_FULL));
        return parkingLot1;
    }
}
