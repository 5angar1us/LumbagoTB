package com.example.TradeBoot.ui.service;

import com.example.TradeBoot.api.domain.orders.OpenOrder;
import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.ui.TradeSettingsRepositoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class StatusService {

    @Autowired
    private TradeSettingsRepositoryWrapper tradeSettingsRepositoryWrapper;

    @Autowired
    private OrdersService ordersService;

    public void getOpenOrdersByConfiguration() {
        var tradesSettings = StreamSupport.stream(tradeSettingsRepositoryWrapper.findAll().spliterator(), false)
                .map(tradeSettings -> tradeSettings.getMarketName())
                .map(ordersService::getOpenOrders)
                .filter(openOrders -> openOrders.size() > 0)
                .map(openOrders -> {
                    var first = openOrders.stream().findFirst().orElseThrow();
                    return new MarketOpenOrderSize(first.getMarket(), openOrders.size());
                })
                .collect(Collectors.toMap(MarketOpenOrderSize::marketName, MarketOpenOrderSize::size));
    }

    record MarketOpenOrderSize(String marketName, int size) {

    }

}
