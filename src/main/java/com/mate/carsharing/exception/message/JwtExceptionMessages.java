package com.mate.carsharing.exception.message;

public class JwtExceptionMessages {
    public static final String TOKEN_EXPIRED = "Token expired";
    public static final String INVALID_TOKEN_FORMAT = "Invalid token format";
    public static final String TOKEN_VALIDATION_FAILED = "Token validation failed";
    public static final String INVALID_TOKEN = "Invalid token";

    /**** 
         * Prevents instantiation of the JwtExceptionMessages utility class.
         */
    private JwtExceptionMessages() {
    }
}
