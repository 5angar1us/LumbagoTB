package com.example.TradeBoot.app;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.services.*;
import com.example.TradeBoot.configuration.TestConfig;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.configuration.TestUtils;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.tradeloop.*;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.services.*;
import com.example.TradeBoot.trade.services.IPositionStatusService;
import com.example.TradeBoot.trade.tradeloop.interfaces.ITradeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReworkHardCodeShortTests {


    private static HttpClientWorker httpClient;

    private static IPositionsService positionsService;
    private static IOrdersService.Base ordersService;
    private static IMarketService marketService;
    private static ClosePositionInformationService closePositionInformationService;
    private static IWalletService walletService;
    private static FinancialInstrumentPositionsService financialInstrumentPositionsService;

    @BeforeAll
    static void init() {
        httpClient = TestServiceInstances.getHttpClient();
        positionsService = TestServiceInstances.getPositionsService();
        ordersService = TestServiceInstances.getOrdersService();
        marketService = TestServiceInstances.getMarketService();
        walletService = TestServiceInstances.getWalletService();

        var coinHandler = new VolumeVisitor.CoinVolumeVisitor(walletService);
        var futureHandler = new VolumeVisitor.FutureVolumeVisitor(positionsService);

        closePositionInformationService = new ClosePositionInformationService(
                walletService,
                TestServiceInstances.getFinancialInstrumentService(), positionsService, coinHandler, futureHandler);

        financialInstrumentPositionsService = TestServiceInstances.getFinancialInstrumentPositionsService();
    }


    public void canCorrectClose() {
        String marketName = TestConfig.MARKET_NAME;
        var market = marketService.getMarket(marketName);
        var orderBook = marketService.getOrderBook(marketName, 5);
        var incrementSize = market.getSizeIncrement();
        long tradeDelay = 0;


        TestUtils.printAccountInfo();
        TestUtils.printPosition(marketName);
        TestUtils.printPositions();
        TestUtils.printBalances();
        TestUtils.printBalance(marketName);
        TestUtils.printOpenOrders(marketName);

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(
                new OrderInformation(
                        BigDecimal.valueOf(0.1),
                        ESide.BUY,
                        new Persent(5)
                )
        );

        MarketInformation marketInformation = new MarketInformation(
                marketName,
                tradeDelay,
                new Persent(0.3));

        TradeInformation tradeInformation = new TradeInformation(orderInformations);

        var positionSize = financialInstrumentPositionsService.getPositionNetSize(marketInformation.market());
        var closePostionMarketTradeInformation = closePositionInformationService
                .createTradeInformation(positionSize);

        if (closePostionMarketTradeInformation.isPresent()) {
            System.out.println("Need to close position");
            System.out.println("\t" + closePostionMarketTradeInformation.get());
        } else {
            System.out.println("Not need to close position");
        }
    }

    @Test
    public void Print() {
        String marketName = TestConfig.SHORT_MARKET_NAME;
        var market = marketService.getMarket(marketName);
        var orderBook = marketService.getOrderBook(marketName, 5);
        var incrementSize = market.getSizeIncrement();
        long tradeDelay = 0;


        TestUtils.printAccountInfo();
        TestUtils.printPosition(marketName);
        TestUtils.printPositions();
        TestUtils.printBalances();
        TestUtils.printBalance(marketName);
        TestUtils.printOpenOrders(marketName);
    }


    public void handleShort() {
        String marketName = TestConfig.SHORT_MARKET_NAME;
        var market = marketService.getMarket(marketName);
        var orderBook = marketService.getOrderBook(marketName, 5);
        var incrementSize = market.getSizeIncrement();
        long tradeDelay = 0;


        TestUtils.printAccountInfo();
        TestUtils.printPosition(marketName);
        TestUtils.printPositions();
        TestUtils.printBalances();
        TestUtils.printBalance(marketName);
        TestUtils.printOpenOrders(marketName);

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(
                BigDecimal.valueOf(1L),
                ESide.BUY,
                new Persent(5)
        ));

        MarketInformation marketInformation = new MarketInformation(
                marketName,
                tradeDelay,
                new Persent(0.3));

        var placeWithDelay = new PlaceWithDelay(ordersService);
        var replaceOrder = new ReplaceByOne(ordersService, placeWithDelay);
        var closeOrders = new CloseByOne(ordersService, marketInformation);

        var globalWorkStatus =  new WorkStatus(false);


        ITradeService tradeService = new SaleProduction(
                marketService,
                new OrderPriceService(),
                financialInstrumentPositionsService,
                closePositionInformationService,
                placeWithDelay,
                replaceOrder,
                marketInformation,
                globalWorkStatus
        );

        LocalTradeLoop localTradeLoop = new LocalTradeLoop(
                tradeService,
                closeOrders,
                globalWorkStatus
        );

    }


    public void canShortWhereNoAccessToShort() {
        String marketName = "FTT/USD";
        var market = marketService.getMarket(marketName);
        var orderBook = marketService.getOrderBook(marketName, 5);
        var incrementSize = market.getSizeIncrement();
        long tradeDelay = 0;

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(
                BigDecimal.valueOf(1L),
                ESide.SELL,
                new Persent(5)
        ));

        MarketInformation marketInformation = new MarketInformation(
                marketName,
                tradeDelay,
                new Persent(0.3));

        var placeWithDelay = new PlaceWithDelay(ordersService);
        var replaceOrder = new ReplaceByOne(ordersService, placeWithDelay);
        var closeOrders = new CloseByOne(ordersService, marketInformation);

        var globalWorkStatus =  new WorkStatus(false);


        ITradeService tradeService = new SaleProduction(
                marketService,
                new OrderPriceService(),
                financialInstrumentPositionsService,
                closePositionInformationService,
                placeWithDelay,
                replaceOrder,
                marketInformation,
                globalWorkStatus
        );


        LocalTradeLoop localTradeLoop = new LocalTradeLoop(
                tradeService,
                closeOrders,
                globalWorkStatus
        );

        TradeInformation tradeInformation = new TradeInformation(orderInformations);

        var positionStatus = new IPositionStatusService() {

            @Override
            public boolean getPositionStatus(String marketName) {
                return false;
            }
        };

        localTradeLoop.runTrade();
    }


}
