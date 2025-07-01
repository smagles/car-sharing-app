package com.mate.carsharing.service.payment.strategy;

import com.mate.carsharing.model.Rental;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinePaymentCalculation implements PaymentCalculationStrategy {
    private static final BigDecimal FINE_MULTIPLIER = BigDecimal.valueOf(1.5);

    @Override
    public BigDecimal calculateAmount(Rental rental) {
        long overdueDays = ChronoUnit.DAYS.between(rental.getReturnDate(), LocalDate.now());
        if (overdueDays < 1) {
            return BigDecimal.ZERO;
        }
        return rental.getCar().getDailyFee()
                .multiply(BigDecimal.valueOf(overdueDays))
                .multiply(FINE_MULTIPLIER);
    }
}
