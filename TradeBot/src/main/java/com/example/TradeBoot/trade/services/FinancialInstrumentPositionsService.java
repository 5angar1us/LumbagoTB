package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.EInstrumentType;
import com.example.TradeBoot.api.utils.BigDecimalUtils;
import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FinancialInstrumentPositionsService {

    private final FinancialInstrumentService financialInstrumentService;

    private final IWalletService walletService;

    private final IPositionsService positionsService;

    public FinancialInstrumentPositionsService(FinancialInstrumentService financialInstrumentService, IWalletService walletService, IPositionsService iPositionsService) {
        this.financialInstrumentService = financialInstrumentService;
        this.walletService = walletService;
        this.positionsService = iPositionsService;
    }

    public boolean isPositionOpen(String marketName) {
        return isNotZero(getPositionNetSize(marketName));
    }

    public boolean isPositionOpen(BigDecimal volume){
        return isNotZero(volume);
    }

    public BigDecimal getPositionNetSize(String marketName){
        var instrumentType = financialInstrumentService.getInstrumentType(marketName);
        return getPositionNetSizeByType(instrumentType, marketName);
    }

    private BigDecimal getPositionNetSizeByType(EInstrumentType instrumentType, String marketName){
        return switch (instrumentType) {
            case COIN -> CoinBalanceSizeVisitor(marketName);
            case FUTURE ->  FutureBalanceSizeVisitor(marketName);
            case EMPTY -> throw new IllegalArgumentException(String.valueOf(instrumentType));
        };
    }

    private BigDecimal CoinBalanceSizeVisitor(String marketName){
        var balance = walletService.getBalanceByMarketOrTrow(marketName);
        return balance.getTotal();
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
