package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.WalletService;
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
    private  WalletService walletService;

    @Autowired
    public ClosePositionInformationService(WalletService walletService) {
        this.walletService = walletService;
    }

    public Optional<TradeInformation> createTradeInformation(ESide baseSide, String marketName) {

        var balance = walletService.getBalanceByMarket(marketName);

        if (balance.isEmpty()) return Optional.empty();

        var isTotalBalanceIsZero = BigDecimalUtils.check(
                balance.get().getTotal().abs(),
                BigDecimalUtils.EOperator.EQUALS,
                BigDecimal.ZERO
        );

        var newSide = ESideChange.change(baseSide);
        var volume = balance.get().getTotal();

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
