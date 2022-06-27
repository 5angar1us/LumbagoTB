package com.example.TradeBoot.api.http;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UricComponetsBuildersTests {

    @Test
    public void canUriComponentsBuilderCreateCurrectUri() {

        var g = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ftx.com")
                .path("/")
                .path("api")
                .path("/markets")
                .path("/").pathSegment("SOL/USD")
                .path("/orderbook")
                .query("depth={keyword}")
                .buildAndExpand("20")
                .encode()
                .toUriString();

        assertEquals("https://ftx.com/api/markets/SOL%2FUSD/orderbook?depth=20", g);
    }

    @Test
    public void canUriComponentsBuilderCreateCurrectUri2() {

        var t = UriComponentsBuilder.newInstance().path("/markets")
                .path("/").pathSegment("SOL/USD")
                .path("/orderbook")
                .query("depth={keyword}")
                .buildAndExpand("20")
                .encode()
                .toUriString();


        var g = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ftx.com")
                .path("/")
                .path("api")
                .encode()
                .toUriString();


        assertEquals("https://ftx.com/api/markets/SOL%2FUSD/orderbook?depth=20", g + t);
    }
}
