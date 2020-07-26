package com.parkinglot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSlot {
    private Enum type;
    public String time;
    public Vehicle vehicle;

    public ParkingSlot(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"));
    }

    public ParkingSlot(Vehicle vehicle,Enum type) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return vehicle.equals(that.vehicle);
    }
}
