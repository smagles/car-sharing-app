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

    public String formatOverdueRentalMessage(Rental rental) {
        return String.format("""
                        🚨 *Overdue Rental Alert* 🚨
                        *Rental ID:* %d
                        *Car:* %s %s
                        *User:* %s %s (%s)
                        *Planned Return Date:* %s
                        """,
                rental.getId(),
                rental.getCar().getBrand(),
                rental.getCar().getModel(),
                rental.getUser().getFirstName(),
                rental.getUser().getLastName(),
                rental.getUser().getEmail(),
                rental.getReturnDate()
        );
    }
}
