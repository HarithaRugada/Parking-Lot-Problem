package com.parkinglot.model;

import java.time.LocalDateTime;

public class ParkingSlot {
    //protected LocalDateTime time;
    public Object vehicle;

    public ParkingSlot(Object vehicle) {
        this.vehicle = vehicle;
        //this.time = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return vehicle.equals(that.vehicle);
    }
}
