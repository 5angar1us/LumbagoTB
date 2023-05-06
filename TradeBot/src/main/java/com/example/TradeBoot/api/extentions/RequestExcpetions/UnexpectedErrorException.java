package com.example.TradeBoot.api.extentions.RequestExcpetions;

public class UnexpectedErrorException extends BadRequestByFtxException{
    public UnexpectedErrorException(String s) {
        super(s);
    }
}
