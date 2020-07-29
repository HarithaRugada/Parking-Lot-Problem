package com.parkinglot.utility;

import com.parkinglot.enums.DriverType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.interfaces.IParkingLotStrategy;

public class ParkingLotStrategy {
    public static IParkingLotStrategy getStrategy(DriverType driverType,VehicleType vehicleType) {
        if (driverType.equals(DriverType.HANDICAP)) {
            return new HandicapDriverStrategy();
        } else if (vehicleType.equals(VehicleType.LARGE)) {
            return new LargeVehicleStrategy();
        }
        return new NormalDriverStrategy();
    }
}