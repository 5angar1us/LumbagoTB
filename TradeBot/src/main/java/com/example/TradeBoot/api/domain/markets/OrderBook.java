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

    private List<OrderBookLine> asks;

    private List<OrderBookLine> bids;

    public OrderBook(List<List<BigDecimal>> asks, List<List<BigDecimal>> bids) {
        setAsks(asks);
        setBids(bids);
    }

    public OrderBook() {}

    public void setBids(List<List<BigDecimal>> bids) {
        this.bids = bids.stream().map(BidOrderBookLine::new).collect(Collectors.toList());
    }

    public void setAsks(List<List<BigDecimal>> asks) {
        this.asks = asks.stream().map(AskOrderBookLine::new).collect(Collectors.toList());
    }

    public List<OrderBookLine> getAllAsks() {
        return this.asks;
    }

    public List<OrderBookLine> getAllBids() {
        return this.bids;
    }

    public OrderBookLine getBestAsk() {
        return this.asks.get(0);
    }

    public OrderBookLine getBestBid() {
        return this.bids.get(0);
    }

    public OrderBookLine getAsk(int number) {
        return this.asks.get(number);
    }

    public OrderBookLine getBid(int count) {
        return this.bids.get(count);
    }


    public OrderBookLine getBestBySide(ESide side){
        return switch (side){
            case BUY -> getBestBid();
            case SELL -> getBestAsk();
            default -> throw new IllegalArgumentException("side");
        };
    }

    public List<OrderBookLine> getAllBySide(ESide side){
        return switch (side){
            case BUY -> getAllBids();
            case SELL -> getAllAsks();
            default -> throw new IllegalArgumentException("side");
        };
    }
}
