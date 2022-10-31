package com.example.TradeBoot.trade;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.http.delay.MarketDelayFactory;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.notification.INotificationService;
import com.example.TradeBoot.notification.telegram.TelegramNotificationService;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import com.example.TradeBoot.trade.services.IPositionStatusService;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.tradeloop.*;
import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TradingRunnableEngineFactory {

    public TradingRunnableEngineFactory(IHttpClientWorker httpClientWorker, OrderPriceService orderPriceService, IMarketService marketService, ClosePositionInformationService closePositionInformationService, FinancialInstrumentPositionsService financialInstrumentPositionsService, INotificationService notificationService, MarketDelayFactory marketDelayFactory) {
        this.httpClientWorker = httpClientWorker;
        this.orderPriceService = orderPriceService;
        this.marketService = marketService;
        this.closePositionInformationService = closePositionInformationService;
        this.financialInstrumentPositionsService = financialInstrumentPositionsService;
        this.notificationService = notificationService;
        this.marketDelayFactory = marketDelayFactory;
    }

    private final IHttpClientWorker httpClientWorker;
    private final OrderPriceService orderPriceService;

    private final IMarketService marketService;

    private final ClosePositionInformationService closePositionInformationService;

    private final FinancialInstrumentPositionsService financialInstrumentPositionsService;

    private final INotificationService notificationService;

    private final MarketDelayFactory marketDelayFactory;

    private WorkStatus globalWorkStatus;

    public List<TradingRunnableEngine> createTradingRunnableEngines(List<TradeSettings> marketTradeSettings, WorkStatus globalWorkStatus) {
        var trapLimitPositionPairs = marketTradeSettings.stream()
                .map(this::createTrapLimitPositionPairs)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.globalWorkStatus = globalWorkStatus;

        return trapLimitPositionPairs.stream()
                .map(this::createEngineForMarket)
                .collect(Collectors.toList());
    }

    private TradingRunnableEngine createEngineForMarket(TradingOrderInfoPair tradingOrderInfoPair) {

        MarketInformation marketInformation = tradingOrderInfoPair.marketInformation();

        TradeInformation tradeInformation = tradingOrderInfoPair.tradeInformation();

        IOrdersService.Abstract ordersService = tradingOrderInfoPair.ordersService();

        var closeOrders = new CloseByOne(ordersService, marketInformation);

        var replaceOrders = new ReplaceMapOrderByOne(ordersService);

        var replaceOrder = new ReplaceByOne(ordersService);

        var openPositionStatus = new IPositionStatusService.OpenPositionStatusService(financialInstrumentPositionsService);

        var PlaceTraps = new PlaceTraps(
                ordersService,
                marketService,
                openPositionStatus,
                orderPriceService,
                replaceOrders,
                tradeInformation,
                marketInformation,
                globalWorkStatus);

        var saleProduction = new SaleProduction(
                marketService,
                ordersService,
                new OrderPriceService(),
                financialInstrumentPositionsService,
                closePositionInformationService,
                replaceOrder,
                marketInformation,
                globalWorkStatus
        );


        var placeTrapOrdersTradeLoop = new LocalTradeLoop(
                globalWorkStatus,
                PlaceTraps,
                closeOrders,
                notificationService
        );

        var saleProductionTradeLoop = new LocalTradeLoop(
                globalWorkStatus,
                saleProduction,
                closeOrders,
                notificationService
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
    }


    private List<OrderInformation> createOrderInformation(TradeSettingsDetail tradeSettingsDetail) {
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

    private List<TradingOrderInfoPair> createTrapLimitPositionPairs(TradeSettings tradeSettings) {
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
                        new TradeInformation(tradeInformation),
                        new IOrdersService.Base(marketDelayFactory.create(httpClientWorker)))
        );

        return tradingOrderInfoPairPairs;
    }

    private MarketInformation createMarketInformation(TradeSettings tradeSettings) {
        return new MarketInformation(
                tradeSettings.getMarketName(),
                tradeSettings.getTradeDelay(),
                new Persent(tradeSettings.getMaximumDefinition()));
    }

    record TradingOrderInfoPair(MarketInformation marketInformation, TradeInformation tradeInformation,
                                IOrdersService.Abstract ordersService) {
    }
}
