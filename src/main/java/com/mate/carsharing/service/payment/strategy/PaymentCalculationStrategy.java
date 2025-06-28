package com.mate.carsharing.service.payment.strategy;

import com.mate.carsharing.model.Rental;
import java.math.BigDecimal;

public interface PaymentCalculationStrategy {
    BigDecimal calculateAmount(Rental rental);

}
