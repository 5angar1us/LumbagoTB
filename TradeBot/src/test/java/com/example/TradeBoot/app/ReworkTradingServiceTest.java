package com.example.TradeBoot.app;

import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.configuration.TestServiceInstances;

import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReworkTradingServiceTest {

    private static IOrdersService.Base ordersService;
    private static IMarketService marketService;
    public static FinancialInstrumentPositionsService financialInstrumentPositionsService;

    @BeforeAll
    static void init() {

        ordersService = TestServiceInstances.getOrdersService();
        marketService = TestServiceInstances.getMarketService();

        financialInstrumentPositionsService = TestServiceInstances.getFinancialInstrumentPositionsService();
    }


    public void canSetLimitOrder() {
//
//        String marketName = TestConfig.MARKET_NAME;
//        var market = marketService.getMarket(marketName);
//        var orderBook = marketService.getOrderBook(marketName, 5);
//        var incrementSize = market.getSizeIncrement();
//        long tradeDelay = 0;
//
//
//        List<OrderInformation> orderInformations = new ArrayList<>();
//        orderInformations.add(new OrderInformation(
//                BigDecimal.valueOf(1L),
//                ESide.BUY,
//                new Persent(5)
//        ));
//
//        MarketInformation marketInformation = new MarketInformation(
//                marketName,
//                tradeDelay,
//                new Persent(0.3));
//
//        LocalTradeLoop localTradeLoop = new LocalTradeLoop(
//                ordersService,
//                marketService,
//                new OrderPriceService(),
//                marketInformation
//        );
//
//        TestUtils.printOpenOrders(marketName);
//        System.out.println("Start catching slip");
////        tradingService.workWithOrders(orderInformations);
//        System.out.println("End catching slip");
//        TestUtils.printOpenOrders(marketName);

    }

}