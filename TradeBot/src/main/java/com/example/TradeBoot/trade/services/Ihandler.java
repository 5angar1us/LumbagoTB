package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;

public interface Ihandler {
    BigDecimal handle(String marketName);

}

record OpenPositionInfo(ESide newSide, BigDecimal volume, BigDecimal totalCost) {}
