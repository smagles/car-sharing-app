package com.mate.carsharing.service.notification;

import com.mate.carsharing.exception.custom.NotificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class CarRentalTelegramBot extends TelegramLongPollingBot {
    public static final String MARKDOWN = "Markdown";

    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @PostConstruct
    private void validateConfig() {
        if (username == null || username.isBlank()
                || token == null || token.isBlank()) {
            throw new IllegalStateException(
                    "Telegram bot username or token is not configured");
        }
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode(MARKDOWN);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new NotificationException("Failed to send Telegram message ", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if (messageText.equals("/start")) {
                String response = "👋 Welcome to Car Rental Bot!"
                        + "\nYou can start booking your cars here.";
                sendMessage(chatId, response);
            } else {
                sendMessage(chatId, "❓ Unknown command. Type /start to begin.");
            }
        }
    }
}
