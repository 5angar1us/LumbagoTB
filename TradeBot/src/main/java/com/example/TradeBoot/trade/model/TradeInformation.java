package com.example.TradeBoot.trade.model;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.util.List;
import java.util.Objects;

public class TradeInformation {
    public void setOrderInformations(List<OrderInformation> orderInformations) {
        Objects.requireNonNull(orderInformations);
        if (orderInformations.size() == 0) throw new IllegalArgumentException("orderInformation");

        ESide defaultSide = orderInformations.get(0).getSide();
        var allInOneSide = orderInformations.stream()
                .allMatch( orderInformation -> orderInformation.getSide().equals(defaultSide));

        if(allInOneSide == false) throw new IllegalArgumentException("orderInformation");

        this.orderInformations = orderInformations;
    }

    public TradeInformation(List<OrderInformation> orderInformations) {
        setOrderInformations(orderInformations);
    }

    public ESide getBaseSide() {
        return orderInformations.get(0).getSide();
    }
    private  List<OrderInformation> orderInformations;
    public List<OrderInformation> getOrderInformations() {
        return orderInformations;
    }

    @Override
    public String toString() {
        return "TradeInformation{" +
                "orderInformations=" + orderInformations +
                '}';
    }
}
