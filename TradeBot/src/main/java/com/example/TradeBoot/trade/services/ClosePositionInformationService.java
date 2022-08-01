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


    public Optional<TradeInformation> createTradeInformation(String marketName) {

        var instrumentType = financialInstrumentService.getInstrumentType(marketName);
        return switch (instrumentType) {
            case COIN -> handle(coinHandler.handle(marketName));
            case FUTURE -> handle(futureHandler.handle(marketName));
            case EMPTY -> throw new IllegalArgumentException(String.valueOf(instrumentType));
        };
    }


    private Optional<TradeInformation> handle(BigDecimal volume) {

        var isTotalVolumeIsZero = BigDecimalUtils.check(
                volume,
                BigDecimalUtils.EOperator.EQUALS,
                BigDecimal.ZERO
        );

        var newSide = createNewSide(volume);

        var isNeedSell = newSide == ESide.SELL
                && BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.GREATER_THAN, BigDecimal.ZERO);

        var isNeedBuy = newSide == ESide.BUY
                && BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.LESS_THAN, BigDecimal.ZERO);

        if (isTotalVolumeIsZero || (newSide == ESide.EMPTY))
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

    private ESide createNewSide(BigDecimal volume){
        var newSide = ESide.EMPTY;
        if (BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.GREATER_THAN, BigDecimal.ZERO)) {
            newSide = ESide.SELL;
        } else if (BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.LESS_THAN, BigDecimal.ZERO)) {
            newSide = ESide.BUY;
        }

        return newSide;
    }
}

