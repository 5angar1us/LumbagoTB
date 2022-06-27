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

    private List<Ask> asks;

    private List<Bid> bids;

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

    public List<Ask> getAllAsks() {
        return this.asks;
    }

    public List<Bid> getAllBids() {
        return this.bids;
    }

    public Ask getBestAsk() {
        return this.asks.get(0);
    }

    public Bid getBestBid() {
        return this.bids.get(0);
    }

    public Ask getAsk(int number) {
        return this.asks.get(number);
    }

    public Bid getBid(int count) {
        return this.bids.get(count);
    }


}
