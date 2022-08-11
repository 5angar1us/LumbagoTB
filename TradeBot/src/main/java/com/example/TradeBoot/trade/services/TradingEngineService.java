package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IWalletService;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.trade.ExtendedExecutor;
import com.example.TradeBoot.trade.TradingRunnableEngine;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.ui.service.ITradeSettingsService;
import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TradingEngineService {

    static final Logger log =
            LoggerFactory.getLogger(TradingEngineService.class);
    protected final OrdersService ordersService;
    protected final IMarketService marketService;

    protected final IWalletService walletService;

    protected final OrderPriceService orderPriceService;

    protected final ClosePositionInformationService closePositionInformationService;

    protected List<MarketTradeSettings> marketTradeSettings;


    protected List<TradingOrderInfoPair> trapLimitPositionPairs;

    protected WorkStatus workStatus = new WorkStatus(true);

    protected ITradeSettingsService tradeSettingsService;


    protected List<TradingRunnableEngine> engines = new ArrayList<>();
    protected ExtendedExecutor executorService;

    protected FinancialInstrumentPositionsService financialInstrumentPositionsService;

    @Autowired
    public TradingEngineService(
            OrdersService ordersService,
            IMarketService marketService,
            IWalletService walletService,
            OrderPriceService orderPriceService,
            ClosePositionInformationService closePositionInformationService,
            ITradeSettingsService tradeSettingsService,
            FinancialInstrumentPositionsService financialInstrumentPositionsService
    ) {
        this.ordersService = ordersService;
        this.marketService = marketService;
        this.walletService = walletService;
        this.orderPriceService = orderPriceService;
        this.closePositionInformationService = closePositionInformationService;
        this.tradeSettingsService = tradeSettingsService;
        this.financialInstrumentPositionsService = financialInstrumentPositionsService;
    }


    public long runnableEnginesCount() {

        if (engines.size() == 0) return 0;

        return engines.stream()
                .filter(engine -> engine.isStopped() == false)
                .count();
    }

    public void saveStop() {
        workStatus.setNeedStop(true);

        if (this.executorService == null)
            return;

        this.executorService.shutdown();
    }

    public boolean isDone(long timeout, TimeUnit timeUnit) throws InterruptedException {
        if (executorService == null) return true;

        return executorService.awaitTermination(timeout, timeUnit);
    }

    public boolean isStop() {
        return workStatus.isNeedStop();
    }


    public void correctStart() {
        saveStop();
        launch(tradeSettingsService.findAll());
    }

    void launch(List<TradeSettings> marketTradeSettings) {
        this.engines.clear();

        workStatus.setDefaultState();

        this.trapLimitPositionPairs = marketTradeSettings.stream()
                .map(this::createTrapLimitPositionPairs)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.executorService = new ExtendedExecutor(trapLimitPositionPairs.size());

        var openPositionStatus = new IPositionStatusService.OpenPositionStatusService(financialInstrumentPositionsService);
        var closePositionStatus = new IPositionStatusService.ClosePositionStatusService(financialInstrumentPositionsService);

        this.engines = trapLimitPositionPairs.stream()
                .map(tradingOrderInfoPair -> {

                    var tradeService = new TradeLoopService(tradingOrderInfoPair.tradeService(),
                            workStatus,
                            openPositionStatus,
                            closePositionStatus,
                            closePositionInformationService,
                            tradingOrderInfoPair.tradeInformation(),
                            tradingOrderInfoPair.market()
                    );

                    return new TradingRunnableEngine(
                            tradingOrderInfoPair.market(),
                            tradeService
                    );
                })
                .collect(Collectors.toList());

        Objects.requireNonNull(this.executorService);

        engines.forEach(executorService::submit);
    }


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

    protected TradeService createTrapLimitOrdersService(
            MarketInformation marketInformation, Persent maximumDivination) {
        return new TradeService(
                ordersService,
                marketService,
                orderPriceService,
                marketInformation,
                maximumDivination,
                financialInstrumentPositionsService,
                workStatus
        );
    }


    public static record TradingOrderInfoPair(TradeService tradeService, TradeInformation tradeInformation, String market) {
    }
}








