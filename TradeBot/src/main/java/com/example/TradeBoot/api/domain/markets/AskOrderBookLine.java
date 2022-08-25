package com.example.TradeBoot.api.domain.markets;

import java.math.BigDecimal;
import java.util.List;

public class AskOrderBookLine extends OrderBookLine.Abstract {
    public AskOrderBookLine(List<BigDecimal> position) {
        super(position);
    }
}