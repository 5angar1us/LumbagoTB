package com.example.TradeBoot.notification;

public enum EMessageType {
    API_UNKNOWN_ERROR("Неизвестная ошибка API"),
    TROUBLE_CLOSING_ORDERS("Проблемы с закрытием ордеров"),
    INTERNAL_ERROR("Внутренняя ошибка"),
    CONVERT_EXCEPTION("Ошибка конвертации модели"),
    Empty("Ничего");

    private final String message;
    EMessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
