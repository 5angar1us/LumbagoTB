package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.utils.BigDecimalUtils;
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


    public boolean isPositionOpen(String marketName) {
        var instrumentType = financialInstrumentService.getInstrumentType(marketName);
        var balanceSize = switch (instrumentType) {
            case COIN -> CoinBalanceSizeVisitor(marketName);
            case FUTURE -> FutureBalanceSizeVisitor(marketName);
            case EMPTY -> throw new IllegalArgumentException(String.valueOf(instrumentType));
        };
        return isNotZero(balanceSize);
    }

    private BigDecimal CoinBalanceSizeVisitor(String marketName){
        var balance = walletService.getBalanceByMarketOrTrow(marketName);
        return balance.getTotal().abs();
    }

    private BigDecimal FutureBalanceSizeVisitor(String marketName){
        var position = positionsService.getPositionByMarketOrTrow(marketName);
       return position.getNetSize();
    }

    private boolean isNotZero(BigDecimal balanceValue) {
        return !BigDecimalUtils.check(
                balanceValue,
                BigDecimalUtils.EOperator.EQUALS,
                BigDecimal.ZERO
        );

    }
}
