package com.example.TradeBoot.app;


import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.configuration.TestServiceInstances;
import org.junit.jupiter.api.BeforeAll;

public class FutureTest {


    private static IMarketService marketService;


    @BeforeAll
    static void init() {
        marketService = TestServiceInstances.getMarketService();

    }

    public void canGetFutureFromMarket() {
        String marketName = "BTC-PERP";
        var market = marketService.getMarket(marketName);
        System.out.println(market);

    }

    public void canGetFutureOrderBookFromMarket() {
        String marketName = "BTC-PERP";
        var orderBook = marketService.getOrderBook(marketName, 5);
        System.out.println(orderBook);

    }

}
