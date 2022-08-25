package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.domain.EInstrumentType;
import com.example.TradeBoot.api.utils.BigDecimalUtils;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.api.services.IPositionsService;
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

    private VolumeVisitor.CoinVolumeVisitor coinVolumeVisitor;

    private VolumeVisitor.FutureVolumeVisitor futureVolumeVisitor;

    @Autowired
    public ClosePositionInformationService(
            IWalletService walletService,
            FinancialInstrumentService financialInstrumentService,
            IPositionsService IPositionsService,
            VolumeVisitor.CoinVolumeVisitor coinVolumeVisitor,
            VolumeVisitor.FutureVolumeVisitor futureVolumeVisitor) {
        this.walletService = walletService;
        this.financialInstrumentService = financialInstrumentService;
        this.IPositionsService = IPositionsService;
        this.coinVolumeVisitor = coinVolumeVisitor;
        this.futureVolumeVisitor = futureVolumeVisitor;
    }


    public synchronized Optional<OrderInformation> createTradeInformation(BigDecimal netVolume) {

        var newSide = getSideBy(netVolume);

        var isTotalVolumeIsZero = BigDecimalUtils.check(
                netVolume,
                BigDecimalUtils.EOperator.EQUALS,
                BigDecimal.ZERO
        );

        if (isTotalVolumeIsZero || (newSide == ESide.EMPTY))
            return Optional.empty();

        return Optional.of(new OrderInformation(netVolume.abs(), newSide, new Persent(0)));
    }

    private BigDecimal getVolume(String marketName, EInstrumentType instrumentType) {
        return switch (instrumentType) {
            case COIN -> coinVolumeVisitor.getVolume(marketName);
            case FUTURE -> futureVolumeVisitor.getVolume(marketName);
            case EMPTY -> throw new IllegalArgumentException(String.valueOf(instrumentType));
        };
    }

    public ESide getSideBy(BigDecimal volume) {
        var newSide = ESide.EMPTY;
        if (BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.GREATER_THAN, BigDecimal.ZERO)) {
            newSide = ESide.SELL;
        } else if (BigDecimalUtils.check(volume, BigDecimalUtils.EOperator.LESS_THAN, BigDecimal.ZERO)) {
            newSide = ESide.BUY;
        }

        return newSide;
    }
}

