package com.example.TradeBoot.trade.services.tradingEngine;

import com.example.TradeBoot.ExtendedExecutor;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.TradingService;
import com.example.TradeBoot.ui.ITradeSettingsService;
import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AbstractTradingEngineService {

    static final Logger log =
            LoggerFactory.getLogger(AbstractTradingEngineService.class);
    protected final OrdersService ordersService;
    protected final MarketService marketService;

    protected final WalletService walletService;

    protected final OrderPriceCalculator orderPriceCalculator;

    protected final ClosePositionInformationService closePositionInformationService;

    protected List<MarketTradeSettings> marketTradeSettings;


    protected List<TradingOrderInfoPair> trapLimitPositionPairs;

    protected TradeStatus tradeStatus = new TradeStatus(false);

    protected ITradeSettingsService tradeSettingsService;


    protected List<ITradingRunnableEngine> engines = new ArrayList<>();
    protected ExtendedExecutor executorService;

    @Autowired
    public AbstractTradingEngineService(
            OrdersService ordersService,
            MarketService marketService,
            WalletService walletService,
            OrderPriceCalculator orderPriceCalculator,
            ClosePositionInformationService closePositionInformationService,
            ITradeSettingsService tradeSettingsService) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.walletService = walletService;
        this.orderPriceCalculator = orderPriceCalculator;
        this.closePositionInformationService = closePositionInformationService;
        this.tradeSettingsService = tradeSettingsService;
    }


    public long runnableEnginesCount() {

        if(engines.size() == 0) return 0;

        return engines.stream()
                .filter(engine -> engine.isStopped() == false)
                .count();
    }

    public void saveStop() {
        tradeStatus.setNeedStop(true);

        if (this.executorService == null)
            return;

        this.executorService.shutdown();
    }

    public boolean isDone(long timeout, TimeUnit timeUnit) throws InterruptedException {
        if(executorService == null) return true;

        return executorService.awaitTermination(timeout, timeUnit);
    }

    public boolean isStop() {
        return tradeStatus.isNeedStop();
    }


    public void currectStart() {
        saveStop();
        launch(tradeSettingsService.findAll());
    }


    abstract void launch(List<TradeSettings> marketTradeSettings);


    protected List<OrderInformation> createOrderInformtaion(TradeSettingsDetail tradeSettingsDetail) {
        List<OrderInformation> result = new ArrayList<OrderInformation>();
        var volume = new BigDecimal(tradeSettingsDetail.getVolume());
        var distanceInPersent = new Persent(tradeSettingsDetail.getPriceOffset());
        switch (tradeSettingsDetail.getTradingStrategy()) {
            case ALL -> {
                result.add(new OrderInformation(volume, ESide.SELL, distanceInPersent));
                result.add(new OrderInformation(volume, ESide.BUY, distanceInPersent));
            }
            case LONG -> {
                result.add(new OrderInformation(volume, ESide.BUY, distanceInPersent));
            }
            case SHORT -> {
                result.add(new OrderInformation(volume, ESide.SELL, distanceInPersent));
            }
            default -> throw new RuntimeException("incorrect tradingStrategyValue" + tradeSettingsDetail);
        }
        return result;
    }

    protected List<TradingOrderInfoPair> createTrapLimitPositionPairs(TradeSettings tradeSettings) {
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

    protected MarketInformation createMarketInformation(TradeSettings tradeSettings) {
        return new MarketInformation(
                tradeSettings.getMarketName(),
                tradeSettings.getTradeDelay());
    }

    protected TradingService createTrapLimitOrdersService(
            MarketInformation marketInformation, Persent maximumDivination) {
        return new TradingService(
                ordersService,
                marketService,
                orderPriceCalculator,
                marketInformation,
                maximumDivination,
                log,
                tradeStatus
        );
    }


}








