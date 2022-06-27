package com.example.TradeBoot.api;

import com.example.TradeBoot.api.domain.Market;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.configuration.TestConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarketOperationTests {

    static HttpClientWorker httpClient;
    static HttpRequestFactory httpRequestFactory;

    @BeforeAll
    static void init()
    {
        httpRequestFactory = new HttpRequestFactory(TestConfig.getAuntification());

        httpClient = new HttpClientWorker(httpRequestFactory, new HttpResponseHandler());
    }

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

    void canGetAllMarketsInfo() throws JsonProcessingException {
        String uri = new StringBuilder()
                .append("/markets")
                .toString();

        var body = httpClient.createGetRequest(uri);
        var markets =  new ObjectMapper().readValue(body, Market[].class);

        for (var x : markets) {
            System.out.println(x.getName());
        }
       var node = new ObjectMapper().readTree(body);

        System.out.println(node.get(0));

    }

    void canParsingJSONtoObject() throws JsonProcessingException {
        var body = "{\"name\":\"1INCH-PERP\",\"enabled\":true,\"postOnly\":false,\"priceIncrement\":1.0E-4,\"sizeIncrement\":1.0,\"minProvideSize\":1.0,\"last\":0.9744,\"bid\":0.9744,\"ask\":0.9748,\"price\":0.9744,\"type\":\"future\",\"baseCurrency\":null,\"isEtfMarket\":false,\"quoteCurrency\":null,\"underlying\":\"1INCH\",\"restricted\":false,\"highLeverageFeeExempt\":false,\"largeOrderThreshold\":500.0,\"change1h\":-0.0182367758186398,\"change24h\":-0.012465795074490726,\"changeBod\":-0.01971830985915493,\"quoteVolume24h\":9978809.6464,\"volumeUsd24h\":9978809.6464,\"priceHigh24h\":1.053,\"priceLow24h\":0.9701}";
        var market = new ObjectMapper().readValue(body, Market.class);
        System.out.println(market.getName());
    }
}
