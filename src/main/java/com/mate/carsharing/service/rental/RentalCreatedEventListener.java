package com.mate.carsharing.service.rental;

import com.mate.carsharing.dto.rental.RentalCreatedEvent;
import com.mate.carsharing.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RentalCreatedEventListener {
    private final RentalMessageFormatter messageFormatter;
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRentalCreated(RentalCreatedEvent event) {
        String message = messageFormatter
                .formatRentalCreationMessage(event.rental(), event.car(), event.user());
        notificationService.sendRentalNotification(message);
    }
}
