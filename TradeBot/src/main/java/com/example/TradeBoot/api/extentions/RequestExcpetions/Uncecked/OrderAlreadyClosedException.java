package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;

public class OrderAlreadyClosedException extends BadRequestByFtxException {

    public OrderAlreadyClosedException(Exception e) {
        super("Order already closed", e);
    }
    public OrderAlreadyClosedException(){
        super("Order already closed");
    }
}
