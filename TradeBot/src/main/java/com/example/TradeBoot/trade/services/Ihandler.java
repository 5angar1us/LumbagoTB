package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;

import java.math.BigDecimal;

public interface Ihandler {
    OpenPositionInfo handle(ESide baseSide, String marketName);
}

record OpenPositionInfo(ESide baseSide, BigDecimal volume, BigDecimal totalCost) {}
