package com.example.TradeBoot.api.extentions.RequestExcpetions.Checked;

public class BadRequestByFtxException extends Exception {
    public BadRequestByFtxException(String s, Exception e) {
        super(s, e);
    }

    public BadRequestByFtxException(String s) {
        super(s);
    }
}
