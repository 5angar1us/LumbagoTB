package com.example.TradeBoot.api.extentions.RequestExcpetions;

public class DoNotSendMoreThanException extends BadRequestByFtxException {
    public DoNotSendMoreThanException(String s) {
        super(s);
    }
}
