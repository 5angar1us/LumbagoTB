package com.example.TradeBoot.api;

import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Lazy()
public class HttpRequestFactoryTest {

    @Autowired
    HttpRequestFactory httpRequestFactory;

    @Test
    void canCreateRequestToMarket() {
        String uri =UriComponentsBuilder.newInstance()
                .path("/markets")
                .path("/").pathSegment("SOL/USD")
                .path("/orderbook")
                .query("depth={keyword}")
                .buildAndExpand(String.valueOf(20))
                .encode()
                .toUriString();

        var body = httpRequestFactory.createGetRequest(uri);

        assertEquals("https://ftx.com/api/markets/SOL%2FUSD/orderbook?depth=20", body.uri().toString());
    }

}
