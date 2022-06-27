package com.example.TradeBoot.trade.model;

public class MarketInformation {

    private final String market;
    private long tradingDelay;


    public MarketInformation(
            String market,
            long tradingDelay
    ) {
        this.market = market;
        this.tradingDelay = tradingDelay;


    }

    public long getTradingDelay() {
        return tradingDelay;
    }


    public String getMarket() {
        return market;
    }

    @Override
    public String toString() {
        return "MarketInformation{" +
                "market='" + market + '\'' +
                ", tradingDelay=" + tradingDelay +
                '}';
    }
}
