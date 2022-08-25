package com.example.TradeBoot.trade.tradeloop.interfaces;

import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;

public interface IPlaceOrder {
    PlacedOrder place(OrderToPlace order);
}
