package com.parkinglot.test;

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
        vehicle= new Object();
    }
    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        boolean isParked = parkingLot.parkVehicle(vehicle);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() {
        parkingLot.parkVehicle(vehicle);
        boolean isUnParked = parkingLot.unParkVehicle(vehicle);
        Assert.assertTrue(isUnParked);
    }
}
