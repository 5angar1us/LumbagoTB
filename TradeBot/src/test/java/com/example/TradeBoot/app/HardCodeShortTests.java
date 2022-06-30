package com.example.TradeBoot.app;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.services.MarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.api.services.PositionsService;
import com.example.TradeBoot.api.services.WalletService;
import com.example.TradeBoot.configuration.TestConfig;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.configuration.TestUtils;

import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.MarketInformation;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.model.TradeInformation;
import com.example.TradeBoot.trade.services.ClosePositionInformationService;
import com.example.TradeBoot.trade.services.TradingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HardCodeShortTests {


    private static HttpClientWorker httpClient;

    private static PositionsService positionsService;
    private static OrdersService ordersService;
    private static MarketService marketService;
    private static ClosePositionInformationService closePositionInformationService;
    private static WalletService walletService;

    @BeforeAll
    static void init() {
        httpClient = TestServiceInstances.getHttpClient();
        positionsService = TestServiceInstances.getPositionsService();
        ordersService = TestServiceInstances.getOrdersService();
        marketService = TestServiceInstances.getMarketService();
        walletService = TestServiceInstances.getWalletService();

        closePositionInformationService = new ClosePositionInformationService(walletService);
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
                tradeDelay
        );

        TradeInformation tradeInformation = new TradeInformation(orderInformations);


        var closePostionMarketTradeInformation = closePositionInformationService.createTradeInformation(
                tradeInformation.getBaseSide(),
                marketInformation.getMarket());

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
                tradeDelay
        );

        TradingService tradingService = new TradingService(
                ordersService,
                marketService,
                new OrderPriceCalculator(),
                marketInformation, new Persent(1)
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
                tradeDelay
        );

        TradingService tradingService = new TradingService(
                ordersService,
                marketService,
                new OrderPriceCalculator(),
                marketInformation, new Persent(0.5)
        );

        TradeInformation tradeInformation = new TradeInformation(orderInformations);

        tradingService.workWithOrders(tradeInformation);
    }


}
