package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.MarketInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.ui.models.TradeSettings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockTrade extends TradingEngineService {
    public MockTrade(OrdersService ordersService, MarketService marketService, WalletService walletService, OrderPriceCalculator orderPriceCalculator, ClosePositionService closePositionService) {
        super(ordersService, marketService, walletService, orderPriceCalculator, closePositionService);
    }

    @Override
    public int runnableEngineCount() {
        return isStop ? 0 : 1;
    }

    @Override
    public void saveStop() {

    }

    @Override
    void launch(List<TradeSettings> marketTradeSettings) {
        var marketTradeSetting = marketTradeSettings.get(0);

        MarketInformation marketInformation = new MarketInformation(marketTradeSetting.getMarketName(), marketTradeSetting.getTradeDelay());
        TradingService tradingService = createTrapLimitOrdersService(marketInformation, new Persent(marketTradeSetting.getMaximumDefinition()));
        var tradeInformations = marketTradeSetting.getTradeSettingsDetails()
                .stream()
                .map(this::createOrderInformtaion)
                .flatMap(List::stream)
                .collect(Collectors.toList());


       var pair =  new TradingOrderInfoPair(
                createTrapLimitOrdersService(marketInformation , new Persent(marketTradeSetting.getMaximumDefinition())),
                new TradeInformation(tradeInformations),
                marketInformation.getMarket()
        );

        tradingService.workWithOrders(pair.tradeInformation());

        var closePositionTradeInformation = closePositionService.createTradeInformation(
                pair.tradeInformation().getBaseSide(),
                pair.market());

        if (closePositionTradeInformation.isPresent()) {
            tradingService.workWithOrders(closePositionTradeInformation.get());
        }

    }
}
