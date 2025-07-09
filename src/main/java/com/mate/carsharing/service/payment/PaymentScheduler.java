package com.mate.carsharing.service.payment;

import com.mate.carsharing.model.Payment;
import com.mate.carsharing.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentScheduler {
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void markExpiredPayments() {
        List<Payment> pendingPayments = paymentRepository
                .findByStatus(Payment.PaymentStatus.PENDING);

        for (Payment payment : pendingPayments) {
            try {
                Session session = stripeService.retrieveSession(payment.getSessionId());
                if (Instant.now().isAfter(Instant.ofEpochSecond(session.getExpiresAt()))) {
                    payment.setStatus(Payment.PaymentStatus.EXPIRED);
                    paymentRepository.save(payment);
                }

            } catch (StripeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
