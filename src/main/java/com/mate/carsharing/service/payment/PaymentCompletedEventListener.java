package com.mate.carsharing.service.payment;

import com.mate.carsharing.dto.payment.PaymentCompletedEvent;
import com.mate.carsharing.model.Payment;
import com.mate.carsharing.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentCompletedEventListener {
    private final PaymentMessageFormatter messageFormatter;
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        Payment payment = event.payment();
        String message = messageFormatter.formatPaymentSuccessMessage(payment);
        notificationService.sendNotification(message);
    }
}
