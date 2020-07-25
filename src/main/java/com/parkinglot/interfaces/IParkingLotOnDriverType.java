package com.parkinglot.interfaces;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.service.ParkingLot;

import java.util.List;

public interface IParkingLotOnDriverType {

    ParkingLot getParkingLot(List<ParkingLot> parkingLots) throws ParkingLotException;
}
