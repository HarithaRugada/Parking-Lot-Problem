package com.parkinglot.test;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.service.ParkingLot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotTest {
    ParkingLot parkingLot = null;
    Object vehicle = null;

    @Before
    public void setUp() {
        parkingLot = new ParkingLot();
        vehicle = new Object();
    }

    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() throws ParkingLotException {
        boolean isParked = parkingLot.parkVehicle(vehicle);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.parkVehicle(vehicle);
        boolean isUnParked = parkingLot.unParkVehicle(vehicle);
        Assert.assertTrue(isUnParked);
    }

    @Test
    public void givenParkingLot_WhenParkingLotGetFull_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle);
            Object vehicle2 = new Object();
            parkingLot.parkVehicle(vehicle2);
            parkingLot.parkVehicle(new Object());
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking lot is full", e.getMessage());
        }
    }
}
