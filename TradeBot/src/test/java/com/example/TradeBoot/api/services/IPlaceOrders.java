package com.example.TradeBoot.api.services;


import com.example.TradeBoot.api.domain.orders.OrderToPlace;

public interface IPlaceOrders {

    public void place(OrderToPlace orderToPlace);
}
