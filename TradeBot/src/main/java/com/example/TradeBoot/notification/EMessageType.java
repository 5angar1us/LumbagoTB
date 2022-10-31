package com.example.TradeBoot.notification;

import com.example.TradeBoot.api.utils.IEnumStringValue;

public enum EMessageType {
    ServerStoped("Сервер остановлен"),
    TroubleClosingOrders("Проблемы с закрытием ордеров"),
    Empty("");

    private final String message;
    EMessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
