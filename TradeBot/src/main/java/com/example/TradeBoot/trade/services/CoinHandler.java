package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.IWalletService;

public class CoinHandler implements Ihandler{

    private IWalletService walletService;

    public CoinHandler(IWalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public OpenPositionInfo handle(ESide baseSide, String marketName) {
        var balance = walletService.getBalanceByMarketOrTrow(marketName);
        var totalCost = balance.getTotal().abs();
        var volume = balance.getTotal();

        return new OpenPositionInfo(baseSide, volume, totalCost);
    }
}
