package com.example.TradeBoot.api;

import com.example.TradeBoot.api.domain.Market;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class ReworkJsonParserTests {

    String body = "{\"name\":\"1INCH-PERP\",\"enabled\":true,\"postOnly\":false,\"priceIncrement\":1.0E-4,\"sizeIncrement\":1.0,\"minProvideSize\":1.0,\"last\":0.9744,\"bid\":0.9744,\"ask\":0.9748,\"price\":0.9744,\"type\":\"future\",\"baseCurrency\":null,\"isEtfMarket\":false,\"quoteCurrency\":null,\"underlying\":\"1INCH\",\"restricted\":false,\"highLeverageFeeExempt\":false,\"largeOrderThreshold\":500.0,\"change1h\":-0.0182367758186398,\"change24h\":-0.012465795074490726,\"changeBod\":-0.01971830985915493,\"quoteVolume24h\":9978809.6464,\"volumeUsd24h\":9978809.6464,\"priceHigh24h\":1.053,\"priceLow24h\":0.9701}";


    @Test
    void canJacksonParsingJSONtoObject() throws JsonProcessingException {
        var market = new ObjectMapper().readValue(body, Market.class);
        System.out.println(market.getName());
    }

//    @Test
//    void canGsonParsingJsonToObject(){
//        var market = new Gson().fromJson(body, Market.class);
//        System.out.println(market.getName());
//    }

//    @Test
//    void canParseOrderBook(){
//
//        String MARKETS_PATH = "/markets";
//
//        var marketName = TestConfig.MARKET_NAME;
//        var depth = 5;
//        var httpClient = TestServiceInstances.getHttpClient();
//
//        String uri = UriComponentsBuilder.newInstance()
//                .path(MARKETS_PATH)
//                .path("/").pathSegment(marketName)
//                .path("/orderbook")
//                .query("depth={keyword}")
//                .buildAndExpand(String.valueOf(depth))
//                .encode()
//                .toUriString();
//
//        String json = httpClient.createGetRequest(uri);
//        var orderBook = GsonJsonModelConverter.convertJsonToModel(GSoneOrderBook.class, json);
//        System.out.println(new CurrectOrderBook(orderBook).getBestAsk());
//        System.out.println(json);
//    }

//    class GSoneOrderBook{
//        private  List<List<BigDecimal>> asks;
//        private  List<List<BigDecimal>> bids;
//    }

//    class CurrectOrderBook{
//        private List<Ask> currentAsk;
//        private List<Bid> currentBid;
//        public CurrectOrderBook(GSoneOrderBook gSoneOrderBook)
//        {
//            currentAsk = gSoneOrderBook.asks.stream().map(Ask::new).collect(Collectors.toList());
//            currentBid = gSoneOrderBook.bids.stream().map(Bid::new).collect(Collectors.toList());
//        }
//
//        public List<Ask> getAllAsks() {
//            return this.currentAsk;
//        }
//
//        public List<Bid> getAllBids() {
//            return this.currentBid;
//        }
//
//        public Ask getBestAsk() {
//            return this.currentAsk.get(0);
//        }
//
//        public Bid getBestBid() {
//            return this.currentBid.get(0);
//        }
//
//        public Ask getAsk(int number) {
//            return this.currentAsk.get(number);
//        }
//
//        public Bid getBid(int count) {
//            return this.currentBid.get(count);
//        }
//    }
}
