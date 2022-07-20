package com.example.TradeBoot.api.services;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.orders.EType;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;
import com.example.TradeBoot.api.http.HttpClientWorker;
import com.example.TradeBoot.api.http.HttpRequestFactory;
import com.example.TradeBoot.api.http.HttpResponseHandler;
import com.example.TradeBoot.configuration.TestConfig;
import com.example.TradeBoot.configuration.TestServiceInstances;
import com.example.TradeBoot.configuration.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderServicesTest {

    private static HttpClientWorker httpClient;
    private static OrdersService ordersService;

    @BeforeAll
    static void init() {
        httpClient = TestServiceInstances.getHttpClient();
        ordersService = TestServiceInstances.getOrdersService();
    }


    public void t(){
        String marketName = "GMT/USD";
        var marketService = new IMarketService.Base(httpClient);

        var OrderBook = marketService.getOrderBook(marketName, 20);
        var bestBid = OrderBook.getBestBid().getPrice();
        var distance1Percent = new BigDecimal(5);
        var targetVolume = GetDistanceVolume(bestBid, distance1Percent);
        System.out.println("Best bid: " + bestBid + "\nDistance percent: " + distance1Percent + "\nTarget Price: " + targetVolume);

        var size = 1L;
        var orderToPlace = new OrderToPlace(marketName, ESide.BUY, targetVolume, EType.LIMIT, BigDecimal.valueOf(size));
        System.out.println("OrderToPlace " + orderToPlace.toString());

        try {

            System.out.println("!!! Start placeOrder !!!");

            System.out.println(ordersService.placeOrder(orderToPlace));

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
    private BigDecimal GetDistanceVolume(BigDecimal bestBid, BigDecimal distancePercent) {
        var value = bestBid
                .divide(BigDecimal.valueOf(100L), 15, RoundingMode.HALF_UP)
                .multiply(distancePercent);

        var value2 = bestBid.subtract(value);
        var scalded = value2.setScale(6, RoundingMode.HALF_UP);
        return scalded;
    }


}
