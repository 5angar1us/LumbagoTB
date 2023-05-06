package com.example.TradeBoot.api.extentions.RequestExcpetions;

public class SendRequestException extends BadRequestByFtxException {

    public SendRequestException(String s, Exception e) {
        super(s, e);
    }
    public SendRequestException(String s) {
        super(s);
    }
}
