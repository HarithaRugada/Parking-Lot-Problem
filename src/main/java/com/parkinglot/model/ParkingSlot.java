package com.parkinglot.model;

import com.parkinglot.enums.DriverType;
import com.parkinglot.enums.VehicleType;

import java.time.LocalDateTime;

public class ParkingSlot {
    private int slot;
    private DriverType driverType;
    private VehicleType vehicleType;
    public LocalDateTime time;
    public Vehicle vehicle;
    public String attendantName;

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ParkingSlot(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now();
    }

    public ParkingSlot(Vehicle vehicle, DriverType driverType, VehicleType vehicleType, String attendantName) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now();
        this.driverType = driverType;
        this.vehicleType = vehicleType;
        this.attendantName = attendantName;
    }

    public ParkingSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getAttendantName() {
        return attendantName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return vehicle.equals(that.vehicle);
    }
}