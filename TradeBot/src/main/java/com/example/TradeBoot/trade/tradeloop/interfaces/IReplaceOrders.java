package com.example.TradeBoot.trade.tradeloop.interfaces;

import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.trade.model.OrderInformation;

import java.util.Map;

public interface IReplaceOrders {

    Map<OrderInformation, PlacedOrder> replace(
            Map<OrderInformation, PlacedOrder> placedOrders,
            Map<OrderInformation, OrderToPlace> ordersToPlace);
}
