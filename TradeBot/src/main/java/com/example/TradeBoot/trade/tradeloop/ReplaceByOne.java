package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.tradeloop.interfaces.IPlaceOrder;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrder;

public class ReplaceByOne implements IReplaceOrder {

    public ReplaceByOne(IOrdersService ordersService, IPlaceOrder placeOrder) {
        this.ordersService = ordersService;
        this.placeOrder = placeOrder;
    }

    IOrdersService ordersService;
    IPlaceOrder placeOrder;

    @Override
    public PlacedOrder replace(PlacedOrder placedOrder, OrderToPlace orderToPlace) {
        ordersService.cancelOrder(placedOrder.getId());
        return placeOrder.place(orderToPlace);
    }
}
