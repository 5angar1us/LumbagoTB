package com.example.TradeBoot.api.extentions.RequestExcpetions.Checked;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;

public class OrderAlreadyClosedException extends BadRequestByFtxException {

    public OrderAlreadyClosedException(Exception e) {
        super("Order already closed", e);
    }
    public OrderAlreadyClosedException(){
        super("Order already closed");
    }
}
