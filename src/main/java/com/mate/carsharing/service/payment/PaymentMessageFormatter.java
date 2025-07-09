package com.mate.carsharing.service.payment;

import com.mate.carsharing.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageFormatter {

    public String formatPaymentSuccessMessage(Payment payment) {
        return String.format("""
                ✅ *Payment Success!*
                👤 *User:* %s
                💸 *Amount:* %s
                📦 *Rental ID:* %d
                🏷️ *Type:* %s
                """,
                payment.getRental().getUser().getUsername(),
                payment.getAmount(),
                payment.getRental().getId(),
                payment.getType()
        );
    }
}
