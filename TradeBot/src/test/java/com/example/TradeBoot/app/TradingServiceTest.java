package com.example.TradeBoot.app;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.configuration.TestConfig;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.configuration.TestUtils;

import com.example.TradeBoot.trade.services.TradeService;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.model.MarketInformation;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.services.FinancialInstrumentPositionsService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TradingServiceTest {

    private static OrdersService ordersService;
    private static IMarketService marketService;
    public static FinancialInstrumentPositionsService financialInstrumentPositionsService;

    @BeforeAll
    static void init() {

        ordersService = TestServiceInstances.getOrdersService();
        marketService = TestServiceInstances.getMarketService();

        financialInstrumentPositionsService = TestServiceInstances.getFinancialInstrumentPositionsService();
    }


    public void canSetLimitOrder() {

        String marketName = TestConfig.MARKET_NAME;
        var market = marketService.getMarket(marketName);
        var orderBook = marketService.getOrderBook(marketName, 5);
        var incrementSize = market.getSizeIncrement();
        long tradeDelay = 0;


        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(
                BigDecimal.valueOf(1L),
                ESide.BUY,
                new Persent(5)
        ));

        MarketInformation marketInformation = new MarketInformation(
                marketName,
                tradeDelay
        );

        TradeService tradeService = new TradeService(
                ordersService,
                marketService,
                new OrderPriceService(),
                marketInformation,
                new Persent(1),
                financialInstrumentPositionsService);

        TestUtils.printOpenOrders(marketName);
        System.out.println("Start catching slip");
//        tradingService.workWithOrders(orderInformations);
        System.out.println("End catching slip");
        TestUtils.printOpenOrders(marketName);

    }

}
