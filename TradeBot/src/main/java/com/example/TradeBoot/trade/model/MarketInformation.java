package com.example.TradeBoot.trade.model;

public record MarketInformation(String market, long tradingDelay, Persent maximumDivination) {

    @Override
    public String toString() {
        return "MarketInformation{" +
                "market='" + market + '\'' +
                ", tradingDelay=" + tradingDelay +
                ", maximumDiviantion=" + maximumDivination +
                '}';
    }
}
