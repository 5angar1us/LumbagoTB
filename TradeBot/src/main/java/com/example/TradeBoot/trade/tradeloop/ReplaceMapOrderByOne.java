package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.tradeloop.interfaces.IPlaceOrder;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrder;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrderMap;

import java.util.HashMap;
import java.util.Map;

public class ReplaceMapOrderByOne implements IReplaceOrderMap {


    public ReplaceMapOrderByOne(IOrdersService ordersService){
        replaceOrder = new ReplaceByOne(ordersService);
    }

    private IReplaceOrder replaceOrder;


    @Override
    public Map<OrderInformation, PlacedOrder> replace(Map<OrderInformation, PlacedOrder> placedOrders, Map<OrderInformation, OrderToPlace> ordersToPlace) {
        Map<OrderInformation, PlacedOrder> newPlacedOrders = new HashMap<>();

        for (Map.Entry<OrderInformation, PlacedOrder> orderInformationPlacedOrderEntry : placedOrders.entrySet()) {
            var orderInformation = orderInformationPlacedOrderEntry.getKey();
            var placedOrder = orderInformationPlacedOrderEntry.getValue();

            var orderToPlace = ordersToPlace.get(orderInformation);

            var newPlacedOrder = replaceOrder.replace(placedOrder, orderToPlace);

            newPlacedOrders.put(orderInformation, newPlacedOrder);
        }

        return newPlacedOrders;
    }
}
