package com.example.TradeBoot.trade.services.tradingEngine;

import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.trade.ExtendedExecutor;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.TradingService;
import com.example.TradeBoot.ui.service.ITradeSettingsService;
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
    protected final IMarketService marketService;

    protected final IWalletService walletService;

    protected final OrderPriceCalculator orderPriceCalculator;

    protected final ClosePositionInformationService closePositionInformationService;

    protected List<MarketTradeSettings> marketTradeSettings;


    protected List<TradingOrderInfoPair> trapLimitPositionPairs;

    protected TradeStatus tradeStatus = new TradeStatus(true);

    protected ITradeSettingsService tradeSettingsService;


    protected List<ITradingRunnableEngine> engines = new ArrayList<>();
    protected ExtendedExecutor executorService;

    protected FinancialInstrumentPositionsService financialInstrumentPositionsService;

    @Autowired
    public AbstractTradingEngineService(
            OrdersService ordersService,
            IMarketService marketService,
            IWalletService walletService,
            OrderPriceCalculator orderPriceCalculator,
            ClosePositionInformationService closePositionInformationService,
            ITradeSettingsService tradeSettingsService,
            FinancialInstrumentPositionsService financialInstrumentPositionsService
            ) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.walletService = walletService;
        this.orderPriceCalculator = orderPriceCalculator;
        this.closePositionInformationService = closePositionInformationService;
        this.tradeSettingsService = tradeSettingsService;
        this.financialInstrumentPositionsService = financialInstrumentPositionsService;
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


    public void correctStart() {
        saveStop();
        launch(tradeSettingsService.findAll());
    }


    abstract void launch(List<TradeSettings> marketTradeSettings);


    protected List<OrderInformation> createOrderInformation(TradeSettingsDetail tradeSettingsDetail) {
        List<OrderInformation> result = new ArrayList<OrderInformation>();
        var volume = new BigDecimal(tradeSettingsDetail.getVolume());
        var distanceInPercent = new Persent(tradeSettingsDetail.getPriceOffset());
        switch (tradeSettingsDetail.getTradingStrategy()) {
            case ALL -> {
                result.add(new OrderInformation(volume, ESide.SELL, distanceInPercent));
                result.add(new OrderInformation(volume, ESide.BUY, distanceInPercent));
            }
            case LONG -> {
                result.add(new OrderInformation(volume, ESide.BUY, distanceInPercent));
            }
            case SHORT -> {
                result.add(new OrderInformation(volume, ESide.SELL, distanceInPercent));
            }
            default -> throw new RuntimeException("incorrect tradingStrategyValue" + tradeSettingsDetail);
        }
        return result;
    }

    protected List<TradingOrderInfoPair> createTrapLimitPositionPairs(TradeSettings tradeSettings) {
        List<TradingOrderInfoPair> tradingOrderInfoPairPairs = new ArrayList<TradingOrderInfoPair>();


        var marketInformation = createMarketInformation(tradeSettings);

        var tradeInformation = tradeSettings.getTradeSettingsDetails()
                .stream()
                .map(this::createOrderInformation)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        tradingOrderInfoPairPairs.add(
                new TradingOrderInfoPair(
                        createTrapLimitOrdersService(marketInformation, new Persent(tradeSettings.getMaximumDefinition())),
                        new TradeInformation(tradeInformation),
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
                financialInstrumentPositionsService,
                tradeStatus
        );
    }


}








