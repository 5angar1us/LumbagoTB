package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.orders.Order;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.configuration.TestUtils;
import com.example.TradeBoot.trade.services.OrderPriceService;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderServicesTest {

    private static OrdersService ordersService;

    private static IMarketService marketService;

    private static OrderPriceService orderPriceService;

    private static HttpClientWorker httpClientWorker;

    @BeforeAll
    static void init() {
        marketService = TestServiceInstances.getMarketService();
        ordersService = TestServiceInstances.getOrdersService();
        orderPriceService = TestServiceInstances.getOrderPriceCalculator();
        httpClientWorker = TestServiceInstances.getHttpClient();
    }


    public void canSet() {
        String marketName = "GMT/USD";

        var orderBook = marketService.getOrderBook(marketName, 20);
        var bestBid = orderBook.getBestBid().getPrice();
        var persentDistance = new Persent(5);
        var size = 1L;

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(BigDecimal.valueOf(size), ESide.BUY, persentDistance));

        var ordersToPlaceMap = orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, marketName);

        for (OrderToPlace orderToPlace : ordersToPlaceMap.values()) {
            System.out.println("OrderToPlace " + orderToPlace.toString());
        }

        try {

            System.out.println("!!! Start placeOrder !!!");

            for (OrderToPlace orderToPlace : ordersToPlaceMap.values()) {
                System.out.println(ordersService.placeOrder(orderToPlace));
            }

            var openOrders = ordersService.getOpenOrders(marketName);

            ordersService.cancelAllOrder();

        } catch (BadRequestByFtxException e) {
            System.out.println(e.getMessage());
            TestUtils.printOpenOrders(marketName);

            try {
                ordersService.cancelAllOrder();
            } catch (BadRequestByFtxException ex) {
                System.out.println("!!!!!!!!!!!!!!!!!!!Я не могу закрыть ордера!!!!!!!!!!!!!!!!!!!!");
            }

            TestUtils.printOpenOrders(marketName);
            System.out.println("!!! All Orders emergency canceled !!!");

        }
        System.out.println("LOG END");
    }

    //не работает хз почему
    @Test
    public void canModifyOrder() {
        String marketName = "GMT/USD";

        var orderBook = marketService.getOrderBook(marketName, 5);
        var bestBid = orderBook.getBestBid().getPrice();
        var persentDistance1 = new Persent(5);
        var persentDistance2 = new Persent(6);
        var size = 1L;

        List<OrderInformation> orderInformations = new ArrayList<>();
        orderInformations.add(new OrderInformation(BigDecimal.valueOf(size), ESide.BUY, persentDistance1));
        orderInformations.add(new OrderInformation(BigDecimal.valueOf(size), ESide.BUY, persentDistance2));

        int placedIndex = 0;
        int newPriceIndex = 1;


        var ordersToPlaceMap = orderPriceService.createOrdersToPlaceMap(orderBook, orderInformations, marketName);




        try {
            //var clientId = "dfhghfgjhgdfjghjkghuygfj1323124124";
            System.out.println("!!! Start placeOrder !!!");
            var toPlaceOrderInformation = orderInformations.get(placedIndex);
            var orderToPlace = ordersToPlaceMap.get(toPlaceOrderInformation);
            //orderToPlace.setClientId(clientId);
            System.out.println("OrderToPlace " + ordersToPlaceMap.get(orderInformations.get(placedIndex)).toString());


            var placedOrder = ordersService.placeOrder(orderToPlace);

            var modifyOrderInformation = orderInformations.get(newPriceIndex);
            var newPrice = ordersToPlaceMap.get(modifyOrderInformation).getPrice();

            modifyOrderPrice1(placedOrder, newPrice);
            //modifyOrderPrice2(placedOrder, newPrice, clientId);


            var openOrders = ordersService.getOpenOrders(marketName);
            ordersService.cancelAllOrder();

        } catch (BadRequestByFtxException e) {
            System.out.println(e.getMessage());
            TestUtils.printOpenOrders(marketName);

            try {
                ordersService.cancelAllOrder();
            } catch (BadRequestByFtxException ex) {
                System.out.println("!!!!!!!!!!!!!!!!!!!Я не могу закрыть ордера!!!!!!!!!!!!!!!!!!!!");
            }

            TestUtils.printOpenOrders(marketName);
            System.out.println("!!! All Orders emergency canceled !!!");

        }
        System.out.println("LOG END");
    }

    private void modifyOrderPrice1(Order order, BigDecimal price) throws BadRequestByFtxException {

        String url = "/orders/" + order.getId() + "/modify";
        //String body = "{\"price\": " + price + "}";
        String body = "{\"size\": " + 2 + "}";
        httpClientWorker.createPostRequest(url, body);
    }

    private void modifyOrderPrice2(Order order, BigDecimal price, String clientId) throws BadRequestByFtxException {
        String url = "/orders/by_client_id/" + clientId + "/modify";
        String body = "{\"size\": " + 2 + "}";
        httpClientWorker.createPostRequest(url, body);

    }

}
