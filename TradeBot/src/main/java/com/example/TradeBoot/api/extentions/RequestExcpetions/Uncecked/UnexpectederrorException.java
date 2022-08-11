package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;

public class UnexpectederrorException extends BadRequestByFtxException{
    public UnexpectederrorException(String s) {
        super(s);
    }
}
