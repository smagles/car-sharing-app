package com.mate.carsharing.service.payment;

import com.mate.carsharing.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageFormatter {

    public String formatPaymentSuccessMessage(Payment payment) {
        if (payment == null || payment.getRental() == null
                || payment.getRental().getUser() == null) {
            throw new IllegalArgumentException("Payment, rental, or user cannot be null");
        }
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
