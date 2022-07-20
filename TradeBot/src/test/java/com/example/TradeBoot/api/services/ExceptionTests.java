package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.OrderAlreadyClosedException;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.trade.calculator.OrderPriceCalculator;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTests {

    private static HttpClientWorker httpClient;
    private static OrdersService ordersService;

    private static OrderPriceCalculator orderPriceCalculator;


    @BeforeAll
    static void init() {
        httpClient = TestServiceInstances.getHttpClient();
        ordersService = TestServiceInstances.getOrdersService();
        orderPriceCalculator = TestServiceInstances.getOrderPriceCalculator();
    }

    @Test
    void orderAlreadyClosed() throws BadRequestByFtxException {
        String marketName = "GMT/USD";
        var marketService = new IMarketService.Base(httpClient);

        var orderBook = marketService.getOrderBook(marketName, 20);
        var bestBid = orderBook.getBestBid().getPrice();
        var persentDistance = new Persent(5);
        var size = 1L;

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(BigDecimal.valueOf(size), ESide.BUY, persentDistance));

        var t = orderPriceCalculator.createOrdersToPlaceMap(orderBook, orderInformations, marketName);

        for (OrderToPlace value : t.values()) {
            //System.out.println("OrderToPlace " + value.toString());
        }

        assertThrows(OrderAlreadyClosedException.class,
                () -> {
                    try {
                        List<Order> placed = new ArrayList<>();
                        for (OrderToPlace value : t.values()) {
                            placed.add(ordersService.placeOrder(value));
                        }

                        for (Order order : placed) {
                            ordersService.cancelOrder(order.getId());
                        }
                        Thread.sleep(5000);
                        for (Order order : placed) {
                            ordersService.cancelOrder(order.getId());
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });


    }

}
