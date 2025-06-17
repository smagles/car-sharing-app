package com.mate.carsharing.exception.custom;

public class RentalAlreadyReturnedException extends RuntimeException {
    public RentalAlreadyReturnedException(Long rentalId) {
        super("Rental with id " + rentalId + " has already been returned.");
    }
}

