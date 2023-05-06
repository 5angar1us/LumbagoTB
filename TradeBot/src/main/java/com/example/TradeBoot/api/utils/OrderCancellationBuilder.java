package com.example.TradeBoot.api.utils;

import com.example.TradeBoot.api.domain.markets.ESide;

public class OrderCancellationBuilder extends AbstractParameterBuilder {

    public OrderCancellationBuilder() {
        super(4);
    }

    public OrderCancellationBuilder TargetMarket(String market) {
        addParameter("market", market);
        return this;
    }

    public OrderCancellationBuilder TargetSide(ESide side) {
        if (side != ESide.EMPTY) addParameter("side", side.toString());
        return this;
    }

    public OrderCancellationBuilder isConditionalOrdersOnly(boolean conditionalOrdersOnly) {
        addParameter("conditionalOrdersOnly", String.valueOf(conditionalOrdersOnly));
        return this;
    }

    public OrderCancellationBuilder isLimitOrdersOnly(boolean limitOrdersOnly) {
        addParameter("limitOrdersOnly", String.valueOf(limitOrdersOnly));
        return this;
    }
}
