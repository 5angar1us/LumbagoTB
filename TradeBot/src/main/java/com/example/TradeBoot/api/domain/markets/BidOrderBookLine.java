package com.example.TradeBoot.api.domain.markets;

import java.math.BigDecimal;
import java.util.List;

public class BidOrderBookLine extends OrderBookLine.Abstract {
    public BidOrderBookLine(List<BigDecimal> position) {
        super(position);
    }
}
