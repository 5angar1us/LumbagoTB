package com.example.TradeBoot.ui.service;

import com.example.TradeBoot.api.utils.BigDecimalUtils;
import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.api.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class TradeStatusService {

    @Autowired
    public TradeStatusService(ITradeSettingsService tradeSettingsService, IWalletService walletService, OrdersService ordersService, IPositionsService positionsService) {
        this.tradeSettingsService = tradeSettingsService;
        this.walletService = walletService;
        this.ordersService = ordersService;
        this.positionsService = positionsService;
    }


    private ITradeSettingsService tradeSettingsService;

    private IWalletService walletService;

    private OrdersService ordersService;

    private IPositionsService positionsService;


    private List<String> ignoredBalanceNames = List.of("USD", "EUR");

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

    public record MarketOpenOrderSize(String marketName, int size) {
    }

    public List<OpenPositionInfo> getOpenPositions() {

        var openCoinsPositions = walletService.getBalances()
                .stream()
                .filter(balance -> ignoredBalanceNames.contains(balance.getCoin()) == false)
                .filter(balance -> {
                    return BigDecimalUtils.check(balance.getTotal(), BigDecimalUtils.EOperator.EQUALS, BigDecimal.ZERO) == false;
                })
                .map(balance -> new OpenPositionInfo(balance.getCoin(), balance.getTotal()));

       var openFuturesPositions = positionsService.getAllPositions()
               .stream()
               .filter(position -> {
                   return BigDecimalUtils.check(position.getCost(), BigDecimalUtils.EOperator.EQUALS, BigDecimal.ZERO) == false;
               })
               .map(position -> new OpenPositionInfo(position.getFuture(), position.getNetSize()));

        return Stream.concat(openCoinsPositions, openFuturesPositions)
                .collect(Collectors.toList());
    }

    public record OpenPositionInfo(String marketName, BigDecimal total) {
    }

    public long getTradeSettingsCount() {
        return StreamSupport.stream(tradeSettingsService.findAll().spliterator(), false).count();
    }


}
