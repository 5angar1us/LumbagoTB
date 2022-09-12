package com.example.TradeBoot.app;

import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.services.OrderPriceService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

public class TradeTest {

    @Autowired
    IOrdersService.Base ordersService;
    @Autowired
    IMarketService marketService;
    @Autowired
    OrderPriceService orderPriceService;

//    public void t(){
//        var marketName = "TESTCEL-PERP";
//
//
//        var orderBook = new OrderBook();
//
//        var market = new Market();
//        market.setName(marketName);
//        List<Market> markets = new ArrayList<>();
//
//        var orderService = new IMarketService.Mock();
//        orderService.setOrderBook(orderBook);
//        orderService.setMarkets(markets);
//
//        TradeSettings tradeSettings = new TradeSettings();
//        tradeSettings.setMarketName(marketName);
//        tradeSettings.setTradeDelay(1000);
//        tradeSettings.setMaximumDefinition(new BigDecimal(0.3));
//
//
//        var tradeSettingsDetail1 = new TradeSettingsDetail();
//        tradeSettingsDetail1.setTradingStrategy(TradingStrategy.LONG);
//        tradeSettingsDetail1.setVolume(10);
//        tradeSettingsDetail1.setPriceOffset(new BigDecimal(1));
//
//        List<TradeSettingsDetail> tradeSettingsDetails = new ArrayList<>();
//        tradeSettingsDetails.add(tradeSettingsDetail1);
//
//       var tradeEngineService = new TradingEngineService(ordersService, );
//       tradeEngineService.correctStart();
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        tradeEngineService.saveStop();
//    }
}
