package com.mate.carsharing.service.payment.strategy;

import com.mate.carsharing.model.Payment;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PaymentCalculationStrategyFactory {
    private final Map<Payment.PaymentType, PaymentCalculationStrategy> strategies = Map.of(
            Payment.PaymentType.PAYMENT, new RegularPaymentCalculation(),
            Payment.PaymentType.FINE, new FinePaymentCalculation());

    public PaymentCalculationStrategy getStrategy(Payment.PaymentType type) {
        return Optional.ofNullable(strategies.get(type))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown payment type: " + type));
    }
}
