package com.example.TradeBoot.app;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.configuration.TestConfig;
import com.example.TradeBoot.configuration.TestUtils;

import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.MarketInformation;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.services.TradingService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TradingServiceTest {

    private static OrdersService ordersService;
    private static IMarketService.Base marketService;

    private static HttpClientWorker httpClient;

    @BeforeAll
    static void init() {
        HttpRequestFactory httpRequestFactory = new HttpRequestFactory(TestConfig.getAuntification());
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();

        httpClient = new HttpClientWorker(httpRequestFactory, httpResponseHandler);

        ordersService = new OrdersService(httpClient);
        marketService = new IMarketService.Base(httpClient);

    }


    public void canSetLimitOrder() {

        String marketName = TestConfig.MARKET_NAME;
        var market = marketService.getMarket(marketName);
        var orderBook = marketService.getOrderBook(marketName, 5);
        var incrementSize = market.getSizeIncrement();
        long tradeDelay = 0;


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
                marketInformation,
                new Persent(1)
        );

        TestUtils.printOpenOrders(marketName);
        System.out.println("Start catching slip");
//        tradingService.workWithOrders(orderInformations);
        System.out.println("End catching slip");
        TestUtils.printOpenOrders(marketName);

    }

}
