package com.mate.carsharing.service.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("develop")
public class MockStripeService implements StripeService {

    @Override
    public Session createStripeSession(BigDecimal amount) throws StripeException {
        Session session = new Session();
        session.setId("cs_test_" + UUID.randomUUID());
        session.setUrl("https://mock.stripe.com/session/" + UUID.randomUUID());
        session.setExpiresAt(Instant.now().plus(Duration.ofHours(1)).getEpochSecond());
        return session;
    }

    @Override
    public Session retrieveSession(String sessionId) throws StripeException {
        Session session = new Session();
        session.setId(sessionId);
        session.setPaymentStatus("PAID");

        return session;
    }
}
