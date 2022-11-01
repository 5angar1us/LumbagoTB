package com.example.TradeBoot.notification.telegram;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class SendTelegramBotMessageService {

    static final Logger log =
            LoggerFactory.getLogger(SendTelegramBotMessageService.class);

    private final TelegramBot telegramBot;

    @Autowired
    public SendTelegramBotMessageService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(Long chatId, String message){
        sendMessage(chatId, message, "Sending message");
    }

    public void sendMessage(Long chatId, String message, String stage) {
        if (StringUtils.isBlank(message)) return;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableHtml(true);
        sendMessage.setText(message);

        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            var errorMessage = String.format("Stage: %s. ErrorMessage: %s. Message: %s",
                    stage,
                    e.getMessage(),
                    message
            );

            log.error(errorMessage);
        }
    }

    public void sendMessage(Long chatId, List<String> messages) {
        if (CollectionUtils.isEmpty(messages)) return;

        messages.forEach(m -> sendMessage(chatId, m));
    }
}
