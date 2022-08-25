package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.trade.ExtendedExecutor;
import com.example.TradeBoot.trade.TradingRunnableEngine;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.tradeloop.*;
import com.example.TradeBoot.trade.tradeloop.interfaces.ITradeService;
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
    protected final IOrdersService.Abstract ordersService;
    protected final IMarketService marketService;

    protected final OrderPriceService orderPriceService;

    protected final ClosePositionInformationService closePositionInformationService;

    protected WorkStatus globalWorkStatus = new WorkStatus(true);

    protected ITradeSettingsService tradeSettingsService;


    protected List<TradingRunnableEngine> engines = new ArrayList<>();
    protected ExtendedExecutor executorService;

    protected FinancialInstrumentPositionsService financialInstrumentPositionsService;

    @Autowired
    public TradingEngineService(
            IOrdersService.Abstract ordersService,
            IMarketService marketService,
            OrderPriceService orderPriceService,
            ClosePositionInformationService closePositionInformationService,
            ITradeSettingsService tradeSettingsService,
            FinancialInstrumentPositionsService financialInstrumentPositionsService
    ) {
        this.ordersService = ordersService;
        this.marketService = marketService;

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
        globalWorkStatus.setNeedStop(true);

        if (this.executorService == null)
            return;

        this.executorService.shutdown();
    }

    public boolean isDone(long timeout, TimeUnit timeUnit) throws InterruptedException {
        if (executorService == null) return true;

        return executorService.awaitTermination(timeout, timeUnit);
    }

    public boolean isStop() {
        return globalWorkStatus.isNeedStop();
    }


    public void correctStart() {
        saveStop();
        launch(tradeSettingsService.findAll());
    }

    void launch(List<TradeSettings> marketTradeSettings) {
        this.engines.clear();

        globalWorkStatus.setNeedStop(false);

        var trapLimitPositionPairs = marketTradeSettings.stream()
                .map(this::createTrapLimitPositionPairs)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.executorService = new ExtendedExecutor(trapLimitPositionPairs.size());



        this.engines = trapLimitPositionPairs.stream()
                .map(tradingOrderInfoPair -> {

                    MarketInformation marketInformation = tradingOrderInfoPair.marketInformation;

                    TradeInformation tradeInformation = tradingOrderInfoPair.tradeInformation();

                    var closeOrders = new CloseByOne(ordersService, marketInformation);

                    var placeWithDelay = new  PlaceWithDelay(ordersService);

                    var replaceOrders = new ReplaceMapOrderByOne(ordersService, placeWithDelay);

                    var replaceOrder = new ReplaceByOne(ordersService, placeWithDelay);


                    var openPositionStatus = new IPositionStatusService.OpenPositionStatusService(financialInstrumentPositionsService);

                    var PlaceTraps = new PlaceTraps(
                            ordersService,
                            marketService,
                            openPositionStatus,
                            orderPriceService,
                            replaceOrders,
                            placeWithDelay,
                            tradeInformation,
                            marketInformation,
                            globalWorkStatus);

                    var saleProduction = new SaleProduction(
                            marketService,
                            new OrderPriceService(),
                            financialInstrumentPositionsService,
                            closePositionInformationService,
                            placeWithDelay,
                            replaceOrder,
                            marketInformation,
                            globalWorkStatus
                    );



                    var placeTrapOrdersTradeLoop = new LocalTradeLoop(
                            PlaceTraps,
                            closeOrders,
                            globalWorkStatus
                    );

                    var saleProductionTradeLoop = new LocalTradeLoop(
                            saleProduction,
                            closeOrders,
                            globalWorkStatus
                    );

                    var tradeService = new GlobalTradeLoop(
                            placeTrapOrdersTradeLoop,
                            saleProductionTradeLoop,
                            globalWorkStatus
                    );

                    return new TradingRunnableEngine(
                            marketInformation.market(),
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
                        marketInformation,
                        new TradeInformation(tradeInformation)
                ));

        return tradingOrderInfoPairPairs;
    }

    protected MarketInformation createMarketInformation(TradeSettings tradeSettings) {
        return new MarketInformation(
                tradeSettings.getMarketName(),
                tradeSettings.getTradeDelay(),
                new Persent(tradeSettings.getMaximumDefinition()));
    }

    public static record TradingOrderInfoPair( MarketInformation marketInformation, TradeInformation tradeInformation) {
    }
}








