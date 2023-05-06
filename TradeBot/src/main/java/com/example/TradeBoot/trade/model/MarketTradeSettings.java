package com.example.TradeBoot.trade.model;

import java.util.*;

public class MarketTradeSettings {

    public MarketTradeSettings()
    {

    }

    public void setMarket(String market) {
        this.market = market;
    }

    public void setTradingDelay(long tradingDelay) {
        this.tradingDelay = tradingDelay;
    }

    private String market;

    private long tradingDelay;

    public long getTradingDelay() {
        return tradingDelay;
    }

    private Optional<List<OrderInformation>> longOrderInformations;

    private Optional<List<OrderInformation>> shortOrderInformations;
    public Map<ETradingStrategy, List<OrderInformation>> getAllOrdersInformation(){
        Map<ETradingStrategy,List<OrderInformation>> allOrdersInformation = new HashMap<>();

        shortOrderInformations.ifPresent(
                orderInformations -> allOrdersInformation.put(ETradingStrategy.SHORT, orderInformations));
        longOrderInformations.ifPresent(
                orderInformations -> allOrdersInformation.put(ETradingStrategy.LONG, orderInformations));

        return allOrdersInformation;
    }


    public Optional<List<OrderInformation>> getLongOrderInformations() {
        return longOrderInformations;
    }

    public Optional<List<OrderInformation>> getShortOrderInformations() {
        return shortOrderInformations;
    }
    public String getMarket() {
        return market;
    }

}