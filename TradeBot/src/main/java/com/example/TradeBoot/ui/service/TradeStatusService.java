package com.example.TradeBoot.ui.service;

import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.ui.TradeSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StatusService {

    @Autowired
    private TradeSettingsService tradeSettingsService;


    @Autowired
    private WalletService walletService;

    @Autowired
    private OrdersService ordersService;


    private  List<String> ignoredBalanceNames = List.of("USD", "EUR");

    public List<MarketOpenOrderSize> getOpenOrdersByConfiguration() {
        var openOrdersMap = StreamSupport.stream(tradeSettingsService.findAll().spliterator(), false)
                .map(tradeSettings -> tradeSettings.getMarketName())
                .map(ordersService::getOpenOrders)
                .filter(openOrders -> openOrders.size() > 0)
                .map(openOrders -> {
                    var firstOrder = openOrders.stream().findFirst().orElseThrow();
                    return new MarketOpenOrderSize(firstOrder.getMarket(), openOrders.size());
                })
                .collect(Collectors.toList());

        return openOrdersMap;
    }

    record MarketOpenOrderSize(String marketName, int size) { }

    public List<OpenPositionInfo> getOpenPositions() {

        var openPositions = walletService.getBalances()
                .stream()
                .filter(balance -> ignoredBalanceNames.contains(balance.getCoin()) == false)
                .filter(balance -> {
                    return BigDecimalUtils.check(balance.getTotal(), BigDecimalUtils.EOperator.EQUALS, BigDecimal.ZERO) == false;
                })
                 .map(balance -> new OpenPositionInfo(balance.getCoin(), balance.getTotal()))
                .collect(Collectors.toList());

        return openPositions;
    }

    record OpenPositionInfo(String marketName, BigDecimal total) {}
}
