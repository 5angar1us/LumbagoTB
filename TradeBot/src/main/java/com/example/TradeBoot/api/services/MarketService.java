package com.example.TradeBoot.api.services;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.Market;
import com.example.TradeBoot.api.domain.markets.Trade;
import com.example.TradeBoot.api.domain.markets.Trades;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import com.example.TradeBoot.api.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class MarketService {

    private static final String MARKETS_PATH = "/markets";
    private final HttpClientWorker httpClient;

    @Autowired
    public MarketService(HttpClientWorker httpClient) {
        this.httpClient = httpClient;
    }

    public List<Market> getAllMarkets() {
        String json = this.httpClient.createGetRequest(MARKETS_PATH);
        return JsonModelConverter.convertJsonToListOfModels(Market.class, json);
    }

    public Market getMarket(String marketName) {
        if (Strings.isNullOrEmpty(marketName))
            throw new IllegalArgumentException("marketName");

        String uri = UriComponentsBuilder.newInstance()
                .path(MARKETS_PATH)
                .path("/").pathSegment(marketName)
                .encode()
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        return JsonModelConverter.convertJsonToModel(Market.class, json);
    }

    public OrderBook getOrderBook(String marketName, int depth) {
        if (Strings.isNullOrEmpty(marketName))
            throw new IllegalArgumentException("marketName");
        if (depth < 1)
            throw new IllegalArgumentException("depth");

        String uri = UriComponentsBuilder.newInstance()
                .path(MARKETS_PATH)
                .path("/").pathSegment(marketName)
                .path("/orderbook")
                .query("depth={keyword}")
                .buildAndExpand(String.valueOf(depth))
                .encode()
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        return JsonModelConverter.convertJsonToModel(OrderBook.class, json);
    }

    public Trades getTrade(String marketName) {
        if (Strings.isNullOrEmpty(marketName))
            throw new IllegalArgumentException("marketName");

        String uri = UriComponentsBuilder.newInstance()
                .path(MARKETS_PATH)
                .path("/").pathSegment(marketName)
                .path("/trades")
                .encode()
                .toUriString();

        String json = this.httpClient.createGetRequest(uri);
        return new Trades(List.of(JsonModelConverter.convertJsonToModel(Trade[].class, json)));
    }



}
