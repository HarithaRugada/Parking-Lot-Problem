package com.parkinglot;

import com.parkinglot.drivertypes.HandicapDriver;
import com.parkinglot.drivertypes.NormalDriver;
import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.interfaces.IParkingLotOnDriverType;
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
import java.util.ArrayList;
import java.util.List;

import static com.parkinglot.drivertypes.NormalDriver.NORMAL;

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
            parkingLot.parkVehicle(vehicle, NORMAL);
            boolean isParked = parkingLot.isVehicleParked(vehicle);
            Assert.assertTrue(isParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenVehicle_WhenParkedTwice_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.parkVehicle(vehicle, NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
            boolean isParked1 = parkingLot.isVehicleParked(vehicle);
            Object vehicle2 = new Object();
            parkingLot.parkVehicle(vehicle2, NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.unParkVehicle(new Object());
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void givenVehicle_CheckIfVehicleIsNotPresent_ShouldThrowException() {
        try {
            parkingLot.parkVehicle(null, NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
            parkingLot.parkVehicle(new Object(), NORMAL);
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
            parkingLot.parkVehicle(vehicle, NORMAL);
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
            parkingLot.parkVehicle(new Object(), NORMAL);
            parkingLot.parkVehicle(vehicle, NORMAL);
            int slotNumber = parkingLot.findVehicleSlot(vehicle);
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
            parkingLot.parkVehicle(new Object(), NORMAL);
            parkingLot.parkVehicle(vehicle, NORMAL);
            parkingLot.findVehicleSlot(new Object());
        } catch (ParkingLotException e) {
            Assert.assertEquals("Vehicle not found", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //TC-8
    @Test
    public void givenParkingLot_WhenVehicleParkedIfTimeIsSet_ShouldReturnTrue() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        try {
            parkingLot.parkVehicle(vehicle, NORMAL);
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
            lotParking.parkVehicle(vehicle, NORMAL);
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
            lotParking.parkVehicle(vehicle, NORMAL);
            boolean isVehicleParked = lotParking.isVehicleParked(new Object());
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
        Object vehicle2 = new Object();
        Object vehicle3 = new Object();
        Object vehicle4 = new Object();
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle, NORMAL);
            boolean isVehiclePark1 = lotParking.isVehicleParked(vehicle);
            lotParking.parkVehicle(vehicle2, NORMAL);
            boolean isVehiclePark2 = lotParking.isVehicleParked(vehicle2);
            lotParking.parkVehicle(vehicle3, NORMAL);
            boolean isVehiclePark3 = lotParking.isVehicleParked(vehicle3);
            lotParking.parkVehicle(vehicle4, NORMAL);
            boolean isVehiclePark4 = lotParking.isVehicleParked(vehicle4);
            Assert.assertTrue(isVehiclePark1 && isVehiclePark2 && isVehiclePark3 && isVehiclePark4);
            int slotNumber = parkingLot.findVehicleSlot(vehicle);
            ParkingLot lotNumber = lotParking.findVehicleLot(vehicle);
            Assert.assertEquals(1, slotNumber);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenLotParking_WhenUnParkVehicleNotAvailable_ShouldThrowException() {
        parkingLot.setCapacity(10);
        parkingLot.initializeParkingLot();
        lotParking.addLot(parkingLot);
        Object vehicle2 = new Object();
        Object vehicle3 = new Object();
        Object vehicle4 = new Object();
        try {
            lotParking.parkVehicle(vehicle, NORMAL);
            lotParking.parkVehicle(vehicle2, NORMAL);
            boolean isVehicleUnParked = lotParking.unParkVehicle(vehicle3);
            Assert.assertTrue(isVehicleUnParked);
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
        Object vehicle2 = new Object();
        Object vehicle3 = new Object();
        Object vehicle4 = new Object();
        lotParking.addLot(parkingLot2);
        try {
            lotParking.parkVehicle(vehicle, NORMAL);
            lotParking.parkVehicle(vehicle2, NORMAL);
            lotParking.parkVehicle(vehicle3, NORMAL);
            lotParking.parkVehicle(vehicle4, NORMAL);
            boolean isVehicleUnParked = lotParking.unParkVehicle(vehicle);
            Assert.assertTrue(isVehicleUnParked);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    //TC-10
    @Test
    public void givenParkingLotSystem_WhenDriverTypeIsHandicap_ShouldGiveNearestLot() {
        IParkingLotOnDriverType iParkingLotOnDriverType = HandicapDriver.HANDICAP;
        List<ParkingLot> parkingLots1 = new ArrayList<>();
        ParkingLot parkingLot12 = new ParkingLot(1);
        parkingLot12.setCapacity(10);
        parkingLot12.initializeParkingLot();
        parkingLots1.add(parkingLot12);
        try {
            parkingLot = iParkingLotOnDriverType.getParkingLot(parkingLots1);
            Assert.assertEquals(parkingLot12, parkingLot);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenParkingLotSystem_WhenDriverTypeIsNormal_ShouldReturnTheFirstEmptySlot() {
        IParkingLotOnDriverType iParkingLotOnDriverType = NormalDriver.NORMAL;
        List<ParkingLot> parkingLots1 = new ArrayList<>();
        ParkingLot parkingLot12 = new ParkingLot(1);
        parkingLot12.setCapacity(10);
        parkingLot12.initializeParkingLot();
        parkingLots1.add(parkingLot12);
        try {
            parkingLot = iParkingLotOnDriverType.getParkingLot(parkingLots1);
            Assert.assertEquals(parkingLot12, parkingLot);
        } catch (ParkingLotException e) {
            e.printStackTrace();
        }
    }
}