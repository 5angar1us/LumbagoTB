package com.example.TradeBoot.api.extentions.RequestExcpetions;


public class RetryRequestException extends BadRequestByFtxException {

    public RetryRequestException() {
        super("Please retry request");
    }
}