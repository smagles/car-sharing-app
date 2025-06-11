package com.mate.carsharing.exception.custom;

public class NoAvailableCarException extends RuntimeException {
    public NoAvailableCarException(String message) {
        super(message);
    }
}

