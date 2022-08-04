package com.example.TradeBoot.app;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.services.OrdersService;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.trade.services.TradingEngineService;
import com.example.TradeBoot.ui.models.TradeSettings;
import com.example.TradeBoot.ui.models.TradeSettingsDetail;
import com.example.TradeBoot.ui.models.TradingStrategy;
import com.example.TradeBoot.ui.service.MockTradeSettingsService;
import com.example.TradeBoot.ui.service.TradeStatusService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class TradingEngineServiceTest {



    private static MockTradeSettingsService mockTradeSettingsService;

    private static TradingEngineService tradeEngineService;

    private static TradeStatusService tradeStatusService;

    private static OrdersService ordersService;

    @BeforeAll
    static void init() {

        mockTradeSettingsService = new MockTradeSettingsService();

        ordersService = TestServiceInstances.getOrdersService();




        tradeEngineService = new TradingEngineService(
                TestServiceInstances.getOrdersService(),
                TestServiceInstances.getMarketService(),
                TestServiceInstances.getWalletService(),
                TestServiceInstances.getOrderPriceCalculator(),
                TestServiceInstances.getClosePositionInformationService(),
                mockTradeSettingsService,
                TestServiceInstances.getFinancialInstrumentPositionsService()
        );


        tradeStatusService = new TradeStatusService(
                mockTradeSettingsService,
                TestServiceInstances.getWalletService(),
                TestServiceInstances.getOrdersService(),
                TestServiceInstances.getPositionsService());

    }

    @Test
    public void isCurrentStop() throws InterruptedException {
        var marketName = "SOL/USD";
        var maximumDefenition = new BigDecimal("0.01");
        var tradeDelay = 50L;

        var tradeSettingsDetail = new TradeSettingsDetail(TradingStrategy.LONG, 1, new BigDecimal("5"));


        var tradeSettings = new TradeSettings(marketName, maximumDefenition, tradeDelay);
        tradeSettings.getTradeSettingsDetails().add(tradeSettingsDetail);

        mockTradeSettingsService.setTradeSettings(List.of(tradeSettings));

        tradeEngineService.correctStart();
        Thread.sleep(2000);
        tradeEngineService.saveStop();
        Thread.sleep(2000);


        var isAllTreadsStopped = tradeEngineService.isDone(2, TimeUnit.SECONDS);

        var openOrders = tradeStatusService.getOpenOrdersByConfiguration();
        var openOrdersSize = openOrders.size();

        if (openOrders.size() > 0) {
            openOrders.stream().forEach(openOrder -> {
                try {
                    ordersService.cancelAllOrderByMarket(openOrder.marketName());
                } catch (BadRequestByFtxException e) {
                    fail(e.getMessage());
                }
            });
        }

        assertTrue("Is all threads in ExecutorService stopped", isAllTreadsStopped);
        assertEquals(0, openOrdersSize);
    }

    @Test
    public void isStoppedWhereCloseOrder() throws InterruptedException {
        var marketName = "SOL/USD";
        var maximumDefenition = new BigDecimal("0.01");
        var tradeDelay = 50L;

        var tradeSettingsDetail = new TradeSettingsDetail(TradingStrategy.LONG, 1, new BigDecimal("5"));


        var tradeSettings = new TradeSettings(marketName, maximumDefenition, tradeDelay);
        tradeSettings.getTradeSettingsDetails().add(tradeSettingsDetail);

        mockTradeSettingsService.setTradeSettings(List.of(tradeSettings));

        tradeEngineService.correctStart();

        try {
            ordersService.cancelAllOrderByMarket(marketName);
        } catch (BadRequestByFtxException e) {
            fail(e.getMessage());
        }
        Thread.sleep(2000);
        var isAllRunnableEnginesStopped = tradeEngineService.runnableEnginesCount() == 0;

        var openOrders = tradeStatusService.getOpenOrdersByConfiguration();

        if (openOrders.size() > 0) {
            openOrders.stream().forEach(openOrder -> {
                try {
                    ordersService.cancelAllOrderByMarket(openOrder.marketName());
                } catch (BadRequestByFtxException e) {
                    fail(e.getMessage());
                }
            });
        }
        tradeEngineService.saveStop();
        assertTrue("Is all RunnableEngines stopped", isAllRunnableEnginesStopped);
    }
}
