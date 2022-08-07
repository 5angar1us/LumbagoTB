package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;


public class RetryRequestException extends RuntimeException {

    public RetryRequestException() {
        super("Please retry request");
    }
}