package com.example.TradeBoot.notification.telegram;

import com.example.TradeBoot.notification.EMessageType;
import com.example.TradeBoot.notification.INotificationService;
import com.example.TradeBoot.notification.telegram.commands.*;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

@Service
public class TelegramNotificationService implements INotificationService {



    private SendTelegramBotNotificationService sendTelegramBotNotificationService;

    private final ImmutableMap<EMessageType, String> detailMap;

    public TelegramNotificationService(SendTelegramBotNotificationService sendTelegramBotNotificationService) {
        this.sendTelegramBotNotificationService = sendTelegramBotNotificationService;

        detailMap = ImmutableMap.<EMessageType, String>builder()
                .put(EMessageType.ServerStoped, "Если ошибка вылезла не несколько раз подряд, то скорее всего она не критична и сервер можно перезапустить")
                .put(EMessageType.TroubleClosingOrders, "Страшно! Страшно! Срочно зовите ремонтника")
                .build();
    }

    @Override
    public void sendMessage(EMessageType messageType, String errorMessage){

        var messageDetails = detailMap.getOrDefault(messageType, "");


        var message = String.format("Сообщение: %s\nПричина: %s",
                messageType.getMessage(),
                errorMessage) ;

        if(messageDetails.isBlank() == false) message = message + "\n\n" + messageDetails;

        sendTelegramBotNotificationService.sendNotification(message);

    }
}
