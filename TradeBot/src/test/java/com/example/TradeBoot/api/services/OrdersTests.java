package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.orders.EType;
import com.example.TradeBoot.api.domain.orders.OpenOrder;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.configuration.TestConfig;
import com.example.TradeBoot.configuration.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrdersTests {

    private static HttpClientWorker httpClient;
    private static OrdersService ordersService;

    @BeforeAll
    static void init() {
        HttpRequestFactory httpRequestFactory = new HttpRequestFactory(TestConfig.getAuntification());
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler();

        httpClient = new HttpClientWorker(httpRequestFactory, httpResponseHandler);
        ordersService = new OrdersService(httpClient);
    }

    public void canSetOrder() {

        IPlaceOrders placeOrders = (OrderToPlace orderToPlace) -> {
            PrintWriter pw = new PrintWriter(System.out, false);


            try {
                PlacedOrder placedOrder = ordersService.placeOrder(orderToPlace);
                System.out.println("PlacedOrder" + placedOrder.toString());

            } catch (BadRequestByFtxException e) {
                System.out.println(e);
            }


            pw.flush();
        };
        var marketName = "FTT/USD";
        defaultSetOrders(placeOrders, marketName);
    }


    public void canSetManyOrdersFast() {
        IPlaceOrders placeOrders = (OrderToPlace orderToPlace) -> {

            try {
                long startTime = System.nanoTime();

                var placedOrder = ordersService.placeOrder(orderToPlace);
                long stopTime = System.nanoTime();
                var placedOrder2 = ordersService.placeOrder(orderToPlace);
                long stopTime2 = System.nanoTime();
                var placedOrder3 = ordersService.placeOrder(orderToPlace);
                long stopTime3 = System.nanoTime();
                //var placedOrder4 = orderService.placeOrder(orderToPlace);
                long stopTime4 = System.nanoTime();

                double allTime = (double) (stopTime4 - startTime) / 1_000_000_000;
                double for1 = (double) (stopTime - startTime) / 1_000_000_000;
                double for2 = (double) (stopTime2 - stopTime) / 1_000_000_000;
                double for3 = (double) (stopTime3 - stopTime2) / 1_000_000_000;
                double for4 = (double) (stopTime4 - stopTime3) / 1_000_000_000;

                String t = "All time: " + allTime
                        + "\n" + "for1: " + for1
                        + "\n" + "for2: " + for2
                        + "\n" + "for3: " + for3
                        + "\n" + "for4: " + for4;

                System.out.println(t);


            } catch (BadRequestByFtxException e) {
                System.out.println(e);
            }
        };

        var marketName = "SOL/USD";
        defaultSetOrders(placeOrders, marketName);
    }

    public void canSetTwoOrdersFast() {
        IPlaceOrders placeOrders = (OrderToPlace orderToPlace) -> {
            PrintWriter pw = new PrintWriter(System.out, false);

            try {
                //pw.println("!!! Place first order !!!");
                var placedOrder = ordersService.placeOrder(orderToPlace);
                var placedOrder2 = ordersService.placeOrder(orderToPlace);
                //pw.println("PlacedOrder" + placedOrder.toString());
                //pw.println("!!! Place second order !!!");

                //pw.println("PlacedOrder" + placedOrder2.toString());

            } catch (BadRequestByFtxException e) {
                throw new RuntimeException(e);
            }


            pw.flush();
        };

        var marketName = "FTT/USD";
        defaultSetOrders(placeOrders, marketName);
    }

    public void defaultSetOrders(IPlaceOrders placeOrders, String marketName) {


        var marketService = new IMarketService.Base(httpClient);

        TestUtils.printAccountInfo();
        TestUtils.printOpenOrders(marketName);
        Separator();

        var OrderBook = marketService.getOrderBook(marketName, 20);
        var bestBid = OrderBook.getBestBid().getPrice();
        var distance1Percent = new BigDecimal(5);
        var targetVolume = GetDistanceVolume(bestBid, distance1Percent);
        System.out.println("Best bid: " + bestBid + "\nDistance percent: " + distance1Percent + "\nTarget Price: " + targetVolume);
        Separator();

        var size = 1L;
        var orderToPlace = new OrderToPlace(marketName, ESide.BUY, targetVolume, EType.LIMIT, BigDecimal.valueOf(size));
        System.out.println("OrderToPlace " + orderToPlace.toString());

        try {

            System.out.println("!!! Start placeOrder !!!");

            placeOrders.place(orderToPlace);

            TestUtils.printOpenOrders(marketName);

            var openOrders = ordersService.getOpenOrders(marketName);
            TestUtils.printOpenOrdersId(openOrders);

            for (OpenOrder openOrder : openOrders) {
                var result = ordersService.cancelOrder(openOrder.getId());
                System.out.println("Order with id:" + openOrder.getId() + " cancel " + result);
            }

            TestUtils.printOpenOrders(marketName);
            System.out.println("Orders canceled");

        } catch (Exception e) {
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


    private void Separator() {
        System.out.println();
    }


    @Test
    public void canCalculateTargetVolumbe() {
        var distance1Percent = new BigDecimal(5);
        var bestBid = new BigDecimal("48.4725");
        var targetVolume = GetDistanceVolume(bestBid, distance1Percent);
        System.out.println(targetVolume);
    }

    private BigDecimal GetDistanceVolume(BigDecimal bestBid, BigDecimal distancePercent) {
        var value = bestBid
                .divide(BigDecimal.valueOf(100L), 15, RoundingMode.HALF_UP)
                .multiply(distancePercent);

        var value2 = bestBid.subtract(value);
        var scalded = value2.setScale(6, RoundingMode.HALF_UP);
        return scalded;
    }
}
