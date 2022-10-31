package com.example.TradeBoot.notification.telegram;

import com.example.TradeBoot.notification.EMessageType;
import com.example.TradeBoot.notification.INotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramNotificationService implements INotificationService {

    @Value("${NotificationChartsId}")
    private String[] notificationChartsId;

    private TelegramBot telegramBot;



    public TelegramNotificationService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    @Override
    public void sendMessage(EMessageType messageType, String errorMessage){
        if(notificationChartsId.length != 0){

            var message = String.format("Сообщение: %s.\n Причина: %s",
                    messageType.getMessage(),
                    errorMessage) ;

            telegramBot.sendNotification(notificationChartsId, message);
        }
    }
}
