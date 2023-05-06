package com.example.TradeBoot.notification.telegram;

import com.example.TradeBoot.notification.EMessageType;
import com.example.TradeBoot.notification.INotificationService;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

@SuppressWarnings("ALL")
@Service
public class TelegramNotificationService implements INotificationService {

    private final SendTelegramBotNotificationService sendTelegramBotNotificationService;

    private final ImmutableMap<EMessageType, String> messageExplanationMap;

    public TelegramNotificationService(SendTelegramBotNotificationService sendTelegramBotNotificationService) {
        this.sendTelegramBotNotificationService = sendTelegramBotNotificationService;

        messageExplanationMap = ImmutableMap.<EMessageType, String>builder()
                .put(EMessageType.API_UNKNOWN_ERROR, "Сервер остановлен из-за неизвестной ошибки API. Если ошибка вылезла не несколько раз подряд, то скорее всего она не критична и сервер можно перезапустить")
                .put(EMessageType.TROUBLE_CLOSING_ORDERS, "Проблема с закрытием ордеров!!! Страшно! Страшно! Срочно зовите ремонтника")
                .put(EMessageType.INTERNAL_ERROR,  "Сервер остановлен из-за внутренней ошибки. Страшно! Страшно! Срочно зовите ремонтника")
                .put(EMessageType.CONVERT_EXCEPTION, "Сервер остановлен из-за ошибки конвертации модели. Страшно! Страшно! Срочно зовите ремонтника")
                .build();
    }

    @Override
    public void sendMessage(EMessageType messageType, String errorMessage){

        var explanation = messageExplanationMap.getOrDefault(messageType, "");


        var message = String.format("Тип ошибки: %s\nПричина: %s",
                messageType.getMessage(),
                errorMessage);

        if(explanation.isBlank() == false) message = message + "\n\n" + explanation;

        sendTelegramBotNotificationService.sendNotification(message);

    }
}
