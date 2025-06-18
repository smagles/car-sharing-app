package com.mate.carsharing.service.rental;

import com.mate.carsharing.model.Car;
import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import org.springframework.stereotype.Component;

@Component
public class RentalMessageFormatter {
    public String formatRentalCreationMessage(Rental rental, Car car, User user) {
        return String.format("""
                🚗 *New rental created!*
                👤 *User:* %s
                🚘 *Car:* %s %s
                📅 *From:* %s *To:* %s
                """,
                user.getUsername(),
                car.getBrand(), car.getModel(),
                rental.getRentalDate().toString(),
                rental.getReturnDate().toString()
        );
    }
}
