package com.parkinglot;

import com.parkinglot.enums.DriverType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Vehicle;
import com.parkinglot.observers.AirportSecurity;
import com.parkinglot.observers.ParkingOwner;
import com.parkinglot.utility.CheckType;
import com.parkinglot.service.LotParking;
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
    LotParking lotParking = null;
    Vehicle vehicle = null;
    ParkingOwner parkingOwner = null;
    AirportSecurity security = null;

    @Before
    public void setUp() {
        parkingLot = new ParkingLot(2);
        lotParking = new LotParking(5);
        vehicle = new Vehicle("black", "Audi");
        parkingOwner = new ParkingOwner();
        security = new AirportSecurity();
        parkingLot.initializeParkingLot();
    }

    //TC-1
    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertTrue(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenVehicle_WhenParkedTwice_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            boolean isParked1 = parkingLot.isVehicleParked(vehicle);
            Vehicle vehicle2 = new Vehicle("blue", "Chevrolet");
            parkingLot.parkVehicle(vehicle2, DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.unParkVehicle(new Vehicle("blue", "Chevrolet"));
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_CheckIfVehicleIsNotPresent_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            boolean isParked = parkingLot.isVehicleParked(new Vehicle("black", "toyota"));
            Assert.assertFalse(isParked);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-3
    @Test
    public void givenVehicle_WhenParkingLotFull_ShouldInformToOwner() {
        parkingLot.setCapacity(2);
        parkingLot.initializeParkingLot();
        parkingLot.registerParkingLotObserver(parkingOwner);
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
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
            parkingLot.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            parkingLot.findVehicleLot(new Vehicle("black", "audi"));
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
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
        lotParking.addLot(parkingLot);
        lotParking.addLot(parkingLot2);
        Assert.assertTrue(lotParking.isLotAdded(parkingLot) && lotParking.isLotAdded(parkingLot2));
    }

    @Test
    public void givenLotParking_WhenLotsNotAdded_ShouldReturnFalse() {
        ParkingLot parkingLot2 = new ParkingLot(5);
        lotParking.addLot(parkingLot);
        Assert.assertFalse(lotParking.isLotAdded(parkingLot2));
    }

    @Test
    public void givenLotParking_WhenVehicleParkedOnLot_ShouldReturnTrue() {
        lotParking.addLot(parkingLot);
        parkingLot.setCapacity(1);
        parkingLot.initializeParkingLot();
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            boolean isVehiclePark = lotParking.isVehicleParked(vehicle);
            Assert.assertTrue(isVehiclePark);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLotSystem_WhenVehicleNotParkedOnLot_ShouldReturnFalse() {
        lotParking.addLot(parkingLot);
        parkingLot.setCapacity(1);
        parkingLot.initializeParkingLot();
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            boolean isVehicleParked = lotParking.isVehicleParked(new Vehicle("red", "audi"));
            Assert.assertFalse(isVehicleParked);
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
        Vehicle secondVehicle = new Vehicle("yellow", "toyota");
        Vehicle thirdVehicle = new Vehicle("black", "audi");
        Vehicle fourthVehicle = new Vehicle("red", "toyota");
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            boolean isVehiclePark1 = lotParking.isVehicleParked(vehicle);
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL, "XYZ");
            boolean isVehiclePark2 = lotParking.isVehicleParked(secondVehicle);
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL, "XYZ");
            boolean isVehiclePark3 = lotParking.isVehicleParked(thirdVehicle);
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL, "XYZ");
            boolean isVehiclePark4 = lotParking.isVehicleParked(fourthVehicle);
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
        lotParking.addLot(parkingLot);
        Vehicle secondVehicle = new Vehicle("blue", "Chevrolet");
        Vehicle thirdVehicle = new Vehicle("blue", "Chevrolet");
        Vehicle fourthVehicle = new Vehicle("blue", "Chevrolet");
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL, "XYZ");
            lotParking.unParkVehicle(fourthVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
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
        Vehicle secondVehicle = new Vehicle("blue", "Chevrolet");
        Vehicle thirdVehicle = new Vehicle("blue", "Chevrolet");
        Vehicle fourthVehicle = new Vehicle("blue", "Chevrolet");
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL, "XYZ");
            lotParking.unParkVehicle(thirdVehicle);
            boolean isVehicleParked = lotParking.isVehicleParked(thirdVehicle);
            Assert.assertFalse(isVehicleParked);
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-10
    @Test
    public void givenParkingLotSystem_WhenDriverTypeIsHandicap_ShouldGiveNearestLot() {
        IParkingLotStrategy parkingLotStrategy = CheckType.typeImplementation(DriverType.HANDICAP);
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
        IParkingLotStrategy parkingLotStrategy = CheckType.typeImplementation(DriverType.NORMAL);
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
        lotParking.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        lotParking.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        lotParking.addLot(parkingLot3);
        Vehicle secondVehicle = new Vehicle("blue", "Chevrolet");
        Vehicle thirdVehicle = new Vehicle("blue", "Chevrolet");
        Vehicle fourthVehicle = new Vehicle("blue", "Chevrolet");
        Vehicle vehicle5 = new Vehicle("blue", "Chevrolet");
        Vehicle sixthVehicle = new Vehicle("blue", "Chevrolet");

        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(new Vehicle("blue", "Chevrolet"), DriverType.HANDICAP, "XYZ");
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle5, DriverType.HANDICAP, "XYZ");
            lotParking.parkVehicle(sixthVehicle, VehicleType.LARGE, "XYZ");
            boolean vehiclePark = lotParking.isVehicleParked(sixthVehicle);
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
        lotParking.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        lotParking.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        lotParking.addLot(parkingLot3);
        Vehicle vehicle1 = new Vehicle("white");
        Vehicle secondVehicle = new Vehicle("black");
        Vehicle thirdVehicle = new Vehicle("white");
        Vehicle fourthVehicle = new Vehicle("blue");
        Vehicle fifthVehicle = new Vehicle("white");
        Vehicle sixthVehicle = new Vehicle("green");
        Vehicle seventhVehicle = new Vehicle("white");
        Vehicle eighthVehicle = new Vehicle("white");
        try {
            lotParking.parkVehicle(vehicle1, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(fifthVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(sixthVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(seventhVehicle, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(eighthVehicle, DriverType.NORMAL, "XYZ");
            List whiteCarList = lotParking.findVehicleByColor("white");
            System.out.println(whiteCarList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLotSystem_WhenParkedVehicleColorIsWhite_ShouldReturnList() {
        parkingLot.setCapacity(3);
        parkingLot.initializeParkingLot();
        Vehicle vehicle1 = new Vehicle("white");
        Vehicle secondVehicle = new Vehicle("black");
        try {
            parkingLot.parkVehicle(vehicle1, DriverType.NORMAL, "XYZ");
            parkingLot.parkVehicle(secondVehicle, DriverType.NORMAL, "XYZ");
            List<Integer> onField = parkingLot.findOnFieldColor("white");
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
        lotParking.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        lotParking.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        lotParking.addLot(parkingLot3);
        Vehicle vehicle1 = new Vehicle("white", "toyota");
        Vehicle vehicle2 = new Vehicle("blue", "BMW");
        Vehicle vehicle3 = new Vehicle("blue", "toyota");
        Vehicle vehicle4 = new Vehicle("white", "toyota");
        Vehicle vehicle5 = new Vehicle("white", "BMW");
        Vehicle vehicle6 = new Vehicle("blue", "toyota");
        Vehicle vehicle7 = new Vehicle("blue", "toyota");
        Vehicle vehicle8 = new Vehicle("blue", "toyota");
        try {
            lotParking.parkVehicle(vehicle1, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle2, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle3, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle4, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle5, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle6, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle7, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle8, DriverType.NORMAL, "XYZ");
            List<List<String>> blueToyotaList = lotParking.findVehicleByTwoFields("blue", "toyota");
            System.out.println(blueToyotaList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_WhenParkedBlueToyotaCar_ShouldReturnLocationAndAttendantNameAndPlateNumber() {
        parkingLot.setCapacity(20);
        parkingLot.initializeParkingLot();
        Vehicle vehicle1 = new Vehicle("white", "toyota");
        Vehicle vehicle2 = new Vehicle("blue", "BMW");
        Vehicle vehicle3 = new Vehicle("blue", "toyota");
        Vehicle vehicle4 = new Vehicle("white", "toyota");
        Vehicle vehicle5 = new Vehicle("white", "BMW");
        Vehicle vehicle6 = new Vehicle("blue", "toyota");
        try {
            parkingLot.parkVehicle(vehicle1, DriverType.NORMAL, "asb");
            parkingLot.parkVehicle(vehicle2, DriverType.NORMAL, "xyz");
            parkingLot.parkVehicle(vehicle3, DriverType.NORMAL, "pqr");
            parkingLot.parkVehicle(vehicle4, DriverType.NORMAL, "xyz");
            parkingLot.parkVehicle(vehicle5, DriverType.NORMAL, "xyz");
            parkingLot.parkVehicle(vehicle6, DriverType.NORMAL, "xyz");
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
        lotParking.addLot(parkingLot);
        ParkingLot parkingLot2 = new ParkingLot(10);
        parkingLot2.initializeParkingLot();
        lotParking.addLot(parkingLot2);
        ParkingLot parkingLot3 = new ParkingLot(10);
        parkingLot3.initializeParkingLot();
        lotParking.addLot(parkingLot3);

        Vehicle vehicle1 = new Vehicle("white", "toyota");
        Vehicle vehicle2 = new Vehicle("blue", "BMW");
        Vehicle vehicle3 = new Vehicle("blue", "toyota");
        Vehicle vehicle4 = new Vehicle("white", "toyota");
        Vehicle vehicle5 = new Vehicle("white", "BMW");
        Vehicle vehicle6 = new Vehicle("blue", "toyota");
        Vehicle vehicle7 = new Vehicle("blue", "toyota");
        Vehicle vehicle8 = new Vehicle("blue", "toyota");
        try {
            lotParking.parkVehicle(vehicle1, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle2, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle3, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle4, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle5, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle6, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle7, DriverType.NORMAL, "XYZ");
            lotParking.parkVehicle(vehicle8, DriverType.NORMAL, "XYZ");
            List<List<Integer>> bmwList = lotParking.findVehicleByModelName("BMW");
            System.out.println(bmwList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLot_WhenParkedBMWVehicle_ShouldReturnLocation() {
        parkingLot.setCapacity(20);
        parkingLot.initializeParkingLot();
        Vehicle vehicle1 = new Vehicle("white", "toyota");
        Vehicle vehicle2 = new Vehicle("blue", "BMW");
        Vehicle vehicle3 = new Vehicle("blue", "toyota");
        Vehicle vehicle4 = new Vehicle("white", "toyota");
        Vehicle vehicle5 = new Vehicle("white", "BMW");
        Vehicle vehicle6 = new Vehicle("blue", "toyota");
        try {
            parkingLot.parkVehicle(vehicle1, DriverType.NORMAL, "asb");
            parkingLot.parkVehicle(vehicle2, DriverType.NORMAL, "xyz");
            parkingLot.parkVehicle(vehicle3, DriverType.NORMAL, "pqr");
            parkingLot.parkVehicle(vehicle4, DriverType.NORMAL, "xyz");
            parkingLot.parkVehicle(vehicle5, DriverType.NORMAL, "xyz");
            parkingLot.parkVehicle(vehicle6, DriverType.NORMAL, "xyz");
            List<Integer> bmwList = parkingLot.findOnFieldModelName("BMW");
            System.out.println(bmwList);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
}