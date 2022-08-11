package com.example.TradeBoot.api;

import com.example.TradeBoot.api.domain.Market;
import com.example.TradeBoot.api.http.*;
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

        var httpResponseHandler = new HttpResponseHandler(new HttpFTXResponseParser(), new HttpResponseErrorHandler());

        httpClient = new HttpClientWorker(httpRequestFactory, new HttpSendErrorHandler(), httpResponseHandler);
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


}
