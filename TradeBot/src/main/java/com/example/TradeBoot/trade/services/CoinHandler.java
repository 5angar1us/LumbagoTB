package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.services.IWalletService;

import java.math.BigDecimal;

public class CoinHandler implements Ihandler {

    private IWalletService walletService;

    public CoinHandler(IWalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public BigDecimal handle(String marketName) {
        var balance = walletService.getBalanceByMarketOrTrow(marketName);
        var volume = balance.getTotal();
        return volume;
    }
}
