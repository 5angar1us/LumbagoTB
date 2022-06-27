package com.example.TradeBoot.api.domain.markets;

import java.math.BigDecimal;
import java.util.List;

public class Ask extends Price.Abstract {
    public Ask(List<BigDecimal> position) {
        super(position);
    }
}