package com.example.TradeBoot.api.domain.markets;

import java.math.BigDecimal;
import java.util.List;

public class Bid extends Price.Abstract {
    public Bid(List<BigDecimal> position) {
        super(position);
    }
}
