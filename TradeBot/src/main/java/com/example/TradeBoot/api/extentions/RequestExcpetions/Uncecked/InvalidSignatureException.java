package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;

public class InvalidSignatureException extends BadRequestByFtxException {

    public InvalidSignatureException(String message) {
        super(message);
    }
}
