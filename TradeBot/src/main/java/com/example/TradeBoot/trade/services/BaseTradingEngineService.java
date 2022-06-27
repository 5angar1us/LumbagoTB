package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.ExtendedExecutor;
import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.ui.models.TradeSettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class BaseTradingEngineService extends TradingEngineService {


    private List<TradingRunnableEngine> engines = new ArrayList<>();
    private ExtendedExecutor executorService;

    public BaseTradingEngineService(OrdersService ordersService, MarketService marketService, WalletService walletService, OrderPriceCalculator orderPriceCalculator, ClosePositionService closePositionService) {
        super(ordersService, marketService, walletService, orderPriceCalculator, closePositionService);
    }


    public int runnableEngineCount() {
        return engines.size();
    }

    public void saveStop() {
        isStop = true;

        if (this.executorService == null)
            return;

        this.engines.forEach(TradingRunnableEngine::stop);
        this.executorService.shutdown();
        this.engines.clear();
    }

    void launch(List<TradeSettings> marketTradeSettings) {

        isStop = false;

        this.trapLimitPositionPairs = marketTradeSettings.stream()
                .map(this::createTrapLimitPositionPairs)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.executorService = new ExtendedExecutor(trapLimitPositionPairs.size());

        this.engines = trapLimitPositionPairs.stream()
                .map(tradingOrderInfoPair -> {
                    return new TradingRunnableEngine(
                            tradingOrderInfoPair.tradingService(),
                            closePositionService,
                            tradingOrderInfoPair.tradeInformation(),
                            tradingOrderInfoPair.market());
                })
                .collect(Collectors.toList());

        Objects.requireNonNull(this.executorService);

        engines.forEach(executorService::submit);
    }

    private List<TradingOrderInfoPair> createTrapLimitPositionPairs(TradeSettings tradeSettings) {
        List<TradingOrderInfoPair> tradingOrderInfoPairPairs = new ArrayList<TradingOrderInfoPair>();


        var marketInformation = createMarketInformation(tradeSettings);

        var tradeInformations = tradeSettings.getTradeSettingsDetails()
                .stream()
                .map(this::createOrderInformtaion)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        tradingOrderInfoPairPairs.add(
                new TradingOrderInfoPair(
                        createTrapLimitOrdersService(marketInformation, new Persent(tradeSettings.getMaximumDefinition())),
                        new TradeInformation(tradeInformations),
                        marketInformation.getMarket()
                ));

        return tradingOrderInfoPairPairs;
    }
    public class TradingRunnableEngine implements Runnable {
        private boolean isStop = false;

        private TradingService tradingService;

        private TradeInformation sourceTradeInformation;
        private ClosePositionService closePositionService;

        private String market;

        public TradingRunnableEngine(TradingService tradingService, ClosePositionService closePositionService, TradeInformation sourceTradeInformation, String market) {
            this.tradingService = tradingService;
            this.closePositionService = closePositionService;
            this.sourceTradeInformation = sourceTradeInformation;
            this.market = market;
        }

        @Override
        public void run() {

            while (this.isStop == false) {

                tradingService.workWithOrders(sourceTradeInformation);

                var closePositionTradeInformation = closePositionService.createTradeInformation(
                        sourceTradeInformation.getBaseSide(),
                        market);

                if (closePositionTradeInformation.isPresent()) {
                    tradingService.workWithOrders(closePositionTradeInformation.get());
                }
            }

        }

        public void stop() {
            this.isStop = true;
        }
    }
}
