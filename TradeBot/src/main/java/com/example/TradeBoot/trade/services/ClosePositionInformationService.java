package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.api.services.IPositionsService;
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

    private CoinHandler coinHandler;

    private FutureHandler futureHandler;

    @Autowired
    public ClosePositionInformationService(
            IWalletService walletService,
            FinancialInstrumentService financialInstrumentService,
            IPositionsService IPositionsService,
            CoinHandler coinHandler,
            FutureHandler futureHandler) {
        this.walletService = walletService;
        this.financialInstrumentService = financialInstrumentService;
        this.IPositionsService = IPositionsService;
        this.coinHandler = coinHandler;
        this.futureHandler = futureHandler;
    }


    public Optional<TradeInformation> createTradeInformation(ESide baseSide, String marketName) {

        var instrumentType = financialInstrumentService.getInstrumentType(marketName);
        return switch (instrumentType) {
            case COIN -> handle(coinHandler.handle(baseSide,marketName));
            case FUTURE -> handle(futureHandler.handle(baseSide, marketName));
            case EMPTY -> throw new IllegalArgumentException(String.valueOf(instrumentType));
        };
    }


    private Optional<TradeInformation> handle(OpenPositionInfo openPositionInfo) {

        var isTotalBalanceIsZero = BigDecimalUtils.check(
                openPositionInfo.totalCost(),
                BigDecimalUtils.EOperator.EQUALS,
                BigDecimal.ZERO
        );

        var newSide = ESideChange.change(openPositionInfo.baseSide());

        var isNeedSell = newSide == ESide.SELL
                && BigDecimalUtils.check(openPositionInfo.volume(), BigDecimalUtils.EOperator.GREATER_THAN, BigDecimal.ZERO);

        var isNeedBuy = newSide == ESide.BUY
                && BigDecimalUtils.check(openPositionInfo.volume(), BigDecimalUtils.EOperator.LESS_THAN, BigDecimal.ZERO);

        if (isTotalBalanceIsZero || (isNeedBuy == false & isNeedSell == false))
            return Optional.empty();

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(
                new OrderInformation(
                        openPositionInfo.volume().abs(),
                        newSide,
                        new Persent(0))
        );

        return Optional.of(
                new TradeInformation(orderInformations));
    }
}

