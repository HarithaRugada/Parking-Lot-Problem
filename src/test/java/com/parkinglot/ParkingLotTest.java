package com.parkinglot;

import com.parkinglot.enums.DriverType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Vehicle;
import com.parkinglot.observers.AirportSecurity;
import com.parkinglot.observers.ParkingOwner;
import com.parkinglot.utility.ParkingLotStrategy;
import com.parkinglot.service.ParkingLotSystem;
import com.parkinglot.service.ParkingLot;
import com.parkinglot.interfaces.IParkingLotStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotTest {
    ParkingLot parkingLot = null;
    ParkingLotSystem parkingLotSystem = null;
    Vehicle vehicle = null;
    ParkingOwner parkingOwner = null;
    AirportSecurity security = null;

    @Before
    public void setUp() {
        parkingLot = new ParkingLot(2);
        parkingLotSystem = new ParkingLotSystem(5);
        vehicle = new Vehicle("black", "Audi", "AP 31 AQ 1234");
        parkingOwner = new ParkingOwner();
        security = new AirportSecurity();
        parkingLot.initializeParkingLot();
    }

    //TC-1
    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertTrue(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenVehicle_WhenParkedTwice_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isParked1 = parkingLot.isVehicleParked(vehicle);
            Vehicle secondVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
            parkingLot.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isParked2 = parkingLot.isVehicleParked(secondVehicle);
            Assert.assertTrue(isParked1 && isParked2);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-2
    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() {
        try {
            parkingLot.setCapacity(4);
            parkingLot.initializeParkingLot();
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.unParkVehicle(vehicle);
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertTrue(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenVehicleParked_AndWhenUnParkedAnotherVehicle_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.unParkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"));
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_CheckIfVehicleIsNotPresent_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isParked = parkingLot.isVehicleParked(new Vehicle("black", "toyota", "AP 31 AQ 1234"));
            Assert.assertFalse(isParked);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-3
    @Test
    public void givenVehicle_WhenParkingLotFull_ShouldInformToOwner() {
        parkingLot.setCapacity(3);
        parkingLot.initializeParkingLot();
        parkingLot.registerParkingLotObserver(parkingOwner);
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "toyota", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("black", "audi", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
        } catch (ParkingLotException e) {
            Assert.assertEquals("Parking lot is full", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-4
    @Test
    public void givenVehicle_WhenParkingLotFull_ShouldInformToSecurity() {
        parkingLot.setCapacity(2);
        parkingLot.initializeParkingLot();
        parkingLot.registerParkingLotObserver(security);
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
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
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            int slotNumber = parkingLot.findVehicleLot(vehicle);
            Assert.assertEquals(2, slotNumber);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_WhenVehicleNotFound_ShouldThrowException() {
        try {
            parkingLot.setCapacity(10);
            parkingLot.initializeParkingLot();
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.findVehicleLot(new Vehicle("black", "audi", "AP 31 AQ 1234"));
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-8
    @Test
    public void givenParkingLot_WhenVehicleParked_IfTimeIsSet_ShouldReturnTrue() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isTimeSet = parkingLot.isTimeSet(vehicle);
            Assert.assertTrue(isTimeSet);
            Assert.assertEquals(new ParkingSlot(vehicle).time, LocalDateTime.now());
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-9
    @Test
    public void givenLotParking_WhenLotsAdded_ShouldReturnTrue() {
        ParkingLot parkingLot2 = new ParkingLot(5);
        parkingLotSystem.addLot(parkingLot);
        parkingLotSystem.addLot(parkingLot2);
        Assert.assertTrue(parkingLotSystem.isLotAdded(parkingLot) && parkingLotSystem.isLotAdded(parkingLot2));
    }

    @Test
    public void givenLotParking_WhenLotsNotAdded_ShouldReturnFalse() {
        ParkingLot parkingLot2 = new ParkingLot(5);
        parkingLotSystem.addLot(parkingLot);
        Assert.assertFalse(parkingLotSystem.isLotAdded(parkingLot2));
    }

    @Test
    public void givenLotParking_WhenVehicleParkedOnLot_ShouldReturnTrue() {
        parkingLotSystem.addLot(parkingLot);
        parkingLot.setCapacity(1);
        parkingLot.initializeParkingLot();
        try {
            parkingLotSystem.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isVehiclePark = parkingLotSystem.isVehicleParked(vehicle);
            Assert.assertTrue(isVehiclePark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLotSystem_WhenVehicleNotParkedOnLot_ShouldReturnFalse() {
        parkingLotSystem.addLot(parkingLot);
        parkingLot.setCapacity(1);
        parkingLot.initializeParkingLot();
        try {
            parkingLotSystem.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isVehicleParked = parkingLotSystem.isVehicleParked(new Vehicle("red", "audi", "AP 31 AQ 1234"));
            Assert.assertFalse(isVehicleParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenLotParking_ShouldParkTheVehicles_InEvenDistribution() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.setCapacity(10);
        parkingLot2.initializeParkingLot();
        Vehicle secondVehicle = new Vehicle("yellow", "toyota", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("black", "audi", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("red", "toyota", "AP 31 AQ 1234");
        parkingLotSystem.addLot(parkingLot2);
        try {
            parkingLotSystem.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isVehiclePark1 = parkingLotSystem.isVehicleParked(vehicle);
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isVehiclePark2 = parkingLotSystem.isVehicleParked(secondVehicle);
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isVehiclePark3 = parkingLotSystem.isVehicleParked(thirdVehicle);
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            boolean isVehiclePark4 = parkingLotSystem.isVehicleParked(fourthVehicle);
            Assert.assertTrue(isVehiclePark1 && isVehiclePark2 && isVehiclePark3 && isVehiclePark4);
            int lotNumber = parkingLot.findVehicleLot(vehicle);
            int slotNumber = parkingLot.getParkingSlot(vehicle);
            Assert.assertEquals(2, slotNumber);
            Assert.assertEquals(1, lotNumber);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenLotParking_WhenUnParkVehicleNotAvailable_ShouldThrowException() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        Vehicle secondVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.unParkVehicle(fourthVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle Not Found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenParkingLotSystem_WhenVehicleUnParked_ShouldReturnTrue() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.setCapacity(10);
        parkingLot2.initializeParkingLot();
        Vehicle secondVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        parkingLotSystem.addLot(parkingLot2);
        try {
            parkingLotSystem.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.unParkVehicle(thirdVehicle);
            boolean isVehicleParked = parkingLotSystem.isVehicleParked(thirdVehicle);
            Assert.assertFalse(isVehicleParked);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle Not Found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-10
    @Test
    public void givenParkingLotSystem_WhenDriverTypeIsHandicap_ShouldGiveNearestLot() {
        IParkingLotStrategy parkingLotStrategy = ParkingLotStrategy.getStrategy(DriverType.HANDICAP, VehicleType.SMALL);
        List<ParkingLot> parkingLots1 = new ArrayList<>();
        ParkingLot parkingLot12 = new ParkingLot(1);
        parkingLot12.setCapacity(10);
        parkingLot12.initializeParkingLot();
        parkingLots1.add(parkingLot12);
        try {
            parkingLot = parkingLotStrategy.getParkingLot(parkingLots1);
            Assert.assertEquals(parkingLot12, parkingLot);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLotSystem_WhenDriverTypeIsNormal_ShouldReturnTheFirstEmptySlot() {
        IParkingLotStrategy parkingLotStrategy = ParkingLotStrategy.getStrategy(DriverType.NORMAL, VehicleType.SMALL);
        List<ParkingLot> parkingLots1 = new ArrayList<>();
        ParkingLot parkingLot12 = new ParkingLot(1);
        parkingLot12.setCapacity(10);
        parkingLot12.initializeParkingLot();
        parkingLots1.add(parkingLot12);
        try {
            parkingLot = parkingLotStrategy.getParkingLot(parkingLots1);
            Assert.assertEquals(parkingLot12, parkingLot);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-11
    @Test
    public void givenParkingLotSystem_WhenVehicleTypeIsLarge_ShouldReturnTrue() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot3);
        Vehicle secondVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        Vehicle sixthVehicle = new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(vehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(new Vehicle("blue", "Chevrolet", "AP 31 AQ 1234"), DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fifthVehicle, DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(sixthVehicle, DriverType.NORMAL, VehicleType.LARGE, "XYZ");
            boolean vehiclePark = parkingLotSystem.isVehicleParked(sixthVehicle);
            Assert.assertTrue(vehiclePark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-12
    @Test
    public void givenParkingLotSystem_WhenParkedVehicleColorIsWhite_ShouldReturnTheListOfParkedSlots() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot3);
        Vehicle firstVehicle = new Vehicle("white", "audi", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("black", "chevrolet", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "Kia", "AP 31 AQ 1234");
        Vehicle sixthVehicle = new Vehicle("green", "lexus", "AP 31 AQ 1234");
        Vehicle seventhVehicle = new Vehicle("white", "honda", "AP 31 AQ 1234");
        Vehicle eighthVehicle = new Vehicle("white", "nissan", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fifthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(sixthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(seventhVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(eighthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            List whiteCarList = parkingLotSystem.findVehicleByColor("white");
            System.out.println(whiteCarList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLotSystem_WhenParkedVehicleColorIsWhite_ShouldReturnList() {
        parkingLot.setCapacity(3);
        parkingLot.initializeParkingLot();
        Vehicle firstVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("black", "chevrolet", "AP 31 AQ 1234");
        try {
            parkingLot.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLot.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            List<String> onField = parkingLot.findOnFieldColor("white");
            System.out.println(onField);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-13
    @Test
    public void givenParkingLotSystem_WhenParkedVehicleColorIsBlue_ShouldListParkedSlots() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot3);
        Vehicle firstVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle sixthVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle seventhVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle eighthVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fifthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(sixthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(seventhVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(eighthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            List<List<String>> blueToyotaList = parkingLotSystem.findVehicleByTwoFields("blue", "toyota");
            System.out.println(blueToyotaList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_WhenParkedBlueToyotaCar_ShouldReturnLocationAndAttendantNameAndPlateNumber() {
        parkingLot.setCapacity(20);
        parkingLot.initializeParkingLot();
        Vehicle firstVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle vehicle6 = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        try {
            parkingLot.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "asb");
            parkingLot.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "pqr");
            parkingLot.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(fifthVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(vehicle6, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            List<String> blueToyotaList = parkingLot.findOnTwoFields("blue", "toyota");
            System.out.println(blueToyotaList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-14
    @Test
    public void givenParkingLotSystem_WhenParkedBMWVehicle_ShouldListParkedSlots() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot3);

        Vehicle firstVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle vehicle6 = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle seventhVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle eighthVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fifthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(vehicle6, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(seventhVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(eighthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            List<List<String>> bmwList = parkingLotSystem.findVehicleByModelName("BMW");
            System.out.println(bmwList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_WhenParkedBMWVehicle_ShouldReturnLocation() {
        parkingLot.setCapacity(20);
        parkingLot.initializeParkingLot();
        Vehicle firstVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle sixthVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        try {
            parkingLot.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "asb");
            parkingLot.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "pqr");
            parkingLot.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(fifthVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(sixthVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            List<String> bmwList = parkingLot.findOnFieldModelName("BMW");
            System.out.println(bmwList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-15
    @Test
    public void givenParkingLotSystem_ShouldReturnAllParkingListBefore30Min() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot3);
        Vehicle firstVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("silver", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle sixthVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle seventhVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle eighthVehicle = new Vehicle("red", "toyota", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fifthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(sixthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(seventhVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(eighthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            List<List<String>> vehicleList = parkingLotSystem.findVehicleParkedLast30Minutes();
            System.out.println(vehicleList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_ShouldReturnAllParkingListBefore30Min() {
        parkingLot.setCapacity(20);
        parkingLot.initializeParkingLot();
        Vehicle firstVehicle = new Vehicle("black", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("red", "audi", "AP 31 AQ 1234");
        try {
            parkingLot.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "abc");
            parkingLot.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "pqr");
            List<String> vehicleList = parkingLot.getVehiclesWhichIsParkedFrom30Min();
            System.out.println(vehicleList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-16
    @Test
    public void givenParkingLotSystem_WhenAskedForHandicapCars_ShouldReturnTheList() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot3);
        Vehicle firstVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("silver", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle sixthVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle seventhVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle eighthVehicle = new Vehicle("red", "toyota", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fifthVehicle, DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(sixthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(seventhVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(eighthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            List<List<String>> vehicleList = parkingLotSystem.findVehiclesBySpecifiedType(DriverType.HANDICAP);
            System.out.println(vehicleList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_WhenAskedForHandicapCars_ShouldReturnTheList() {
        parkingLot.setCapacity(20);
        parkingLot.initializeParkingLot();
        Vehicle firstVehicle = new Vehicle("black", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("red", "audi", "AP 31 AQ 1234");
        try {
            parkingLot.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "abc");
            parkingLot.parkVehicle(secondVehicle, DriverType.HANDICAP, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "pqr");
            List<String> vehicleList = parkingLot.getVehiclesListOfSpecifiedType(DriverType.HANDICAP);
            System.out.println(vehicleList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-17
    @Test
    public void givenParkingLotSystem_ShouldReturnAllTheVehiclesList() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        parkingLotSystem.addLot(parkingLot3);
        Vehicle firstVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("blue", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("silver", "toyota", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        Vehicle fifthVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle sixthVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle seventhVehicle = new Vehicle("blue", "toyota", "AP 31 AQ 1234");
        Vehicle eighthVehicle = new Vehicle("red", "toyota", "AP 31 AQ 1234");
        try {
            parkingLotSystem.parkVehicle(firstVehicle, DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(secondVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(thirdVehicle, DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(fifthVehicle, DriverType.HANDICAP, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(sixthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(seventhVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            parkingLotSystem.parkVehicle(eighthVehicle, DriverType.NORMAL, VehicleType.SMALL, "XYZ");
            List<List<String>> vehicleList = parkingLotSystem.findAllVehicles();
            System.out.println(vehicleList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_ShouldReturnTheListOfAllVehicles() {
        parkingLot.setCapacity(20);
        parkingLot.initializeParkingLot();
        Vehicle firstVehicle = new Vehicle("black", "toyota", "AP 31 AQ 1234");
        Vehicle secondVehicle = new Vehicle("white", "BMW", "AP 31 AQ 1234");
        Vehicle thirdVehicle = new Vehicle("red", "audi", "AP 31 AQ 1234");
        Vehicle fourthVehicle = new Vehicle("white", "toyota", "AP 31 AQ 1234");
        try {
            parkingLot.parkVehicle(firstVehicle, DriverType.NORMAL, VehicleType.SMALL, "abc");
            parkingLot.parkVehicle(secondVehicle, DriverType.HANDICAP, VehicleType.SMALL, "xyz");
            parkingLot.parkVehicle(thirdVehicle, DriverType.NORMAL, VehicleType.SMALL, "pqr");
            parkingLot.parkVehicle(fourthVehicle, DriverType.NORMAL, VehicleType.SMALL, "def");
            List<String> vehicleList = parkingLot.getCompleteVehiclesList();
            System.out.println(vehicleList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
}