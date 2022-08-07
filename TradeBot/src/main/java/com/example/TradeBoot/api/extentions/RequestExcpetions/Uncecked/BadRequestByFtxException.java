package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;

public class BadRequestByFtxException extends RuntimeException {
    public BadRequestByFtxException(String s, Exception e) {
        super(s, e);
    }

    public BadRequestByFtxException(String s) {
        super(s);
    }
}
