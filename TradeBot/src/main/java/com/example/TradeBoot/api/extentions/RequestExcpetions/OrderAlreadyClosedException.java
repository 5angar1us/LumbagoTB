package com.example.TradeBoot.api.extentions.RequestExcpetions;

public class OrderAlreadyClosedException extends BadRequestByFtxException {

    public OrderAlreadyClosedException(Exception e) {
        super("Order already closed", e);
    }
    public OrderAlreadyClosedException(){
        super("Order already closed");
    }
}
