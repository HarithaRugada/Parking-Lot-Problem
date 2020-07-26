package com.parkinglot.utility;

import com.parkinglot.enums.DriverType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.interfaces.IParkingLotStrategy;

public class CheckType {
    public static IParkingLotStrategy typeImplementation(Enum type) {
        if (type.equals(DriverType.HANDICAP)) {
            return new HandicapDriver();
        } else if (type.equals(VehicleType.LARGE)) {
            return new LargeVehicle();
        }
        return new NormalDriver();
    }
}
