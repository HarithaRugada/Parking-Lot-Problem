package com.parkinglot.exception;

public class ParkingLotException extends Exception {
    public String message;

    public ParkingLotException(String message) {
        super(message);
        this.message = message;
    }
}
