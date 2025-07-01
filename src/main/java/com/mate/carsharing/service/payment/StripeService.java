package com.mate.carsharing.service.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;

public interface StripeService {
    Session createStripeSession(BigDecimal amount) throws StripeException;

    Session retrieveSession(String sessionId) throws StripeException;
}
