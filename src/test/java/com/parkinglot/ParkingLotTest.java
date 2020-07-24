package com.parkinglot;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.observers.AirportSecurity;
import com.parkinglot.observers.ParkingOwner;
import com.parkinglot.service.LotParking;
import com.parkinglot.service.ParkingLot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingLotTest {
    ParkingLot parkingLot = null;
    LotParking lotParking = null;
    Object vehicle = null;
    ParkingOwner parkingOwner = null;
    AirportSecurity security = null;

    @Before
    public void setUp() {
        parkingLot = new ParkingLot(2);
        lotParking = new LotParking(5);
        vehicle = new Object();
        parkingOwner = new ParkingOwner();
        security = new AirportSecurity();
        parkingLot.initializeParkingLot();
    }

    //TC-1
    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        try {
            parkingLot.parkVehicle(vehicle);
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertTrue(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
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
    public void given2Vehicles_WhenParked_ShouldReturnTrue() {
        parkingLot.setCapacity(5);
        parkingLot.initializeParkingLot();
        try {
            parkingLot.parkVehicle(vehicle);
            boolean isParked1 = parkingLot.isVehicleParked(vehicle);
            Object vehicle2 = new Object();
            parkingLot.parkVehicle(vehicle2);
            boolean isParked2 = parkingLot.isVehicleParked(vehicle2);
            Assert.assertTrue(isParked1 && isParked2);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-2
    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnFalse() {
        try {
            parkingLot.setCapacity(4);
            parkingLot.initializeParkingLot();
            parkingLot.parkVehicle(vehicle);
            parkingLot.parkVehicle(new Object());
            parkingLot.unParkVehicle(vehicle);
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertFalse(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
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
    public void givenParkingLot_WhenParkedWithProvidedSlot_ShouldReturnTrue() {
        try {
            parkingLot.setCapacity(10);
            parkingLot.initializeParkingLot();
            parkingLot.parkVehicle(vehicle);
            boolean vehiclePark = parkingLot.isVehicleParked(vehicle);
            Assert.assertTrue(vehiclePark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-7
    @Test
    public void givenParkingLot_WhenVehicleFound_ShouldReturnVehicleSlot() {
        try {
            parkingLot.setCapacity(10);
            parkingLot.initializeParkingLot();
            parkingLot.parkVehicle(new Object());
            parkingLot.parkVehicle(vehicle);
            int slotNumber = parkingLot.findVehicle(this.vehicle);
            Assert.assertEquals(1, slotNumber);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-8
    @Test
    public void givenParkingLot_WhenVehicleParkedIfTimeIsSet_ShouldReturnTrue() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        try {
            parkingLot.parkVehicle(vehicle);
            boolean isTimeSet = parkingLot.isTimeSet(vehicle);
            Assert.assertTrue(isTimeSet);
            Assert.assertEquals(new ParkingSlot(vehicle).time, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-9
    @Test
    public void givenLotParking_WhenAddedLots_ShouldReturnTrue() {
        ParkingLot parkingLot2 = new ParkingLot(5);
        lotParking.addLot(parkingLot);
        lotParking.addLot(parkingLot2);
        Assert.assertTrue(lotParking.isLotAdded(parkingLot) && lotParking.isLotAdded(parkingLot2));
    }

    @Test
    public void givenLotParking_WhenVehicleParkedOnLot_ShouldReturnTrue() {
        lotParking.addLot(parkingLot);
        parkingLot.setCapacity(1);
        parkingLot.initializeParkingLot();
        try {
            lotParking.parkVehicle(vehicle);
            boolean isVehiclePark = lotParking.isVehicleParked(vehicle);
            Assert.assertTrue(isVehiclePark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenLotParking_ShouldParkTheVehicles_InEvenDistribution() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        lotParking.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.setCapacity(10);
        parkingLot2.initializeParkingLot();
        Object vehicle2 = new Object();
        Object vehicle3 = new Object();
        Object vehicle4 = new Object();
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle);
            boolean isVehiclePark1 = lotParking.isVehicleParked(vehicle);
            lotParking.parkVehicle(vehicle2);
            boolean isVehiclePark2 = lotParking.isVehicleParked(vehicle2);
            lotParking.parkVehicle(vehicle3);
            boolean isVehiclePark3 = lotParking.isVehicleParked(vehicle3);
            lotParking.parkVehicle(vehicle4);
            boolean isVehiclePark4 = lotParking.isVehicleParked(vehicle4);
            Assert.assertTrue(isVehiclePark1 && isVehiclePark2 && isVehiclePark3 && isVehiclePark4);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLotSystem_WhenVehicleUnParked_ShouldReturnTrue() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        lotParking.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.setCapacity(10);
        parkingLot2.initializeParkingLot();
        Object vehicle2 = new Object();
        Object vehicle3 = new Object();
        Object vehicle4 = new Object();
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle);
            lotParking.parkVehicle(vehicle2);
            lotParking.parkVehicle(vehicle3);
            lotParking.parkVehicle(vehicle4);
            boolean isVehicleUnParked = lotParking.unParkVehicle(vehicle);
            Assert.assertTrue(isVehicleUnParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
}