package com.mate.carsharing.exception.custom;

public class RegistrationException extends RuntimeException {
    /**
     * Constructs a new RegistrationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public RegistrationException(String message) {
        super(message);
    }

}
