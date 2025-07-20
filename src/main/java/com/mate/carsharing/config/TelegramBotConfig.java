package com.mate.carsharing.config;

import com.mate.carsharing.exception.custom.NotificationException;
import com.mate.carsharing.service.notification.CarRentalTelegramBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
@Profile("!develop")
public class TelegramBotConfig {
    private final CarRentalTelegramBot carRentalTelegramBot;

    @PostConstruct
    public void registerBot() {
        try {
            new TelegramBotsApi(DefaultBotSession.class)
                    .registerBot(carRentalTelegramBot);

        } catch (TelegramApiException ex) {
            throw new NotificationException(
                    "Failed to register Telegram bot – notifications disabled", ex);
        }
    }
}
