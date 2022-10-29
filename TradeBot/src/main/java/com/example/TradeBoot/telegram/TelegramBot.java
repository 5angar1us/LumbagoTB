package com.example.TradeBoot.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TelegramBot extends TelegramLongPollingBot {

    private Message requestMessage = new Message();
    private final SendMessage messageSender = new SendMessage();

    private final String botUsername;
    private final String botToken;

    public TelegramBot(
            TelegramBotsApi telegramBotsApi,
            @Value("${telegram-bot.name}") String botUsername,
            @Value("${telegram-bot.token}") String botToken) throws TelegramApiException {
        this.botUsername = botUsername;
        this.botToken = botToken;

        telegramBotsApi.registerBot(this);
    }

    /**
     * Этот метод вызывается при получении обновлений через метод GetUpdates.
     *
     * @param request Получено обновление
     */
    @Override
    public void onUpdateReceived(Update request) {
        requestMessage = request.getMessage();
        messageSender.setChatId(requestMessage.getChatId().toString());

        if (request.hasMessage() && requestMessage.hasText()) {
            try {
                sendMessage(messageSender,"Я бот простой и отвечать на команды не умею");
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }



    /**
     * Шабонный метод отправки сообщения пользователю
     *
     * @param messageSender - метод обработки сообщения
     * @param msg - сообщение
     */
    private void sendMessage(SendMessage messageSender, String msg) throws TelegramApiException {
        messageSender.setText(msg);
        execute(messageSender);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}