package com.mate.carsharing.exception.custom;

public class PaymentAlreadyPaidException extends RuntimeException {
    public PaymentAlreadyPaidException(String message) {
        super(message);
    }
}
