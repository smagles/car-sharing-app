package com.mate.carsharing.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    @Value("${telegram.bot.chat.id}")
    private String chatId;
    private final CarRentalTelegramBot carRentalBot;

    @Override
    public void sendNotification(String message) {
        carRentalBot.sendMessage(chatId, message);
    }
}
