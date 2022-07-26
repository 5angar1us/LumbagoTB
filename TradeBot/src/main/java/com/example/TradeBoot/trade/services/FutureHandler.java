package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.IPositionsService;

public class FutureHandler implements Ihandler{

    IPositionsService positionsService;

    public FutureHandler(IPositionsService iPositionsService) {
        positionsService = iPositionsService;
    }

    @Override
    public OpenPositionInfo handle(ESide baseSide, String marketName) {
        var position = positionsService.getPositionByMarketOrTrow(marketName);
        var volume = position.getNetSize();
        var totalCost = position.getCost();

        return new OpenPositionInfo(baseSide, volume, totalCost);
    }
}
