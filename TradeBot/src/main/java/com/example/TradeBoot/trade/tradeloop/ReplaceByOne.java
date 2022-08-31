package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrder;

public class ReplaceByOne implements IReplaceOrder {

    public ReplaceByOne(IOrdersService ordersService) {
        this.ordersService = ordersService;
    }

    IOrdersService ordersService;

    @Override
    public PlacedOrder replace(PlacedOrder placedOrder, OrderToPlace orderToPlace) {
        ordersService.cancelOrder(placedOrder.getId());
        return ordersService.placeOrder(orderToPlace);
    }
}
