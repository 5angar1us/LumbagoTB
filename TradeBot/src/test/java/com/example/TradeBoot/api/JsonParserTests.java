package com.example.TradeBoot.api;

import com.example.TradeBoot.api.domain.Market;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
public class JsonParserTests {

    @Autowired
    IHttpClientWorker httpClient;

    @Test
    void canJacksonParsingJSONtoObject() throws JsonProcessingException {
        String marketName = "CEL/USD";
        String MARKETS_PATH = "/markets";
        String uri = UriComponentsBuilder.newInstance()
                .path(MARKETS_PATH)
                .path("/").pathSegment(marketName)
                .encode()
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        var market = new ObjectMapper().readValue(json, Market.class);
        System.out.println(market.getName());
    }


}
