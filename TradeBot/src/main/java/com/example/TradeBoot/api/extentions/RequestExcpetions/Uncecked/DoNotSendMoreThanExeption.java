package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;

public class DoNotSendMoreThanExeption extends BadRequestByFtxException {
    public DoNotSendMoreThanExeption(String s) {
        super(s);
    }
}
