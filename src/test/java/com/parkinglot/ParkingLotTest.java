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
import java.time.format.DateTimeFormatter;
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
        vehicle = new Vehicle();
        parkingOwner = new ParkingOwner();
        security = new AirportSecurity();
        parkingLot.initializeParkingLot();
    }

    //TC-1
    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertTrue(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenVehicle_WhenParkedTwice_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            boolean isParked1 = parkingLot.isVehicleParked(vehicle);
            Vehicle vehicle2 = new Vehicle();
            parkingLot.parkVehicle(vehicle2, DriverType.NORMAL);
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.unParkVehicle(new Vehicle());
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_CheckIfVehicleIsNotPresent_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(null, DriverType.NORMAL);
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
        parkingLot.setCapacity(2);
        parkingLot.initializeParkingLot();
        parkingLot.registerParkingLotObserver(parkingOwner);
        try {
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
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
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
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
            parkingLot.parkVehicle(new Vehicle(), DriverType.NORMAL);
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            parkingLot.findVehicleLot(new Vehicle());
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
            parkingLot.parkVehicle(vehicle, DriverType.NORMAL);
            boolean isTimeSet = parkingLot.isTimeSet(vehicle);
            Assert.assertTrue(isTimeSet);
            Assert.assertEquals(new ParkingSlot(vehicle).time, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
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
            lotParking.parkVehicle(vehicle, DriverType.NORMAL);
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
            lotParking.parkVehicle(vehicle, DriverType.NORMAL);
            boolean isVehicleParked = lotParking.isVehicleParked(new Vehicle());
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
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        Vehicle fourthVehicle = new Vehicle();
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL);
            boolean isVehiclePark1 = lotParking.isVehicleParked(vehicle);
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL);
            boolean isVehiclePark2 = lotParking.isVehicleParked(secondVehicle);
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL);
            boolean isVehiclePark3 = lotParking.isVehicleParked(thirdVehicle);
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL);
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
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        Vehicle fourthVehicle = new Vehicle();
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL);
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL);
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
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        Vehicle fourthVehicle = new Vehicle();
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL);
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL);
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
        Vehicle secondVehicle = new Vehicle();
        Vehicle thirdVehicle = new Vehicle();
        Vehicle fourthVehicle = new Vehicle();
        Vehicle vehicle5 = new Vehicle();
        Vehicle sixthVehicle = new Vehicle();

        try {
            lotParking.parkVehicle(vehicle, DriverType.NORMAL);
            lotParking.parkVehicle(new Vehicle(), DriverType.HANDICAP);
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(vehicle5, DriverType.HANDICAP);
            lotParking.parkVehicle(sixthVehicle, VehicleType.LARGE);
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
            lotParking.parkVehicle(vehicle1, DriverType.NORMAL);
            lotParking.parkVehicle(secondVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(thirdVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(fourthVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(fifthVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(sixthVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(seventhVehicle, DriverType.NORMAL);
            lotParking.parkVehicle(eighthVehicle, DriverType.NORMAL);
            List whiteCarList = lotParking.findVehicleByField("white");
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
            parkingLot.parkVehicle(vehicle1, DriverType.NORMAL);
            parkingLot.parkVehicle(secondVehicle, DriverType.NORMAL);
            ArrayList<Integer> onField = parkingLot.findOnField("white");
            System.out.println(onField);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
}