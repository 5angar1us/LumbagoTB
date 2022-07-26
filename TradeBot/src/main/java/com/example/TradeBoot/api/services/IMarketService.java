package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.Market;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.markets.Trade;
import com.example.TradeBoot.api.domain.markets.Trades;
import com.example.TradeBoot.api.http.HttpClientWorkerWithDelay;
import com.example.TradeBoot.api.http.IHttpClientWorker;
import com.example.TradeBoot.api.utils.JsonModelConverter;
import com.example.TradeBoot.api.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface IMarketService {
    List<Market> getAllMarkets();

    Market getMarket(String marketName);

    OrderBook getOrderBook(String marketName, int depth);

    Trades getTrade(String marketName);

    @Service
    class Base implements IMarketService {

        private static final String MARKETS_PATH = "/markets";
        private final HttpClientWorkerWithDelay httpClient;

        @Autowired
        public Base(HttpClientWorkerWithDelay httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        public List<Market> getAllMarkets() {
            String json = this.httpClient.createGetRequest(MARKETS_PATH);
            return JsonModelConverter.convertJsonToListOfModels(Market.class, json);
        }

        @Override
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

        @Override
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

        @Override
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

    class Mock implements IMarketService {
        List<Market> markets;
        Market market;

        OrderBook orderBook;
        Trades trades;

        @Override
        public List<Market> getAllMarkets() {
            return markets;
        }

        @Override
        public Market getMarket(String marketName) {
            return market;
        }

        @Override
        public OrderBook getOrderBook(String marketName, int depth) {
            return orderBook;
        }

        @Override
        public Trades getTrade(String marketName) {
            return trades;
        }

        public void setMarkets(List<Market> markets) {
            this.markets = markets;
        }

        public void setMarket(Market market) {
            this.market = market;
        }

        public void setOrderBook(OrderBook orderBook) {
            this.orderBook = orderBook;
        }

        public void setTrades(Trades trades) {
            this.trades = trades;
        }
    }
}
