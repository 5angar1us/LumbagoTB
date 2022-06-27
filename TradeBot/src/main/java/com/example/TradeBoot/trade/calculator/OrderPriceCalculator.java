package com.example.TradeBoot.trade.calculator;

import com.example.TradeBoot.api.domain.markets.ESide;
import com.example.TradeBoot.api.domain.markets.OrderBook;
import com.example.TradeBoot.api.domain.orders.EType;
import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.BigDecimalUtils;
import com.example.TradeBoot.trade.model.OrderInformation;
import com.example.TradeBoot.trade.model.Persent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderPriceCalculator {

    public OrderPriceCalculator() {}

    public Map<OrderInformation, OrderToPlace> createOrdersToPlaceMap(
            OrderBook orderBook,
            Set<OrderInformation> orderInformations,
            String market) {

        Map<OrderInformation, OrderToPlace> orderToPlaces = new HashMap<>();

        for (OrderInformation orderInformation : orderInformations) {
            BigDecimal price = calculateCorrectPrice(
                    orderBook,
                    orderInformation.getDistanceInPercent(),
                    orderInformation.getSide()
            );

            orderToPlaces.put(
                    orderInformation,
                    new OrderToPlace(
                            market,
                            orderInformation.getSide(),
                            price,
                            EType.LIMIT,
                            orderInformation.getVolume()
                    ));
        }
        return orderToPlaces;
    }

    public Map.Entry<OrderInformation, OrderToPlace> createOtherToPlacePair(OrderBook orderBook, OrderInformation orderInformation, String market) {
        BigDecimal price = calculateCorrectPrice(
                orderBook,
                orderInformation.getDistanceInPercent(),
                orderInformation.getSide());
        var map = new HashMap<OrderInformation, OrderToPlace>();
        map.put(
                orderInformation,
                new OrderToPlace(
                        market,
                        orderInformation.getSide(),
                        price,
                        EType.LIMIT,
                        orderInformation.getVolume()
                )
        );

        return map.entrySet().stream().findFirst().orElseThrow();
    }


    public boolean isPriceInBoarding(BigDecimal currentPrice, BigDecimal placedPrice, Persent maximumDeviation) {
        var topBoarding = targetPriceHigher(currentPrice, maximumDeviation);
        var bottomBoarding = targetPriceLower(currentPrice, maximumDeviation);

        var isLessOrEqualTopBoarding = BigDecimalUtils.check(
                topBoarding,
                BigDecimalUtils.EOperator.GREATER_THAN_OR_EQUALS,
                placedPrice
        );

        var isMoreOrEqualsBottomBoarding = BigDecimalUtils.check(
                bottomBoarding,
                BigDecimalUtils.EOperator.LESS_THAN_OR_EQUALS,
                placedPrice
        );

        return isLessOrEqualTopBoarding || isMoreOrEqualsBottomBoarding;
    }

    public BigDecimal calculateCorrectPrice(OrderBook orderBook, Persent distance, ESide side) {
        BigDecimal price;
        switch (side) {
            case BUY -> price = targetPriceLower(
                    orderBook.getBestBid().getPrice(),
                    distance);

            case SELL -> price = targetPriceHigher(
                    orderBook.getBestAsk().getPrice(),
                    distance);

            default -> throw new IllegalArgumentException("side");
        }
        return price;
    }

    private BigDecimal targetPriceHigher(BigDecimal askPrice, Persent persent) {
        return HUNDRED
                .add(persent.getValue())
                .multiply(askPrice)
                .multiply(ONE_HUNDREDTH);
    }

    private BigDecimal targetPriceLower(BigDecimal bidPrice, Persent persent) {

        return HUNDRED
                .subtract(persent.getValue())
                .multiply(bidPrice)
                .multiply(ONE_HUNDREDTH);
    }

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100L);
    private static final BigDecimal ONE_HUNDREDTH = new BigDecimal("0.01");

}
