package com.example.TradeBoot.api.domain.markets;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderBook {
    @Override
    public String toString() {
        return "OrderBook{" +
                "asks=" + asks +
                ", bids=" + bids +
                '}';
    }

    private List<Price> asks;

    private List<Price> bids;

    public OrderBook(List<List<BigDecimal>> asks, List<List<BigDecimal>> bids) {
        setAsks(asks);
        setBids(bids);
    }

    public OrderBook() {}

    public void setBids(List<List<BigDecimal>> bids) {
        this.bids = bids.stream().map(Bid::new).collect(Collectors.toList());
    }

    public void setAsks(List<List<BigDecimal>> asks) {
        this.asks = asks.stream().map(Ask::new).collect(Collectors.toList());
    }

    public List<Price> getAllAsks() {
        return this.asks;
    }

    public List<Price> getAllBids() {
        return this.bids;
    }

    public Price getBestAsk() {
        return this.asks.get(0);
    }

    public Price getBestBid() {
        return this.bids.get(0);
    }

    public Price getAsk(int number) {
        return this.asks.get(number);
    }

    public Price getBid(int count) {
        return this.bids.get(count);
    }


    public Price getBestBySide(ESide side){
        return switch (side){
            case BUY -> getBestBid();
            case SELL -> getBestAsk();
            default -> throw new IllegalArgumentException("side");
        };
    }

    public List<Price> getAllBySide(ESide side){
        return switch (side){
            case BUY -> getAllBids();
            case SELL -> getAllAsks();
            default -> throw new IllegalArgumentException("side");
        };
    }

}
