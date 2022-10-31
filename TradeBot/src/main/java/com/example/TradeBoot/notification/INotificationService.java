package com.example.TradeBoot.notification;

public interface INotificationService {
    void sendMessage(EMessageType messageType, String errorMessage);

}
