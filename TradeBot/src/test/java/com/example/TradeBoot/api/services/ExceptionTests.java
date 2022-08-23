package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.OrderAlreadyClosedException;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTests {

    private static IOrdersService.Base ordersService;

    private static IMarketService marketService;

    private static OrderPriceService orderPriceService;

    @BeforeAll
    static void init() {
        ordersService = TestServiceInstances.getOrdersService();
        orderPriceService = TestServiceInstances.getOrderPriceCalculator();
        marketService = TestServiceInstances.getMarketService();
    }

    @Test
    void orderAlreadyClosed() throws BadRequestByFtxException {
        String marketName = "GMT/USD";

        var orderBook = marketService.getOrderBook(marketName, 20);
        var bestBid = orderBook.getBestBid().getPrice();
        var persentDistance = new Persent(5);
        var size = 1L;

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(BigDecimal.valueOf(size), ESide.BUY, persentDistance));

        var ordersToPlaceMap = orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, marketName);

        for (OrderToPlace value : ordersToPlaceMap.values()) {
            //System.out.println("OrderToPlace " + value.toString());
        }

        assertThrows(OrderAlreadyClosedException.class,
                () -> {
                    try {
                        List<Order> placed = new ArrayList<>();
                        for (OrderToPlace value : ordersToPlaceMap.values()) {
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
