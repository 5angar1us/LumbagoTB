package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;


public class MultithreadingException extends BadRequestByFtxException {

    public MultithreadingException() {
        super("Missing parameter market");
    }
}