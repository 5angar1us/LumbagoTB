package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;

import java.math.BigDecimal;

public class FinancialInstrumentPositionsService {

    private FinancialInstrumentService financialInstrumentService;

    private final IWalletService walletService;

    private IPositionsService positionsService;

    public FinancialInstrumentPositionsService(FinancialInstrumentService financialInstrumentService, IWalletService walletService, IPositionsService iPositionsService) {
        this.financialInstrumentService = financialInstrumentService;
        this.walletService = walletService;
        this.positionsService = iPositionsService;
    }


    boolean isPositionOpen(String marketName) {
        var instrumentType = financialInstrumentService.getInstrumentType(marketName);
        return switch (instrumentType) {
            case COIN -> isTotalBalanceIsNotZero(getTotalCostAsCoin(marketName));
            case FUTURE -> isTotalBalanceIsNotZero(getTotalCostAsFuture(marketName));
            case EMPTY -> throw new IllegalArgumentException(String.valueOf(instrumentType));
        };

    }

    private BigDecimal getTotalCostAsCoin(String marketName){
        var balance = walletService.getBalanceByMarketOrTrow(marketName);
        return balance.getTotal().abs();
    }

    private BigDecimal getTotalCostAsFuture(String marketName){
        var position = positionsService.getPositionByMarketOrTrow(marketName);
       return position.getNetSize();
    }



    private boolean isTotalBalanceIsNotZero(BigDecimal totalCost) {
        return !BigDecimalUtils.check(
                totalCost,
                BigDecimalUtils.EOperator.EQUALS,
                BigDecimal.ZERO
        );

    }
}
