package com.mate.carsharing.exception.custom;

public class NotificationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
