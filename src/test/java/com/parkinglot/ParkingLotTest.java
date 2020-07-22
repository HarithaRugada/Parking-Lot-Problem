package com.parkinglot;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.observers.AirportSecurity;
import com.parkinglot.observers.ParkingOwner;
import com.parkinglot.service.ParkingLot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingLotTest {
    ParkingLot parkingLot = null;
    Object vehicle = null;
    ParkingOwner parkingOwner = null;
    AirportSecurity security = null;

    @Before
    public void setUp() {
        parkingLot = new ParkingLot(2);
        vehicle = new Object();
        parkingOwner = new ParkingOwner();
        security = new AirportSecurity();
        parkingLot.initializeParkingLot();
    }

    //TC-1
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
            Assert.assertEquals("Already Parked", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void given2Vehicles_WhenParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.setCapacity(5);
        parkingLot.initializeParkingLot();
        parkingLot.parkVehicle(vehicle);
        boolean isParked1 = parkingLot.isVehicleParked(vehicle);
        Object vehicle2 = new Object();
        parkingLot.parkVehicle(vehicle2);
        boolean isParked2 = parkingLot.isVehicleParked(vehicle2);
        Assert.assertTrue(isParked1 && isParked2);
    }

    //TC-2
    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnFalse() throws ParkingLotException {
        parkingLot.setCapacity(4);
        parkingLot.initializeParkingLot();
        parkingLot.parkVehicle(vehicle);
        parkingLot.parkVehicle(new Object());
        parkingLot.unParkVehicle(vehicle);
        boolean isParked = parkingLot.isVehicleParked(vehicle);
        Assert.assertFalse(isParked);
    }

    @Test
    public void givenVehicleParked_AndWhenUnParkedAnotherVehicle_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.unParkVehicle(new Object());
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

    //TC-3
    @Test
    public void givenVehicle_WhenParkingLotFull_ShouldInformToOwner() {
        parkingLot.registerParkingLotObserver(parkingOwner);
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking lot is full", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-4
    @Test
    public void givenVehicle_WhenParkingLotFull_ShouldInformToSecurity() {
        parkingLot.registerParkingLotObserver(security);
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking lot is full", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-5
    @Test
    public void givenVehicle_WhenParkingAvailable_ShouldInformToOwner() {
        parkingLot.setCapacity(5);
        parkingLot.initializeParkingLot();
        parkingLot.registerParkingLotObserver(parkingOwner);
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
            parkingLot.unParkVehicle(vehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking lot is available", e.getMessage());
            System.out.println(e.getMessage());
        }
        boolean parkingAvailable = parkingOwner.isParkingAvailable();
        Assert.assertFalse(parkingAvailable);
    }

    @Test
    public void givenVehicle_WhenParkingAvailable_ShouldInformToSecurity() {
        parkingLot.setCapacity(5);
        parkingLot.initializeParkingLot();
        parkingLot.registerParkingLotObserver(security);
        try {
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
            parkingLot.unParkVehicle(vehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking lot is available", e.getMessage());
            System.out.println(e.getMessage());
        }
        boolean parkingAvailable = security.isParkingAvailable();
        Assert.assertFalse(parkingAvailable);
    }

    //TC-6
    @Test
    public void givenParkingLot_WhenParkedWithProvidedSlot_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLot.parkVehicle(vehicle);
        boolean vehiclePark = parkingLot.isVehicleParked(vehicle);
        Assert.assertTrue(vehiclePark);
    }

    //TC-7
    @Test
    public void givenParkingLot_WhenVehicleFound_ShouldReturnVehicleSlot() throws ParkingLotException {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLot.parkVehicle(new Object());
        parkingLot.parkVehicle(vehicle);
        int slotNumber = parkingLot.findVehicle(this.vehicle);
        Assert.assertEquals(1, slotNumber);
    }

    //TC-8
    @Test
    public void givenVehicleForParking_WhenVehicleParkedTimeIsSet_ShouldReturnParkingTime() throws ParkingLotException {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLot.parkVehicle(vehicle);
        String parkingTime = parkingLot.getParkingTime(vehicle);
        Assert.assertEquals(parkingTime, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
    }
}