package com.mate.carsharing.service.rental;

import com.mate.carsharing.model.Rental;
import com.mate.carsharing.repository.RentalRepository;
import com.mate.carsharing.service.notification.NotificationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalOverdueCheckServiceImpl implements RentalOverdueCheckService {
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;
    private final RentalMessageFormatter messageFormatter;

    @Override
    @Scheduled(cron = "0 30 12 * * *")
    @Transactional(readOnly = true)
    public void checkOverdueRentals() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Rental> overdueRentals = rentalRepository.findOverdueRentals(tomorrow);

        if (overdueRentals.isEmpty()) {
            notificationService.sendNotification("No rentals overdue today!");
            return;
        }
        for (Rental rental : overdueRentals) {
            String message = messageFormatter.formatOverdueRentalMessage(rental);
            notificationService.sendNotification(message);
        }
    }
}
