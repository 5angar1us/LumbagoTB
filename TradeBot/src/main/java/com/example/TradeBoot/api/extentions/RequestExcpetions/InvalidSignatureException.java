package com.example.TradeBoot.api.extentions.RequestExcpetions;

public class InvalidSignatureException extends BadRequestByFtxException {

    public InvalidSignatureException(String message) {
        super(message);
    }
}
