package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.tradeloop.interfaces.IPlaceOrder;
import com.example.TradeBoot.trade.tradeloop.interfaces.IReplaceOrders;

import java.util.HashMap;
import java.util.Map;

public class ReplaceOrdersByOne implements IReplaceOrders {


    public ReplaceOrdersByOne(IOrdersService ordersService, IPlaceOrder placeOrder) {
        this.ordersService = ordersService;
        this.placeOrder = placeOrder;
    }

    private IOrdersService ordersService;

    private IPlaceOrder placeOrder;



    @Override
    public Map<OrderInformation, PlacedOrder> replace(Map<OrderInformation, PlacedOrder> placedOrders, Map<OrderInformation, OrderToPlace> ordersToPlace) {
        Map<OrderInformation, PlacedOrder> newPlacedOrders = new HashMap<>();

        for (Map.Entry<OrderInformation, PlacedOrder> orderInformationPlacedOrderEntry : placedOrders.entrySet()) {
            var orderInformation = orderInformationPlacedOrderEntry.getKey();
            var placedOrder = orderInformationPlacedOrderEntry.getValue();

            ordersService.cancelOrder(placedOrder.getId());
            var orderToPlace = ordersToPlace.get(orderInformation);
            var newPlacedOrder = placeOrder.place(orderToPlace);

            newPlacedOrders.put(orderInformation, newPlacedOrder);
        }

        return newPlacedOrders;
    }
}
