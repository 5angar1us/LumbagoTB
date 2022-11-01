package com.example.TradeBoot.notification.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SendTelegramBotNotificationService {

    static final Logger log =
            LoggerFactory.getLogger(SendTelegramBotNotificationService.class);

    private final Long[] notificationChartsId;

    private final SendTelegramBotMessageService sendTelegramBotMessageService;

    @Autowired
    public SendTelegramBotNotificationService(TelegramBot telegramBot, Long[] notificationChartsId, SendTelegramBotMessageService sendTelegramBotMessageService) {
        this.notificationChartsId = notificationChartsId;
        this.sendTelegramBotMessageService = sendTelegramBotMessageService;
    }

    public void sendNotification(String message){
        if(notificationChartsId.length == 0) return;

        Arrays.stream(notificationChartsId).forEach(chatId->{

            sendTelegramBotMessageService.sendMessage(chatId, message ,"Sending notification in telegram");
        });
    }


}