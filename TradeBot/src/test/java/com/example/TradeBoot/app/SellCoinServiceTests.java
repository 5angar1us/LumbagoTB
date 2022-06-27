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
import com.example.TradeBoot.trade.services.TradingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class SellCoinServiceTests {
    private static HttpClientWorker httpClient;

    private static PositionsService positionsService;
    private static OrdersService ordersService;
    private static MarketService marketService;

    private static WalletService walletService;

    @BeforeAll
    static void init() {
        httpClient = TestServiceInstances.getHttpClient();
        positionsService = TestServiceInstances.getPositionsService();
        ordersService = TestServiceInstances.getOrdersService();
        marketService = TestServiceInstances.getMarketService();
        walletService = TestServiceInstances.getWalletService();

    }
    @Test
    public void print(){
        String market1 = TestConfig.MARKET_NAME;
        String market2 = TestConfig.SHORT_MARKET_NAME;

        System.out.println(marketService.getMarket(market1));
        System.out.println(marketService.getMarket(market2));
    }


    public void canSetLimitOrder() {
        String marketName = TestConfig.MARKET_NAME;
        var market = marketService.getMarket(marketName);
        var orderBook = marketService.getOrderBook(marketName, 5);
        var incrementSize = market.getSizeIncrement();
        long tradingDelay = 0;


        MarketInformation marketInformation = new MarketInformation(
                marketName,
                tradingDelay
        );

        TradingService tradingService = new TradingService(
                ordersService,
                marketService,
                new OrderPriceCalculator(),
                marketInformation, new Persent(0.3)
        );
        //PrintAccountInfo();
        //PrintPosition(marketName);
        //PrintPositions();
        TestUtils.printBalances();
        TestUtils.printBalance(marketName);
        TestUtils.printOpenOrders(marketName);

        var orderInformation = getCoinOrderInformation(marketName);
        if (orderInformation.isPresent()) {
            System.out.println("Start trading");
//            tradingService.workWithOrders(orderInformation.get());
        }


        TestUtils.printBalances();
        TestUtils.printBalance(marketName);
        TestUtils.printOpenOrders(marketName);
        System.out.println("END");
    }

    private Optional<List<OrderInformation>> getCoinOrderInformation(String market) {
        var balance = walletService.getBalanceByMarket(market);

        if (balance.isEmpty()) return Optional.empty();

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(
                new OrderInformation(
                        balance.get().getFree(),
                        ESide.SELL,
                        new Persent(5))
        );
        return Optional.of(orderInformations);
    }

//    public void canSellCoint()
//    {
//        String marketName = TestConfig.MARKET_NAME;
//        var market = marketService.getMarket(marketName);
//        var orderBook = marketService.getOrderBook(marketName,5);
//        var incrementSize = market.getSizeIncrement();
//        long tradingDelay = 0;
//
//        CoinSellService coinSellService = createPositionClosingService(incrementSize, tradingDelay);
//
//        printBalances();
//        printBalance(marketName);
//        printOpenOrders(marketName);
//
//        System.out.println("Start closing");
//        coinSellService.handleSell();
//
//        printBalances();
//        printBalance(marketName);
//        printOpenOrders(marketName);
//        System.out.println("END");
//    }



}
