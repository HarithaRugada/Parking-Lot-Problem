package com.parkinglot.utility;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.service.ParkingLot;
import com.parkinglot.interfaces.IParkingLotStrategy;

import java.util.List;

public class HandicapDriver implements IParkingLotStrategy {

    @Override
    public ParkingLot getParkingLot(List<ParkingLot> parkingLots) throws ParkingLotException {
        ParkingLot parkingLot1 = parkingLots.stream()
                .filter(parkingLot -> parkingLot.getSlotList().size() > 0)
                .findFirst()
                .orElseThrow(() -> new ParkingLotException("Parking is full", ParkingLotException.ExceptionType.PARKING_FULL));
        return parkingLot1;
    }
}
