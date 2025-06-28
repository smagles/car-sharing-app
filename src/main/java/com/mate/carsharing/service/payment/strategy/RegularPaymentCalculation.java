package com.mate.carsharing.service.payment.strategy;

import com.mate.carsharing.model.Rental;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegularPaymentCalculation implements PaymentCalculationStrategy {
    @Override
    public BigDecimal calculateAmount(Rental rental) {
        long days = ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate()) + 1;
        return rental.getCar().getDailyFee().multiply(BigDecimal.valueOf(days));
    }
}
