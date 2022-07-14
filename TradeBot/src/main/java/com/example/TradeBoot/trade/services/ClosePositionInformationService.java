package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.implemetations.IWalletService;
import com.example.TradeBoot.api.services.implemetations.IPositionsService;
import com.example.TradeBoot.api.utils.ESideChange;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.model.TradeInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClosePositionInformationService {
    private IWalletService walletService;

    private FinancialInstrumentService financialInstrumentService;

    private IPositionsService IPositionsService;

    @Autowired
    public ClosePositionInformationService(IWalletService walletService, FinancialInstrumentService financialInstrumentService, IPositionsService IPositionsService) {
        this.walletService = walletService;
        this.financialInstrumentService = financialInstrumentService;
        this.IPositionsService = IPositionsService;
    }


    public Optional<TradeInformation> createTradeInformation(ESide baseSide, String marketName) {

        var instrumentType = financialInstrumentService.getInstrumentType(marketName);
        return switch (instrumentType) {
            case COIN -> handleAsCoin(baseSide, marketName);
            case FUTURE -> handleAsFuture(baseSide, marketName);
            case EMPTY -> throw new IllegalArgumentException(String.valueOf(instrumentType));
        };
    }

    private Optional<TradeInformation> handleAsCoin(ESide baseSide, String marketName) {
        var balance = walletService.getBalanceByMarketOrTrow(marketName);
        var totalCost = balance.getTotal().abs();
        var volume = balance.getTotal();

        return handle(baseSide, volume, totalCost);
    }

    private Optional<TradeInformation> handleAsFuture(ESide baseSide, String marketName) {
        var position = IPositionsService.getPositionByMarketOrTrow(marketName);
        var volume = position.getNetSize();
        var totalCost = position.getCost();

        return handle(baseSide, volume, totalCost);
    }

    private Optional<TradeInformation> handle(ESide baseSide, BigDecimal volume, BigDecimal totalCost) {

        var isTotalBalanceIsZero = BigDecimalUtils.check(
                totalCost,
                BigDecimalUtils.EOperator.EQUALS,
                BigDecimal.ZERO
        );

        var newSide = ESideChange.change(baseSide);

        var isNeedSell = newSide == ESide.SELL
                && BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.GREATER_THAN, BigDecimal.ZERO);

        var isNeedBuy = newSide == ESide.BUY
                && BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.LESS_THAN, BigDecimal.ZERO);

        if (isTotalBalanceIsZero || (isNeedBuy == false & isNeedSell == false))
            return Optional.empty();

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(
                new OrderInformation(
                        volume.abs(),
                        newSide,
                        new Persent(0))
        );

        return Optional.of(
                new TradeInformation(orderInformations));
    }
}

