package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.IPositionsService;

import java.math.BigDecimal;

public class FutureHandler implements Ihandler{

    IPositionsService positionsService;

    public FutureHandler(IPositionsService iPositionsService) {
        positionsService = iPositionsService;
    }

    @Override
    public BigDecimal handle(String marketName) {
        var position = positionsService.getPositionByMarketOrTrow(marketName);
        var volume = position.getNetSize();
        return volume;
    }
}
