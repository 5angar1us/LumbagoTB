package com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked;

public class OrderAlreadyQueuedForCancellationException extends BadRequestByFtxException {
    public OrderAlreadyQueuedForCancellationException() {
        super("Order already queued for cancellation");
    }
}
