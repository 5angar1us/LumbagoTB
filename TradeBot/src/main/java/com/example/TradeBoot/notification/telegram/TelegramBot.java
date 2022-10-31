package com.example.TradeBoot.notification.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    static final Logger log =
            LoggerFactory.getLogger(TelegramBot.class);
    Message requestMessage = new Message();
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
        messageSender.setChatId(requestMessage.getChatId());

        if (request.hasMessage() && requestMessage.hasText()) {
            String messageText = requestMessage.getText();

            if (messageText.contains("/chartId")) {
                Long chatId = request.getMessage().getChatId();
                defaultSendMessageOrLogError(messageSender, "ID Канала : " + chatId.toString());
            }
            else {
                defaultSendMessageOrLogError(messageSender,"Я тебя не понял");
            }
        }
    }

    public void sendNotification(String[] chartsId, String message){
        Arrays.stream(chartsId).forEach(chartId->{
            messageSender.setChatId(chartId);
            sendMessageOrLogError(messageSender, message, "Sending notification in telegram");
        });
    }


    private void defaultSendMessageOrLogError(SendMessage messageSender, String message)
    {
        sendMessageOrLogError(messageSender, message, "Sending message");
    }

    private void sendMessageOrLogError(SendMessage messageSender, String message, String stage){
        try {
            defaultSendMessage(messageSender, message);
        } catch (TelegramApiException e) {

            var errorMessage = String.format("Stage: %s. ErrorMessage: %s. Message: %s",
                    stage,
                    e.getMessage(),
                    message
            );

            log.error(errorMessage);
        }
    }

    /**
     * Шабонный метод отправки сообщения пользователю
     *
     * @param messageSender - метод обработки сообщения
     * @param message - сообщение
     */
    private void defaultSendMessage(SendMessage messageSender, String message) throws TelegramApiException {
        messageSender.setText(message);
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