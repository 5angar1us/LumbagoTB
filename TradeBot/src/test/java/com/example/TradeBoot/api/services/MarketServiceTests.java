package com.example.TradeBoot.api.services;


import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.configuration.TestConfig;
import com.example.TradeBoot.configuration.TestServiceInstances;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
public class MarketServiceTests {

    private static HttpClientWorker httpClient;

    private static MarketService marketService;

    @BeforeAll
    static void init() {
        httpClient = TestServiceInstances.getHttpClient();
        marketService = TestServiceInstances.getMarketService();

    }


    @Test
    public void canGetOrderBook(){
        var t = marketService.getOrderBook("SOL/USD", 20);
    }

    @Test
    public void canGetAllMarkets(){
        var t = marketService.getAllMarkets();

        for (var item : t) {
            System.out.println(item.getName());
        }
    }
}
