package com.parkinglot.model;

import java.time.LocalDateTime;

public class ParkingSlot {
    private int slot;
    private Enum type;
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

    public ParkingSlot(Vehicle vehicle, Enum type, String attendantName) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now();
        this.type = type;
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

    public Enum getType(){
        return type;
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
