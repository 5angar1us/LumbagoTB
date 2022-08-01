package com.example.TradeBoot.trade.model;

import java.util.List;
import java.util.Objects;

public class TradeInformation {

    private  List<OrderInformation> orderInformations;

    public void setOrderInformation(List<OrderInformation> orderInformation) {
        Objects.requireNonNull(orderInformation);
        if (orderInformation.size() == 0) throw new IllegalArgumentException("orderInformation size 0");

        this.orderInformations = orderInformation;
    }

    public TradeInformation(List<OrderInformation> orderInformation) {
        setOrderInformation(orderInformation);
    }

    public List<OrderInformation> getOrderInformations() {
        return orderInformations;
    }

    @Override
    public String toString() {
        return "TradeInformation{" +
                "orderInformations=" + orderInformations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeInformation that = (TradeInformation) o;
        return getOrderInformations().equals(that.getOrderInformations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderInformations());
    }
}
