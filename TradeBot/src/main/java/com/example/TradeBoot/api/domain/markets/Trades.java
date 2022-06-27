package com.example.TradeBoot.api.domain.markets;

import java.util.List;

public class Trades {
    List<Trade> trades;

    public Trades(List<Trade> trades) {
        this.trades = trades;
    }

    public List<Trade> getTrades() {
        return this.trades;
    }

    public Trade getTrade(int index) {
        return this.trades.get(index);
    }
}
