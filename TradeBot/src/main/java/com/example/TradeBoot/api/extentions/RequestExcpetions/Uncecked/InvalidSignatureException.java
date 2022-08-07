package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;

public class InvalidSignatureException extends BadRequestByFtxException {
    public InvalidSignatureException(String s, Exception e) {
        super(s, e);
    }

    public InvalidSignatureException(String s) {
        super(s);
    }
}
