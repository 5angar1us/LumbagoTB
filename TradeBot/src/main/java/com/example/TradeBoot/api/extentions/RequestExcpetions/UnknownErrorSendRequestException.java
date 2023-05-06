package com.example.TradeBoot.api.extentions.RequestExcpetions;

public class UnknownErrorSendRequestException extends RuntimeException{

    public UnknownErrorSendRequestException(String s, Exception e) {
        super(s, e);
    }

    public UnknownErrorSendRequestException(String s) {
        super(s);
    }
}
