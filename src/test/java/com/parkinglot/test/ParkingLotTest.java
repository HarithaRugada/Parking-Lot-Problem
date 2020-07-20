package com.parkinglot.test;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.service.ParkingLot;
import com.parkinglot.utility.AirportSecurity;
import com.parkinglot.utility.ParkingOwner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingLotTest {
    ParkingLot parkingLot = null;
    Object vehicle = null;

    @Before
    public void setUp() {
        parkingLot = new ParkingLot(2);
        vehicle = new Object();
    }

    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.parkVehicle(vehicle);
        boolean isParked = parkingLot.isVehicleParked(vehicle);
        Assert.assertTrue(isParked);
    }

    @Test
    public void givenVehicle_WhenParkedTwice_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(vehicle);
        } catch (ParkingLotException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void given2Vehicles_WhenParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.parkVehicle(vehicle);
        boolean isParked1 = parkingLot.isVehicleParked(vehicle);
        Object vehicle2 = new Object();
        parkingLot.parkVehicle(vehicle2);
        boolean isParked2 = parkingLot.isVehicleParked(vehicle2);
        Assert.assertTrue(isParked1 && isParked2);
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.parkVehicle(vehicle);
        boolean isUnParked = parkingLot.unParkVehicle(vehicle);
        Assert.assertTrue(isUnParked);
    }

    @Test
    public void givenVehicleParked_AndWhenUnParkedAnotherVehicle_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle);
            boolean isUnParked = parkingLot.unParkVehicle(new Object());
            Assert.assertFalse(isUnParked);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_CheckIfVehicleIsNotPresent_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(null);
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertFalse(isParked);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_WhenParkingLotFull_ShouldInformToOwner() {
        ParkingOwner parkingOwner = new ParkingOwner();
        parkingLot.registerOwner(parkingOwner);
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
            parkingLot.parkVehicle(new Object());
            boolean parkingFull = parkingOwner.parkingFull();
            Assert.assertTrue(parkingFull);
        } catch (ParkingLotException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_WhenParkingLotFull_ShouldInformToSecurity() {
        AirportSecurity security = new AirportSecurity();
        parkingLot.registerSecurity(security);
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
            boolean parkingFull = security.parkingFull();
            Assert.assertTrue(parkingFull);
        } catch (ParkingLotException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_WhenParkingAvailable_ShouldInformToOwner() {
        ParkingOwner parkingOwner = new ParkingOwner();
        parkingLot.registerOwner(parkingOwner);
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
        } catch (ParkingLotException e) {
        }
        try {
            parkingLot.unParkVehicle(vehicle);
        } catch (ParkingLotException e) {
        }
        boolean parkingAvailable = parkingOwner.parkingAvailable();
        Assert.assertTrue(parkingAvailable);
    }
}
