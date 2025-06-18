package com.mate.carsharing.dto.rental;

import com.mate.carsharing.model.Car;
import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;

public record RentalCreatedEvent(Rental rental, Car car, User user) {}
