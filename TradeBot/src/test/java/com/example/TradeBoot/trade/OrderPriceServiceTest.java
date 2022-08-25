package com.example.TradeBoot.trade;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.services.IMarketService;
import com.example.TradeBoot.api.utils.ESideChange;
import com.example.TradeBoot.trade.model.Persent;
import com.example.TradeBoot.trade.services.OrderPriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class OrderPriceServiceTest {

    @Autowired
    OrderPriceService orderPriceService;

    @Autowired
    IMarketService marketService;


    @Test
    public void canCalculateMarketPrice() {
        var marketName = "CEL-PERP";
        var targetSide = ESide.BUY;


        var orderBook = marketService.getOrderBook(marketName, 5);
        System.out.println(orderBook);

        BigDecimal worstPrice = orderPriceService.calculateMarketPrice(orderBook, targetSide);

        BigDecimal expectedPrice = orderBook.getBestBySide(ESideChange.change(targetSide)).getPrice();


        worstPrice = worstPrice.setScale(expectedPrice.scale());

        Assertions.assertEquals(expectedPrice, worstPrice);
    }
    @Test
    public void canCalculateMostFavorablePrice(){
        var orderBook = CreateOrderBook(new BigDecimal("2.8095"), new BigDecimal("0.0005"), 5, 2);
        var currentPrice1 = orderPriceService.calculateMostFavorablePrice(
                orderBook,
                new Persent(0),
                ESide.BUY,
                new BigDecimal(100));

        var currentPrice2 = orderPriceService.calculateMostFavorablePrice(
                orderBook,
                new Persent(0),
                ESide.SELL,
                new BigDecimal(201));


        System.out.println(orderBook);
        System.out.println(currentPrice1);
        System.out.println(currentPrice2);
    }

    private OrderBook CreateOrderBook(BigDecimal startPrice, BigDecimal priceStep, int oneSideCount, int spreadSizeInStepCount) {
        BigDecimal defaultVolume = new BigDecimal(100);

        BigDecimal priceAccamulator = startPrice.add(BigDecimal.ZERO);

        List<List<BigDecimal>> bids = getPrices(priceStep, oneSideCount, defaultVolume, priceAccamulator)
                .stream()
                .sorted(Comparator.comparing(orderBookLine -> orderBookLine.get(0)))
                .collect(Collectors.toList());
        Collections.reverse(bids);

        for (var i = 0; i < oneSideCount + spreadSizeInStepCount; i++) {
            priceAccamulator = priceAccamulator.add(priceStep);
        }

        List<List<BigDecimal>> asks = getPrices(priceStep, oneSideCount, defaultVolume, priceAccamulator);

        var orderBook = new OrderBook();
        orderBook.setAsks(asks);
        orderBook.setBids(bids);
        return orderBook;
    }

    private List<List<BigDecimal>> getPrices(BigDecimal step, int oneSideCount, BigDecimal defaultVolume, BigDecimal priceAccamulator) {
        List<List<BigDecimal>> prices = new ArrayList<>();
        for (var i = 0; i < oneSideCount; i++) {

            var orderBookLine = new ArrayList<BigDecimal>();
            orderBookLine.add(priceAccamulator);
            orderBookLine.add(defaultVolume.multiply(new BigDecimal(i + 1)));

            prices.add(orderBookLine);
            priceAccamulator = priceAccamulator.add(step);
        }
        return prices.stream().sorted(Comparator.comparing(orderBookLine -> orderBookLine.get(0))).collect(Collectors.toList());
    }
}



